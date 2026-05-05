package com.example.coupon.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额值对象
 */
public class Money {
    private final Long amount;

    public Money(Long amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("\u91d1\u989d\u4e0d\u80fd\u4e3anull\u4e14\u5fc5\u987b\u5927\u4e8e\u7b49\u4e8e0");
        }
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public static Money ofFen(Long fen) {
        return new Money(fen);
    }

    public static Money ofYuan(BigDecimal yuan) {
        if (yuan == null) {
            throw new IllegalArgumentException("\u91d1\u989d\u4e0d\u80fd\u4e3anull");
        }
        BigDecimal fen = yuan.multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN);
        return new Money(fen.longValue());
    }

    public boolean greaterThanOrEqual(Money other) {
        return this.amount >= other.amount;
    }

    public boolean greaterThan(Money other) {
        return this.amount > other.amount;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other) {
        long result = this.amount - other.amount;
        if (result < 0) {
            throw new IllegalArgumentException("\u51cf\u6cd5\u8fd0\u7b97\u7ed3\u679c\u4e0d\u80fd\u5c0f\u4e8e0");
        }
        return new Money(result);
    }

    public BigDecimal toYuan() {
        return new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }
}
