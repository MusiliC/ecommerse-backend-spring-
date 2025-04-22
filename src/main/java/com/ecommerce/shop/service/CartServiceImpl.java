package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.CartDto;
import com.ecommerce.shop.dtos.ProductDto;
import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Cart;
import com.ecommerce.shop.models.CartItem;
import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.repository.CartItemRepository;
import com.ecommerce.shop.repository.CartRepository;
import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.util.AuthUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {

        //Create or Find Existing Cart
        Cart cart = createCart();

        //Retrieve Product Details
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product already in cart");
        }

        //Validations - If available, if in the cart
        if (product.getQuantity() == 0) {
            throw new APIException("Product is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Product quantity is less than requested");
        }
        //Create cart Item

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setDiscount(product.getDiscount());
        //Save
        cartItemRepository.save(newCartItem);

        //Return updated cart
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
        cartRepo.save(cart);

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> productDtoStream = cartItems.stream()
                .map(item -> {
                    ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                    productDto.setQuantity(item.getQuantity());
                    return productDto;
                });

        cartDto.setProducts(productDtoStream.toList());

        return cartDto;
    }

    @Override
    public List<CartDto> getCarts() {

        List<Cart> carts = cartRepo.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No Cart Exists");
        }

        List<CartDto> cartDtos = carts.stream()
                .map(cart -> {
                    CartDto cartDto = modelMapper.map(cart, CartDto.class);


                    List<ProductDto> productDtos = cart.getCartItems().stream().map(cartItem -> {
                        ProductDto productDto = modelMapper.map(cartItem.getProduct(), ProductDto.class);
                        productDto.setQuantity(cartItem.getQuantity());
                        return productDto;
                    }).toList();

                    cartDto.setProducts(productDtos);
                    return cartDto;
                })
                .toList();

        return cartDtos;
    }

    @Override
    public CartDto getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDto> productDtos = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());

        cartDto.setProducts(productDtos);

        return cartDto;
    }

    @Transactional
    @Override
    public CartDto updateProductQuantity(Long productId, int quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepo.findCartByEmail(emailId);

        Long cartId = userCart.getCartId();

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException("Product is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Product quantity is less than requested");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIException("Product quantity cannot be negative");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {

            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getQuantity() * product.getPrice()));
            cartRepo.save(cart);
        }

        CartItem updateCartItem = cartItemRepository.save(cartItem);
        if (updateCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updateCartItem.getCartItemId());
        }

        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> productDtoStream = cartItems.stream()
                .map(item -> {
                    ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                    productDto.setQuantity(item.getQuantity());
                    return productDto;
                });

        cartDto.setProducts(productDtoStream.toList());

        return cartDto;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartRepo.save(cart);

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " deleted from cart";
    }

    @Override
    public void updateProductsInCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);


    }

    private Cart createCart() {
        Cart userCart = cartRepo.findCartByEmail(authUtil.loggedInUser().getEmail());

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepo.save(cart);

        return newCart;
    }
}
