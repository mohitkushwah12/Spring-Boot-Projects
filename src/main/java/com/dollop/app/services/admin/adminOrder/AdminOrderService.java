package com.dollop.app.services.admin.adminOrder;

import java.util.List;

import com.dollop.app.dto.AnalyticsResponse;
import com.dollop.app.dto.OrderDto;

public interface AdminOrderService {

	List<OrderDto> getAllPlacedOrders();
	
	OrderDto changeOrderStatus(Long orderId, String status);
	
	AnalyticsResponse calculateAnalytics();
}
