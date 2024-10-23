package com.dollop.app.services.customer.review;

import java.io.IOException;

import com.dollop.app.dto.OrderedProductsResponseDto;
import com.dollop.app.dto.ReviewDto;

public interface ReviewService {
	
	OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);
	
	ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
