package com.dollop.app.services.customer.wishlist;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dollop.app.dto.WishListDto;
import com.dollop.app.entity.Product;
import com.dollop.app.entity.User;
import com.dollop.app.entity.WishList;
import com.dollop.app.repository.ProductRepository;
import com.dollop.app.repository.UserRepository;
import com.dollop.app.repository.WishListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

	private final UserRepository userRepository;
	
	private final ProductRepository productRepository;
	
	private final WishListRepository wishListRepository;

	@Override
	public WishListDto addProductToWishList(WishListDto wishListDto) {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(wishListDto.getProductId());
		Optional<User> optionalUser = userRepository.findById(wishListDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalUser.isPresent()) {
			WishList wishList = new WishList();
			
			wishList.setProduct(optionalProduct.get());
			wishList.setUser(optionalUser.get());
			
			return wishListRepository.save(wishList).getWishListDto();
		}
		return null;
	}

	@Override
	public List<WishListDto> getWishlistByUserId(Long userId) {
		// TODO Auto-generated method stub
		return wishListRepository.findAllByUserId(userId).stream().map(WishList::getWishListDto).collect(Collectors.toList());
		
	}
}
