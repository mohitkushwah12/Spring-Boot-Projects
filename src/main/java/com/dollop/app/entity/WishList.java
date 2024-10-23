package com.dollop.app.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.dollop.app.dto.WishListDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class WishList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	public WishListDto getWishListDto() {
		// TODO Auto-generated method stub
		WishListDto wishListDto = new WishListDto();
		wishListDto.setId(id);
		wishListDto.setProductId(product.getId());
		wishListDto.setReturnedImg(product.getImg());
		wishListDto.setProductName(product.getName());
		wishListDto.setProductDescription(product.getDescription());
		wishListDto.setPrice(product.getPrice());
		wishListDto.setUserId(user.getId());
		
		return wishListDto;
	}
}
