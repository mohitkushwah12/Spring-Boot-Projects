package com.dollop.app.services.customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dollop.app.dto.ProductDetailDto;
import com.dollop.app.dto.ProductDto;
import com.dollop.app.dto.UserDto;
import com.dollop.app.entity.FAQ;
import com.dollop.app.entity.Product;
import com.dollop.app.entity.Review;
import com.dollop.app.entity.User;
import com.dollop.app.repository.FAQRepository;
import com.dollop.app.repository.ProductRepository;
import com.dollop.app.repository.ReviewRepository;
import com.dollop.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {
	
	
	private final ProductRepository productRepository;
	
	private final FAQRepository faqRepository;
	
	private final ReviewRepository reviewRepository;
	
	private final UserRepository userRepository;
	
	@Override
	public List<ProductDto> getAllProducts() {
	List<Product> products = productRepository.findAll();
	return products.stream().map(Product::getDto).collect(Collectors.toList());
	}

	@Override
	public List<ProductDto> searchProductByTitle(String name) {
		List<Product> products = productRepository.findAllByNameContaining(name);
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}

	@Override
	public ProductDetailDto getProductDetailById(Long productId) {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if(optionalProduct.isPresent()) {
			List<FAQ> faqList = faqRepository.findAllByProductId(productId);
			List<Review> reviewList = reviewRepository.findAllByProductId(productId);
			
			ProductDetailDto productDetailDto = new ProductDetailDto();
			
			productDetailDto.setProductDto(optionalProduct.get().getDto());
			productDetailDto.setFaqDtoList(faqList.stream().map(FAQ::getFAQDto).collect(Collectors.toList()));
			productDetailDto.setReviewDtoList(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));
			
			return productDetailDto;
		}
		return null;
	}

	@Override
	public UserDto getProfileByUserId(Long userId) {
		// TODO Auto-generated method stub
		 Optional<User> userOptional = userRepository.findById(userId);
	        if (userOptional.isPresent()) {
	        	
	            User user = userOptional.get();
	            UserDto userDto = new UserDto();
	            userDto.setName(user.getName());
	            userDto.setEmail(user.getEmail());
	            userDto.setPassword(user.getPassword());
	            userDto.setUserRole(user.getRole());
	            
	            return userDto;
	        }
	        return null; // Or throw an exception if user not found
	    }
	}

