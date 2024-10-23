package com.dollop.app.services.customer;

import java.util.List;

import com.dollop.app.dto.ProductDetailDto;
import com.dollop.app.dto.ProductDto;
import com.dollop.app.dto.UserDto;

public interface CustomerProductService {
	
	List<ProductDto> getAllProducts();

	List<ProductDto> searchProductByTitle(String name);
	
	ProductDetailDto getProductDetailById(Long productId);
	
	UserDto getProfileByUserId(Long userId);
}
