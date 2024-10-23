package com.dollop.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dollop.app.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findAllByProductId(Long productId);

}
