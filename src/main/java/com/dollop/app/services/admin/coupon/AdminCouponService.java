package com.dollop.app.services.admin.coupon;

import java.util.List;

import com.dollop.app.entity.Coupon;

public interface AdminCouponService {

	Coupon createCoupon(Coupon coupon);
	
	List<Coupon> getAllCoupons();
}
