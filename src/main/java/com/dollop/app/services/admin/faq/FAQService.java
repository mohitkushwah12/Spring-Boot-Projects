package com.dollop.app.services.admin.faq;

import com.dollop.app.dto.FAQDto;

public interface FAQService {

	FAQDto postFAQ(Long productId, FAQDto faqDto);
}
