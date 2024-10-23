package com.dollop.app.dto;

import lombok.Data;

@Data
public class AddProductInCartDto {
	
	private Long userId;
	
	private Long productId;
}
