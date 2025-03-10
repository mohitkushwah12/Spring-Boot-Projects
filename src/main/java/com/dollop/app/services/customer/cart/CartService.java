package com.dollop.app.services.customer.cart;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import com.dollop.app.dto.AddProductInCartDto;
import com.dollop.app.dto.OrderDto;
import com.dollop.app.dto.PlaceOrderDto;

public interface CartService {
	
	ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
	
	OrderDto getCartByUserId(Long userId);
	
	OrderDto applyCoupon(Long userId, String code);
	
	OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
	
	OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);
	
	OrderDto placeOrder(PlaceOrderDto placeOrderDto);
	
	List<OrderDto> getAllPlacedOrders(Long userId);
	
	OrderDto searchOrderByTrackingId(UUID trackingId);
}
