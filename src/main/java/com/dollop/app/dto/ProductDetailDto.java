package com.dollop.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDetailDto {

	private ProductDto productDto;
	
	private List<ReviewDto> reviewDtoList;
	
	private List<FAQDto> faqDtoList;
}
