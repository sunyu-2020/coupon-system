# 优惠券系统

这是一个基于Spring Boot的优惠券管理系统，支持优惠券模板创建、优惠券发放、优惠券核销等功能。

## 技术栈

- Java 8+
- Spring Boot 2.7.18
- MyBatis
- H2 Database
- Lombok
- MapStruct

## 项目结构

```
src/main/java/com/example/coupon/
├── controller/       # REST API控制器
├── dto/              # 数据传输对象
├── entity/           # 领域实体
├── enums/            # 枚举类
├── exception/        # 异常类
├── model/            # 数据模型
├── repository/       # 数据访问层
├── service/          # 应用服务层
├── valueobject/      # 值对象
├── config/           # 配置类
└── CouponApplication.java  # 主应用类
```

## 构建项目

```bash
# 编译项目
./gradlew clean compileJava

# 运行测试
./gradlew test

# 启动应用
./gradlew bootRun
```

## API接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/coupon/templates | 创建优惠券模板 |
| POST | /api/coupon/users/{userId}/receive | 领取优惠券 |
| GET  | /api/coupon/users/{userId}/coupons | 查询用户优惠券 |
| POST | /api/coupon/users/{userId}/use | 核销优惠券 |
