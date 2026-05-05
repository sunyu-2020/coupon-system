package com.example.coupon.repository;

import com.example.coupon.model.UserCouponModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券数据访问接口
 */
@Mapper
public interface UserCouponRepository {
    List<UserCouponModel> findByUserId(@Param("userId") Long userId);
    UserCouponModel findByCouponCode(@Param("couponCode") String couponCode);
    int save(UserCouponModel userCoupon);
    int updateStatus(@Param("couponCode") String couponCode,
                     @Param("status") String status);
}
