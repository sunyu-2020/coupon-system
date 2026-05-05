package com.example.coupon.service;

import com.example.coupon.entity.CouponTemplate;
import com.example.coupon.entity.UserCoupon;
import com.example.coupon.enums.CouponStatus;
import com.example.coupon.exception.CouponException;
import com.example.coupon.model.CouponTemplateModel;
import com.example.coupon.model.UserCouponModel;
import com.example.coupon.repository.CouponTemplateRepository;
import com.example.coupon.repository.UserCouponRepository;
import com.example.coupon.valueobject.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券应用服务
 */
@Service
public class CouponService {
    
    private final CouponTemplateRepository couponTemplateRepository;
    private final UserCouponRepository userCouponRepository;
    
    public CouponService(CouponTemplateRepository couponTemplateRepository,
                        UserCouponRepository userCouponRepository) {
        this.couponTemplateRepository = couponTemplateRepository;
        this.userCouponRepository = userCouponRepository;
    }
    
    @Transactional
    public String createCouponTemplate(String templateId, String name,
                                     com.example.coupon.enums.CouponType couponType,
                                     Money couponValue, Money minConsume,
                                     LocalDateTime startTime, LocalDateTime endTime,
                                     Integer totalQuantity, Integer userMaxQuantity) {
        CouponTemplate template = new CouponTemplate(templateId, name, couponType,
                                                   couponValue, minConsume, startTime,
                                                   endTime, totalQuantity, userMaxQuantity);
        CouponTemplateModel model = new CouponTemplateModel(templateId, name, couponType,
                                                          couponValue.getAmount(),
                                                          minConsume.getAmount(),
                                                          startTime, endTime,
                                                          totalQuantity, userMaxQuantity);
        couponTemplateRepository.save(model);
        return templateId;
    }
    
    @Transactional
    public String receiveCoupon(Long userId, String templateId) {
        CouponTemplateModel templateModel = couponTemplateRepository.findByTemplateId(templateId);
        if (templateModel == null) {
            throw new CouponException("\u4f18\u60e0\u5238\u6a21\u677f\u4e0d\u5b58\u5728: " + templateId);
        }
        CouponTemplate template = new CouponTemplate(
                templateModel.getTemplateId(),
                templateModel.getName(),
                templateModel.getCouponType(),
                new Money(templateModel.getCouponValue()),
                new Money(templateModel.getMinConsume()),
                templateModel.getStartTime(),
                templateModel.getEndTime(),
                templateModel.getTotalQuantity(),
                templateModel.getUserMaxQuantity()
        );
        List<UserCouponModel> userCoupons = userCouponRepository.findByUserId(userId);
        long userReceivedCount = userCoupons.stream()
                .filter(coupon -> templateId.equals(coupon.getTemplateId()))
                .count();
        if (!template.canReceive((int) userReceivedCount)) {
            throw new CouponException("\u4e0d\u7b26\u5408\u9886\u53d6\u6761\u4ef6");
        }
        String couponCode = "COUPON_" + System.currentTimeMillis() + "_" + userId;
        UserCouponModel userCouponModel = new UserCouponModel(
                couponCode, userId, templateId,
                templateModel.getCouponValue(), templateModel.getMinConsume(),
                templateModel.getStartTime(), templateModel.getEndTime()
        );
        userCouponRepository.save(userCouponModel);
        couponTemplateRepository.incrementIssuedQuantity(templateId, 1);
        return couponCode;
    }
    
    @Transactional(readOnly = true)
    public List<UserCoupon> queryUserCoupons(Long userId) {
        List<UserCouponModel> models = userCouponRepository.findByUserId(userId);
        return models.stream().map(model -> {
            UserCoupon userCoupon = new UserCoupon(
                    model.getUserId(),
                    Long.valueOf(model.getTemplateId().hashCode()),
                    new Money(model.getCouponValue()),
                    new Money(model.getMinConsume()),
                    model.getValidStartTime(),
                    model.getValidEndTime()
            );
            userCoupon.setId(model.getId());
            userCoupon.setStatus(model.getStatus());
            userCoupon.setCreateTime(model.getCreateTime());
            userCoupon.setUpdateTime(model.getUpdateTime());
            return userCoupon;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public boolean useCoupon(String couponCode, Long userId, Money orderAmount) {
        UserCouponModel model = userCouponRepository.findByCouponCode(couponCode);
        if (model == null) {
            throw new CouponException("\u4f18\u60e0\u5238\u4e0d\u5b58\u5728: " + couponCode);
        }
        if (!userId.equals(model.getUserId())) {
            throw new CouponException("\u4f18\u60e0\u5238\u4e0d\u5c5e\u4e8e\u5f53\u524d\u7528\u6237");
        }
        UserCoupon userCoupon = new UserCoupon(
                model.getUserId(),
                Long.valueOf(model.getTemplateId().hashCode()),
                new Money(model.getCouponValue()),
                new Money(model.getMinConsume()),
                model.getValidStartTime(),
                model.getValidEndTime()
        );
        userCoupon.setId(model.getId());
        userCoupon.setStatus(model.getStatus());
        userCoupon.use(orderAmount);
        userCouponRepository.updateStatus(couponCode, CouponStatus.USED.getCode());
        return true;
    }
}
