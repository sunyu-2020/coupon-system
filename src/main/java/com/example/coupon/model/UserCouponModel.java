package com.example.coupon.model;

import com.example.coupon.enums.CouponStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户优惠券数据模型 - 用于数据库映射
 */
@Data
public class UserCouponModel {
    private Long id;
    private String couponCode;
    private Long userId;
    private String templateId;
    private Long couponValue;
    private Long minConsume;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private CouponStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserCouponModel() {}

    public UserCouponModel(String couponCode, Long userId, String templateId,
                          Long couponValue, Long minConsume, LocalDateTime validStartTime,
                          LocalDateTime validEndTime) {
        this.couponCode = couponCode;
        this.userId = userId;
        this.templateId = templateId;
        this.couponValue = couponValue;
        this.minConsume = minConsume;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.status = CouponStatus.NEW;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
