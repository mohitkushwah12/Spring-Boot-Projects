package com.dollop.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dollop.app.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
