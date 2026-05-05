package com.example.coupon.repository;

import com.example.coupon.model.CouponTemplateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券模板数据访问接口
 */
@Mapper
public interface CouponTemplateRepository {
    CouponTemplateModel findByTemplateId(@Param("templateId") String templateId);
    int save(CouponTemplateModel template);
    int incrementIssuedQuantity(@Param("templateId") String templateId,
                              @Param("increment") int increment);
}
