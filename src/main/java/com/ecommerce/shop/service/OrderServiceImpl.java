package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.OrderDto;
import com.ecommerce.shop.dtos.OrderItemDto;
import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Address;
import com.ecommerce.shop.models.Cart;
import com.ecommerce.shop.models.CartItem;
import com.ecommerce.shop.models.Order;
import com.ecommerce.shop.models.OrderItem;
import com.ecommerce.shop.models.OrderStatus;
import com.ecommerce.shop.models.Payment;
import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.repository.AddressRepository;
import com.ecommerce.shop.repository.CartRepository;
import com.ecommerce.shop.repository.OrderItemRepository;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.PaymentRepository;
import com.ecommerce.shop.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderServiceI {

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartService cartService;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;


    @Transactional
    @Override
    public OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "CartId", emailId);
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        Order order = new Order();

        order.setEmail(emailId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setAddress(address);

        List<CartItem> cartItems = cart.getCartItems();

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);


        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
//            Reduce Stock
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
//            Remove items from the cart
            cartService.deleteProductFromCart(cart.getCartId(), product.getProductId());
        });

        OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);
        orderItems.forEach(orderItem -> orderDto.getOrderItems().add(modelMapper.map(orderItem, OrderItemDto.class)));


        orderDto.setAddressId(address.getAddressId());

        return orderDto;
    }
}
