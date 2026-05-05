package com.example.coupon.dto;

import lombok.Data;

/**
 * 核销优惠券请求DTO
 */
@Data
public class UseCouponRequest {
    private String couponCode;
    private String orderAmount;
}
