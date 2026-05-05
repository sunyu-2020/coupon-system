package com.example.coupon.entity;

import com.example.coupon.enums.CouponStatus;
import com.example.coupon.exception.CouponException;
import com.example.coupon.valueobject.Money;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户优惠券实体
 */
@Data
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long templateId;
    private Money couponAmount;
    private Money minConsumeAmount;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private CouponStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserCoupon(Long userId, Long templateId, Money couponAmount,
                      Money minConsumeAmount, LocalDateTime validStartTime,
                      LocalDateTime validEndTime) {
        this.userId = userId;
        this.templateId = templateId;
        this.couponAmount = couponAmount;
        this.minConsumeAmount = minConsumeAmount;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.status = CouponStatus.NEW;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        validate();
    }

    private void validate() {
        if (userId == null) { throw new CouponException("ç¨æ·IDä¸è½ä¸ºç©º"); }
        if (templateId == null) { throw new CouponException("æ¨¡æ¿IDä¸è½ä¸ºç©º"); }
        if (couponAmount == null) { throw new CouponException("Çæ ¼å¡éé¢ä¸è½ä¸ºç©º"); }
        if (minConsumeAmount == null) { throw new CouponException("æä½æ¶è´¹éé¢ä¸è½ä¸ºç©º"); }
        if (validStartTime == null) { throw new CouponException("æææå¼å§æ¶é´ä¸è½ä¸ºç©º"); }
        if (validEndTime == null) { throw new CouponException("æææç»ææ¶é´ä¸è½ä¸ºç©º"); }
        if (validStartTime.isAfter(validEndTime)) { throw new CouponException("æææå¼å§æ¶é´ä¸è½æäºç»ææ¶é´"); }
    }

    public UserCoupon use(Money orderAmount) {
        if (!CouponStatus.NEW.equals(this.status)) { throw new CouponException("Çæ ¼å¡ç¶æä¸ä¸ºNEWï¼æ æ³æ ¸é"); }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.validStartTime) || now.isAfter(this.validEndTime)) { throw new CouponException("Çæ ¼å¡ä¸å¨æææå"); }
        if (orderAmount == null || !orderAmount.greaterThanOrEqual(this.minConsumeAmount)) { throw new CouponException("è®¢åéé¢ä¸æ»¡è¶³æä½æ¶è´¹è¦æ±"); }
        this.status = CouponStatus.USED;
        this.updateTime = LocalDateTime.now();
        return this;
    }

    public boolean isAvailable() {
        if (!CouponStatus.NEW.equals(this.status)) { return false; }
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(this.validStartTime) && !now.isAfter(this.validEndTime);
    }

    public boolean isExpired() {
        if (CouponStatus.EXPIRED.equals(this.status)) { return true; }
        return LocalDateTime.now().isAfter(this.validEndTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCoupon that = (UserCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
