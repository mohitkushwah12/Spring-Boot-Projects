package com.dollop.app.services.customer.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.dollop.app.dto.OrderedProductsResponseDto;
import com.dollop.app.dto.ProductDto;
import com.dollop.app.dto.ReviewDto;
import com.dollop.app.entity.CartItems;
import com.dollop.app.entity.Order;
import com.dollop.app.entity.Product;
import com.dollop.app.entity.Review;
import com.dollop.app.entity.User;
import com.dollop.app.repository.OrderRepository;
import com.dollop.app.repository.ProductRepository;
import com.dollop.app.repository.ReviewRepository;
import com.dollop.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final OrderRepository orderRepository;
	
	private final ProductRepository productRepository;
	
	private final UserRepository userRepository;
	
	private final ReviewRepository reviewRepository;
	
	@Override
	public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();
		if(optionalOrder.isPresent()) {
			orderedProductsResponseDto.setOrderAmount(optionalOrder.get().getAmount());
			
			List<ProductDto> productDtoList = new ArrayList<>();
			for(CartItems cartItems: optionalOrder.get().getCartItems()) {
				ProductDto productDto = new ProductDto();
				
				productDto.setId(cartItems.getProduct().getId());
				productDto.setName(cartItems.getProduct().getName());
				productDto.setPrice(cartItems.getPrice());
				productDto.setQuantity(cartItems.getQuantity());
				
				productDto.setByteImg(cartItems.getProduct().getImg());
				
				productDtoList.add(productDto);
			}
			orderedProductsResponseDto.setProductDtoList(productDtoList);
		}
		return orderedProductsResponseDto;
	}

	@Override
	public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
		Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalUser.isPresent()) {
			Review review = new Review();
			
			review.setRating(reviewDto.getRating());
			review.setDescription(reviewDto.getDescription());
			review.setUser(optionalUser.get());
			review.setProduct(optionalProduct.get());
			review.setImg(reviewDto.getImg().getBytes());
			
			return reviewRepository.save(review).getDto();
		}
		return null;
	}
}
