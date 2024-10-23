package com.dollop.app.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.dto.OrderDto;
import com.dollop.app.services.customer.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TrackingController {

	private final CartService cartService;
	
	@GetMapping("/order/{trackingId}")
	public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId){
		OrderDto orderDto = cartService.searchOrderByTrackingId(trackingId);
		if(orderDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderDto);
	}
}
