package com.example.coupon.dto;

import com.example.coupon.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 优惠券模板创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateCreateRequest {
    private String templateId;
    private String name;
    private CouponType couponType;
    private Long couponValue;
    private Long minConsume;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalQuantity;
    private Integer userMaxQuantity;
}
