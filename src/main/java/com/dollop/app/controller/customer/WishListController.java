package com.dollop.app.controller.customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.dto.WishListDto;
import com.dollop.app.services.customer.wishlist.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class WishListController {

	private final WishListService wishListService;
	
	@PostMapping("/wishlist")
	public ResponseEntity<?> addProductToWishList(@RequestBody WishListDto wishListDto){
		WishListDto postedWishListDto = wishListService.addProductToWishList(wishListDto);
		if(postedWishListDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something Went Wrong..");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(postedWishListDto);
	}
	
	@GetMapping("/wishlist/{userId}")
	public ResponseEntity<List<WishListDto>> getWishlistByUserId(@PathVariable Long userId){
		return ResponseEntity.ok(wishListService.getWishlistByUserId(userId));
	}
}
