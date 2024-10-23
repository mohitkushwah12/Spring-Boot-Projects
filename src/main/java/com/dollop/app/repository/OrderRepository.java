package com.dollop.app.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dollop.app.entity.Order;
import com.dollop.app.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
	//@Query("SELECT o FROM Order o WHERE o.id = :id AND o.orderStatus = ?2")
	Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
	
	List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);
	
	List<Order> findByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> orderStatus);

	Optional<Order> findByTrackingId(UUID trackingId);

	List<Order> findByDateBetweenAndOrderStatus(Date startOfMonth, Date endOfMonth, OrderStatus status);

	Long countByOrderStatus(OrderStatus status);
}
