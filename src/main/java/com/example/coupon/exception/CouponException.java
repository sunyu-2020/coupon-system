package com.example.coupon.exception;

/**
 * 优惠券业务异常类
 */
public class CouponException extends RuntimeException {

    public CouponException(String message) {
        super(message);
    }

    public CouponException(String message, Throwable cause) {
        super(message, cause);
    }
}
