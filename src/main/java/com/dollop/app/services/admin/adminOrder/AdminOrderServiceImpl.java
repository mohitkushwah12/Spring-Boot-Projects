package com.dollop.app.services.admin.adminOrder;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.dollop.app.dto.AnalyticsResponse;
import com.dollop.app.dto.OrderDto;
import com.dollop.app.entity.Order;
import com.dollop.app.enums.OrderStatus;
import com.dollop.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService 
{
	private final OrderRepository orderRepository;
	
	public List<OrderDto> getAllPlacedOrders() {
		
		List<Order> orderList = orderRepository.
				findAllByOrderStatusIn(List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered));
		
		return orderList.stream().map(Order::getOrderDto).collect(Collectors.toList());
		
	}

	@Override
	public OrderDto changeOrderStatus(Long orderId, String status) {
		// TODO Auto-generated method stub
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if(optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			
			if(Objects.equals(status, "Shipped")) {
				order.setOrderStatus(OrderStatus.Shipped);
			}else if(Objects.equals(status, "Delivered")) {
				order.setOrderStatus(OrderStatus.Delivered);
			}
			return orderRepository.save(order).getOrderDto();
		}
		return null;
	}

	@Override
	public AnalyticsResponse calculateAnalytics() {
		// TODO Auto-generated method stub
		LocalDate currentDate = LocalDate.now();
		LocalDate previousMonthDate = currentDate.minusMonths(1);
		
		Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
		Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());
		
		Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
		Long previousMonthEarnings = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());
		
		Long placed = orderRepository.countByOrderStatus(OrderStatus.Placed);
		Long shipped = orderRepository.countByOrderStatus(OrderStatus.Shipped);
		Long delivered = orderRepository.countByOrderStatus(OrderStatus.Delivered);
		
		return new AnalyticsResponse(placed, shipped, delivered, 
				currentMonthOrders, previousMonthOrders, currentMonthEarnings, previousMonthEarnings);
	}
	
	public Long getTotalOrdersForMonth(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date startOfMonth = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		Date endOfMonth = calendar.getTime();
		
		List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);
		
		return (long)orders.size();
	}
	
	public Long getTotalEarningsForMonth(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date startOfMonth = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		Date endOfMonth = calendar.getTime();
		
		List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);
		
		Long sum = 0L;
		for(Order order: orders) {
			sum += order.getAmount();
		}
		return sum;
	}
}
