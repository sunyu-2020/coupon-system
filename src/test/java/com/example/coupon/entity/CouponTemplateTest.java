package com.example.coupon.entity;

import com.example.coupon.enums.CouponType;
import com.example.coupon.enums.CouponStatus;
import com.example.coupon.exception.CouponException;
import com.example.coupon.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CouponTemplateTest {

    private String templateId;
    private String name;
    private CouponType couponType;
    private Money couponValue;
    private Money minConsume;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalQuantity;
    private Integer userMaxQuantity;

    @BeforeEach
    void setUp() {
        templateId = "TPL001";
        name = "\u6d4b\u8bd5\u4f18\u60e0\u5238";
        couponType = CouponType.CASH;
        couponValue = Money.ofYuan(new BigDecimal("10.00"));
        minConsume = Money.ofYuan(new BigDecimal("50.00"));
        startTime = LocalDateTime.now().minusDays(1);
        endTime = LocalDateTime.now().plusDays(30);
        totalQuantity = 1000;
        userMaxQuantity = 5;
    }

    @Test
    void testCreateCouponTemplateSuccessfully() {
        assertDoesNotThrow(() -> new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, totalQuantity, userMaxQuantity
        ));
    }

    @Test
    void testCreateCouponTemplateWithInvalidTime() {
        LocalDateTime invalidEndTime = startTime.minusDays(2);
        Exception exception = assertThrows(CouponException.class, () -> new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, invalidEndTime, totalQuantity, userMaxQuantity
        ));
    }

    @Test
    void testCanReceiveWithinLimits() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, totalQuantity, userMaxQuantity
        );
        assertTrue(template.canReceive(3));
    }

    @Test
    void testCannotReceiveWhenUserExceedsLimit() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, totalQuantity, userMaxQuantity
        );
        assertFalse(template.canReceive(userMaxQuantity));
    }

    @Test
    void testCannotReceiveWhenOutOfTotalQuantity() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, 1, userMaxQuantity
        );
        template.increaseIssuedQuantity(1);
        assertFalse(template.canReceive(0));
    }

    @Test
    void testCannotReceiveWhenOutOfDate() {
        LocalDateTime pastStartTime = LocalDateTime.now().minusDays(2);
        LocalDateTime pastEndTime = LocalDateTime.now().minusDays(1);
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                pastStartTime, pastEndTime, totalQuantity, userMaxQuantity
        );
        assertFalse(template.canReceive(0));
    }

    @Test
    void testIncreaseIssuedQuantitySuccessfully() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, 10, userMaxQuantity
        );
        assertDoesNotThrow(() -> template.increaseIssuedQuantity(5));
        assertEquals(5, template.getIssuedQuantity());
    }

    @Test
    void testIncreaseIssuedQuantityExceedsTotal() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, 10, userMaxQuantity
        );
        assertThrows(CouponException.class, () -> template.increaseIssuedQuantity(15));
    }

    @Test
    void testCanIssueMore() {
        CouponTemplate template = new CouponTemplate(
                templateId, name, couponType, couponValue, minConsume,
                startTime, endTime, 10, userMaxQuantity
        );
        assertTrue(template.canIssueMore());
        template.increaseIssuedQuantity(10);
        assertFalse(template.canIssueMore());
    }
}
