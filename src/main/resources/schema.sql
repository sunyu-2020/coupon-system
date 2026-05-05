-- 优惠券模板表
CREATE TABLE IF NOT EXISTS coupon_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    coupon_type VARCHAR(32) NOT NULL,
    coupon_value BIGINT NOT NULL,
    min_consume BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_quantity INT NOT NULL,
    user_max_quantity INT NOT NULL,
    issued_quantity INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coupon_code VARCHAR(128) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    template_id VARCHAR(64) NOT NULL,
    coupon_value BIGINT NOT NULL,
    min_consume BIGINT NOT NULL,
    valid_start_time TIMESTAMP NOT NULL,
    valid_end_time TIMESTAMP NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'NEW',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
