package com.dollop.app.services.admin.faq;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dollop.app.dto.FAQDto;
import com.dollop.app.entity.FAQ;
import com.dollop.app.entity.Product;
import com.dollop.app.repository.FAQRepository;
import com.dollop.app.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

	
	private final FAQRepository faqRepository;
	
	private final ProductRepository productRepository;
	
	public FAQDto postFAQ(Long productId, FAQDto faqDto) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if(optionalProduct.isPresent()) {
			FAQ faq = new FAQ();
			
			faq.setQuestion(faqDto.getQuestion());
			faq.setAnswer(faqDto.getAnswer());
			faq.setProduct(optionalProduct.get());
			
			return faqRepository.save(faq).getFAQDto();
		}
		return null;
	}
}
