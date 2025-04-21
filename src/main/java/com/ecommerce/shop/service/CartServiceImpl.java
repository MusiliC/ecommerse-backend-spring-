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


                    List<ProductDto> productDtos = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());

                    cartDto.setProducts(productDtos);
                    return cartDto;
                })
                .toList();

        return cartDtos;
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
