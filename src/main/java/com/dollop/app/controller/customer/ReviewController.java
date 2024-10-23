package com.dollop.app.controller.customer;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dollop.app.dto.OrderedProductsResponseDto;
import com.dollop.app.dto.ReviewDto;
import com.dollop.app.services.customer.review.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	@GetMapping("/ordered-products/{orderId}")
	public ResponseEntity<OrderedProductsResponseDto> getOrderedProductsDetailsByOrderId(@PathVariable Long orderId) {
		return ResponseEntity.ok(reviewService.getOrderedProductsDetailsByOrderId(orderId));
	}
	
	@PostMapping("/review")
	public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) throws IOException {
		ReviewDto dto = reviewService.giveReview(reviewDto);
		if(dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something Went Wrong..");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
}
