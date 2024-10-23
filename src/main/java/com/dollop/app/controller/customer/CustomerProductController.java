package com.dollop.app.controller.customer;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dollop.app.dto.ProductDetailDto;
import com.dollop.app.dto.ProductDto;
import com.dollop.app.dto.UserDto;
import com.dollop.app.services.customer.CustomerProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController 
{
	private final CustomerProductService customerProductService;
	
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductDto>> getAllProducts() {
		List<ProductDto> productDtos = customerProductService.getAllProducts();
		return ResponseEntity.ok(productDtos);
	}
	
	@GetMapping("/search/{name}")
	public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
		List<ProductDto> productDtos = customerProductService.searchProductByTitle(name);
		return ResponseEntity.ok(productDtos);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable Long productId){
		ProductDetailDto productDetailDto = customerProductService.getProductDetailById(productId);
		if(productDetailDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(productDetailDto);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
		UserDto userDto = customerProductService.getProfileByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}
}
