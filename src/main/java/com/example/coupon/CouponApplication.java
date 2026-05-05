package com.example.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 优惠券系统主应用程序
 */
@SpringBootApplication
@MapperScan("com.example.coupon.repository")
public class CouponApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }
}
