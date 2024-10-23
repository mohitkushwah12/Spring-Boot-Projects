package com.dollop.app.services.admin.category;

import java.util.List;

import com.dollop.app.dto.CategoryDto;
import com.dollop.app.entity.Category;

public interface CategoryService 
{
	Category createCategory(CategoryDto categoryDto);
	
	List<Category> getAllCategories();
}
