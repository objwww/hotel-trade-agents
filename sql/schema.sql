DROP TABLE IF EXISTS guide_offer;
DROP TABLE IF EXISTS daily_inventory;
DROP TABLE IF EXISTS daily_rate;
DROP TABLE IF EXISTS rate_plan;
DROP TABLE IF EXISTS room_type;
DROP TABLE IF EXISTS seller;
DROP TABLE IF EXISTS hotel;

CREATE TABLE hotel (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       hotel_id VARCHAR(64) NOT NULL,
                       hotel_name VARCHAR(128) NOT NULL,
                       country VARCHAR(64) NOT NULL,
                       city VARCHAR(64) NOT NULL,
                       address VARCHAR(255),
                       star_level INT,
                       status VARCHAR(32) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       UNIQUE KEY uk_hotel_id (hotel_id),
                       KEY idx_city (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE seller (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        seller_id VARCHAR(64) NOT NULL,
                        seller_name VARCHAR(128) NOT NULL,
                        seller_type VARCHAR(32) NOT NULL,
                        confirm_type VARCHAR(32) NOT NULL,
                        pay_type VARCHAR(32) NOT NULL,
                        status VARCHAR(32) NOT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        UNIQUE KEY uk_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE room_type (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           room_type_id VARCHAR(64) NOT NULL,
                           hotel_id VARCHAR(64) NOT NULL,
                           room_type_name VARCHAR(128) NOT NULL,
                           bed_type VARCHAR(64),
                           max_guests INT,
                           area VARCHAR(64),
                           status VARCHAR(32) NOT NULL,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           UNIQUE KEY uk_room_type_id (room_type_id),
                           KEY idx_hotel_id (hotel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rate_plan (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           rate_plan_id VARCHAR(64) NOT NULL,
                           hotel_id VARCHAR(64) NOT NULL,
                           seller_id VARCHAR(64) NOT NULL,
                           room_type_id VARCHAR(64) NOT NULL,
                           rate_plan_name VARCHAR(128) NOT NULL,
                           breakfast_type VARCHAR(32) NOT NULL,
                           cancel_policy_id VARCHAR(64),
                           guest_requirement_id VARCHAR(64),
                           pay_type VARCHAR(32) NOT NULL,
                           status VARCHAR(32) NOT NULL,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           UNIQUE KEY uk_rate_plan_id (rate_plan_id),
                           KEY idx_hotel_id (hotel_id),
                           KEY idx_room_type_id (room_type_id),
                           KEY idx_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE daily_rate (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            rate_plan_id VARCHAR(64) NOT NULL,
                            biz_date DATE NOT NULL,
                            sale_price DECIMAL(10, 2) NOT NULL,
                            currency VARCHAR(16) NOT NULL,
                            status VARCHAR(32) NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            UNIQUE KEY uk_rate_plan_date (rate_plan_id, biz_date),
                            KEY idx_biz_date (biz_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE daily_inventory (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 rate_plan_id VARCHAR(64) NOT NULL,
                                 biz_date DATE NOT NULL,
                                 total_count INT NOT NULL,
                                 frozen_count INT NOT NULL DEFAULT 0,
                                 sold_count INT NOT NULL DEFAULT 0,
                                 safety_stock INT NOT NULL DEFAULT 0,
                                 version INT NOT NULL DEFAULT 0,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 UNIQUE KEY uk_rate_plan_date (rate_plan_id, biz_date),
                                 KEY idx_biz_date (biz_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE guide_offer (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             guide_offer_id VARCHAR(64) NOT NULL,
                             hotel_id VARCHAR(64) NOT NULL,
                             seller_id VARCHAR(64) NOT NULL,
                             room_type_id VARCHAR(64) NOT NULL,
                             rate_plan_id VARCHAR(64) NOT NULL,
                             check_in_date DATE NOT NULL,
                             check_out_date DATE NOT NULL,
                             room_count INT NOT NULL,
                             display_amount DECIMAL(10, 2) NOT NULL,
                             currency VARCHAR(16) NOT NULL,
                             inventory_tag VARCHAR(64),
                             breakfast_tag VARCHAR(64),
                             cancel_tag VARCHAR(64),
                             data_freshness VARCHAR(32),
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             UNIQUE KEY uk_guide_offer_id (guide_offer_id),
                             KEY idx_hotel_id (hotel_id),
                             KEY idx_rate_plan_id (rate_plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;