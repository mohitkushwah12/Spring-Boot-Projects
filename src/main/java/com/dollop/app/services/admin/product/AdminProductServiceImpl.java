package com.dollop.app.services.admin.product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.dollop.app.dto.ProductDto;
import com.dollop.app.entity.Category;
import com.dollop.app.entity.Product;
import com.dollop.app.repository.CategoryRepository;
import com.dollop.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService 
{
	private final ProductRepository productRepository;
	
	private final CategoryRepository categoryRepository;

	@Override
	public ProductDto addProduct(ProductDto productDto) throws IOException {
		
		Product product = new Product();
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setImg(productDto.getImg().getBytes());
		
		Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
		
		product.setCategory(category);
		
		return productRepository.save(product).getDto();
	}

	@Override
	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}

	@Override
	public List<ProductDto> getAllProductByName(String name) {
		List<Product> products = productRepository.findAllByNameContaining(name);
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}

	@Override
	public boolean deleteProduct(Long id) {
		Optional<Product> optional = productRepository.findById(id);
		if(optional.isPresent()) {
			productRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public ProductDto getProductById(Long productId) {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if(optionalProduct.isPresent()) {
			return optionalProduct.get().getDto();
		}else {
			return null;
		}
	}

	@Override
	public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(productId);
		Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
		if(optionalProduct.isPresent() && optionalCategory.isPresent()) {
			Product product = optionalProduct.get();
			
			product.setName(productDto.getName());
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());
			product.setCategory(optionalCategory.get());
			
			if(productDto.getImg() != null) {
				product.setImg(productDto.getImg().getBytes());
			}
			return productRepository.save(product).getDto();
		}else {
			return null;
		}
	}

}
