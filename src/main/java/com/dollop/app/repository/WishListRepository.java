package com.dollop.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dollop.app.entity.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long>{
	
	List<WishList> findAllByUserId(Long userId);
}
