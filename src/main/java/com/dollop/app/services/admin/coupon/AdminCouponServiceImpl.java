package com.dollop.app.services.admin.coupon;

import java.util.List;

import org.springframework.stereotype.Service;
import com.dollop.app.entity.Coupon;
import com.dollop.app.exceptions.ValidationException;
import com.dollop.app.repository.CouponRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService {

	private final CouponRepository couponRepository;
	
	@Override
	public Coupon createCoupon(Coupon coupon) {
		if(couponRepository.existsByCode(coupon.getCode())) {
			throw new ValidationException("Coupon code Already exists");
		}
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> getAllCoupons() {
		return couponRepository.findAll();
	}
}
