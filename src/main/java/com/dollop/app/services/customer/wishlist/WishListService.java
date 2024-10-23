package com.dollop.app.services.customer.wishlist;

import java.util.List;

import com.dollop.app.dto.WishListDto;

public interface WishListService {
	
	WishListDto addProductToWishList(WishListDto wishListDto);
	
	List<WishListDto> getWishlistByUserId(Long userId);

}
