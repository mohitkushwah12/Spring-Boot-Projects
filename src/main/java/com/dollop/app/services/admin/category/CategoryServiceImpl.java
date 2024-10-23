package com.dollop.app.services.admin.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dollop.app.dto.CategoryDto;
import com.dollop.app.entity.Category;
import com.dollop.app.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

	private final CategoryRepository categoryRepository;

	@Override
	public Category createCategory(CategoryDto categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		System.out.println(category);
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}
}
