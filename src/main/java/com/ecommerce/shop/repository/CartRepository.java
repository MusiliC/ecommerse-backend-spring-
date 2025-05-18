package com.ecommerce.shop.repository;

import com.ecommerce.shop.models.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);


    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.product.productId = ?1")
    List<Cart> findCartsByProductId(Long productId);

}
