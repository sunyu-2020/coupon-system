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

public class UserCouponTest {

    private Long userId;
    private Long templateId;
    private Money couponAmount;
    private Money minConsumeAmount;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;

    @BeforeEach
    void setUp() {
        userId = 1L;
        templateId = 1L;
        couponAmount = Money.ofYuan(new BigDecimal("10.00"));
        minConsumeAmount = Money.ofYuan(new BigDecimal("50.00"));
        validStartTime = LocalDateTime.now().minusDays(1);
        validEndTime = LocalDateTime.now().plusDays(30);
    }

    @Test
    void testCreateUserCouponSuccessfully() {
        assertDoesNotThrow(() -> new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        ));
    }

    @Test
    void testCreateUserCouponWithNullUserId() {
        assertThrows(CouponException.class, () -> new UserCoupon(
                null, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        ));
    }

    @Test
    void testCreateUserCouponWithNullTemplateId() {
        assertThrows(CouponException.class, () -> new UserCoupon(
                userId, null, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        ));
    }

    @Test
    void testCreateUserCouponWithInvalidTimeRange() {
        LocalDateTime invalidEndTime = validStartTime.minusDays(1);
        assertThrows(CouponException.class, () -> new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, invalidEndTime
        ));
    }

    @Test
    void testUseCouponSuccessfully() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        Money orderAmount = Money.ofYuan(new BigDecimal("100.00"));
        assertDoesNotThrow(() -> userCoupon.use(orderAmount));
        assertEquals(CouponStatus.USED, userCoupon.getStatus());
    }

    @Test
    void testUseCouponWithInsufficientOrderAmount() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        Money insufficientOrderAmount = Money.ofYuan(new BigDecimal("10.00"));
        assertThrows(CouponException.class, () -> userCoupon.use(insufficientOrderAmount));
    }

    @Test
    void testUseCouponWhenNotInNewStatus() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        userCoupon.setStatus(CouponStatus.USED);
        Money orderAmount = Money.ofYuan(new BigDecimal("100.00"));
        assertThrows(CouponException.class, () -> userCoupon.use(orderAmount));
    }

    @Test
    void testUseCouponWhenExpired() {
        LocalDateTime pastStartTime = LocalDateTime.now().minusDays(30);
        LocalDateTime pastEndTime = LocalDateTime.now().minusDays(1);
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                pastStartTime, pastEndTime
        );
        Money orderAmount = Money.ofYuan(new BigDecimal("100.00"));
        assertThrows(CouponException.class, () -> userCoupon.use(orderAmount));
    }

    @Test
    void testIsAvailableWhenInNewStatusAndWithinValidityPeriod() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        assertTrue(userCoupon.isAvailable());
    }

    @Test
    void testIsNotAvailableWhenNotInNewStatus() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        userCoupon.setStatus(CouponStatus.USED);
        assertFalse(userCoupon.isAvailable());
    }

    @Test
    void testIsNotAvailableWhenExpired() {
        LocalDateTime pastStartTime = LocalDateTime.now().minusDays(30);
        LocalDateTime pastEndTime = LocalDateTime.now().minusDays(1);
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                pastStartTime, pastEndTime
        );
        assertFalse(userCoupon.isAvailable());
    }

    @Test
    void testIsExpiredWhenEndTimePassed() {
        LocalDateTime pastStartTime = LocalDateTime.now().minusDays(30);
        LocalDateTime pastEndTime = LocalDateTime.now().minusDays(1);
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                pastStartTime, pastEndTime
        );
        assertTrue(userCoupon.isExpired());
    }

    @Test
    void testIsExpiredWhenStatusIsExpired() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        userCoupon.setStatus(CouponStatus.EXPIRED);
        assertTrue(userCoupon.isExpired());
    }

    @Test
    void testIsNotExpiredWhenInValidPeriod() {
        UserCoupon userCoupon = new UserCoupon(
                userId, templateId, couponAmount, minConsumeAmount,
                validStartTime, validEndTime
        );
        assertFalse(userCoupon.isExpired());
    }
}
