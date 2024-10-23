package com.dollop.app.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.entity.Coupon;
import com.dollop.app.exceptions.ValidationException;
import com.dollop.app.services.admin.coupon.AdminCouponService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {

	private final AdminCouponService adminCouponService;
	
	
	@PostMapping
	public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon){
		try {
			Coupon createCoupon = adminCouponService.createCoupon(coupon);
			return ResponseEntity.ok(createCoupon);
		}
		catch(ValidationException ve) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Coupon>> getAllCoupons(){
		return ResponseEntity.ok(adminCouponService.getAllCoupons());
	}
}
