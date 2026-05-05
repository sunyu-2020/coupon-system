package com.example.coupon.entity;

import com.example.coupon.enums.CouponType;
import com.example.coupon.exception.CouponException;
import com.example.coupon.valueobject.Money;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 优惠券模板实体
 */
@Data
public class CouponTemplate {
    private String templateId;
    private String name;
    private CouponType couponType;
    private Money couponValue;
    private Money minConsume;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalQuantity;
    private Integer userMaxQuantity;
    private Integer issuedQuantity;

    public CouponTemplate(String templateId, String name, CouponType couponType, Money couponValue,
                         Money minConsume, LocalDateTime startTime, LocalDateTime endTime,
                         Integer totalQuantity, Integer userMaxQuantity) {
        validateParams(name, couponType, couponValue, minConsume, startTime, endTime,
                      totalQuantity, userMaxQuantity);
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
    }

    private void validateParams(String name, CouponType couponType, Money couponValue,
                               Money minConsume, LocalDateTime startTime, LocalDateTime endTime,
                               Integer totalQuantity, Integer userMaxQuantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Çæ ¼å¡æ¨¡æ¿åç§°ä¸è½ä¸ºç©º");
        }
        if (couponType == null) {
            throw new IllegalArgumentException("Çæ ¼å¡ç±»åä¸è½ä¸ºç©º");
        }
        if (couponValue == null) {
            throw new IllegalArgumentException("Çæ ¼å¡å¼ä¸è½ä¸ºç©º");
        }
        if (minConsume == null) {
            throw new IllegalArgumentException("æä½æ¶è´¹é¨æ§ä¸è½ä¸ºç©º");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("æææå¼å§æ¶é´ä¸è½ä¸ºç©º");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("æææç»ææ¶é´ä¸è½ä¸ºç©º");
        }
        if (totalQuantity == null || totalQuantity <= 0) {
            throw new IllegalArgumentException("æ»åè¡éå¿é¡»å¤§äº0");
        }
        if (userMaxQuantity == null || userMaxQuantity <= 0) {
            throw new IllegalArgumentException("æ¯ä¸ªç¨æ·æå¤é¢åæ°éå¿é¡»å¤§äº0");
        }
        if (!endTime.isAfter(startTime)) {
            throw new CouponException("æ¨¡æ¿ç»ææ¶é´å¿é¡»æäºå¼å§æ¶é´");
        }
    }

    public boolean canReceive(int userReceivedCount) {
        if (userReceivedCount >= this.userMaxQuantity) {
            return false;
        }
        if (this.issuedQuantity >= this.totalQuantity) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.startTime) || now.isAfter(this.endTime)) {
            return false;
        }
        return true;
    }

    public void increaseIssuedQuantity(int quantity) {
        if (this.issuedQuantity + quantity > this.totalQuantity) {
            throw new CouponException("å·²åæ¾æ°éå ä¸å¢æ°éä¸è½è¶è¿æ»åè¡é");
        }
        this.issuedQuantity += quantity;
    }

    public boolean canIssueMore() {
        return this.issuedQuantity < this.totalQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponTemplate that = (CouponTemplate) o;
        return Objects.equals(templateId, that.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId);
    }
}
