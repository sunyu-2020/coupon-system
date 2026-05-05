package com.example.coupon.enums;

/**
 * 优惠券状态枚举
 */
public enum CouponStatus {
    NEW("NEW", "未使用"),
    USED("USED", "已使用"),
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String desc;

    CouponStatus(String code, String desc) {
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
