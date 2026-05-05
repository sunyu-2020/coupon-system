package com.example.coupon.model;

import com.example.coupon.enums.CouponType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券模板数据模型 - 用于数据库映射
 */
@Data
public class CouponTemplateModel {
    private Long id;
    private String templateId;
    private String name;
    private CouponType couponType;
    private Long couponValue;
    private Long minConsume;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalQuantity;
    private Integer userMaxQuantity;
    private Integer issuedQuantity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public CouponTemplateModel() {}

    public CouponTemplateModel(String templateId, String name, CouponType couponType,
                              Long couponValue, Long minConsume, LocalDateTime startTime,
                              LocalDateTime endTime, Integer totalQuantity, Integer userMaxQuantity) {
        this.templateId = templateId;
        this.name = name;
        this.couponType = couponType;
        this.couponValue = couponValue;
        this.minConsume = minConsume;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalQuantity = totalQuantity;
        this.userMaxQuantity = userMaxQuantity;
        this.issuedQuantity = 0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
