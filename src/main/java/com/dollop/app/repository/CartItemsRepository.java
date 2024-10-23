package com.dollop.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dollop.app.entity.CartItems;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long>
{

	Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long orderId, Long userId);

//	@Query("SELECT  c FROM CartItems c WHERE user.id =: userId AND product.id=:productId")
//	CartItems findCartItemByUserId(Long userId,Long productId);

	Optional<CartItems> findByProductIdAndOrderId(Long productId, Long orderId);

}