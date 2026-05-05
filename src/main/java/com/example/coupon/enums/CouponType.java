package com.example.coupon.enums;

/**
 * 优惠券类型枚举
 */
public enum CouponType {
    CASH("CASH", "现金券"),
    DISCOUNT("DISCOUNT", "折扣券");

    private final String code;
    private final String desc;

    CouponType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
