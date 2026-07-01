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


CREATE TABLE IF NOT EXISTS quote_snapshot (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              quote_token VARCHAR(128) NOT NULL,

                                              hotel_id VARCHAR(64) NOT NULL,
                                              seller_id VARCHAR(64) NOT NULL,
                                              room_type_id VARCHAR(64) NOT NULL,
                                              rate_plan_id VARCHAR(64) NOT NULL,

                                              check_in_date DATE NOT NULL,
                                              check_out_date DATE NOT NULL,
                                              room_count INT NOT NULL,

                                              available TINYINT NOT NULL,
                                              max_bookable_room_count INT NOT NULL,
                                              total_amount DECIMAL(10, 2) NOT NULL,
                                              currency VARCHAR(16) NOT NULL,

                                              quote_status VARCHAR(32) NOT NULL,
                                              expire_at DATETIME NOT NULL,

                                              source_trace_id VARCHAR(128),
                                              daily_snapshot_json TEXT,

                                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                              UNIQUE KEY uk_quote_token (quote_token),
                                              KEY idx_rate_plan_id (rate_plan_id),
                                              KEY idx_expire_at (expire_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





# freeze_token：冻结请求唯一号，用来防止重复冻结
# order_no：订单号
# rate_plan_id：冻结哪个售卖计划
# check_in_date/check_out_date：冻结哪个入住区间
# room_count：冻结几间
# freeze_status：FROZEN / CANCELLED / CONFIRMED
# source_trace_id：链路追踪用
CREATE TABLE IF NOT EXISTS inventory_freeze_record (
                                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                       freeze_token VARCHAR(128) NOT NULL,
                                                       order_no VARCHAR(128) NOT NULL,

                                                       rate_plan_id VARCHAR(64) NOT NULL,
                                                       check_in_date DATE NOT NULL,
                                                       check_out_date DATE NOT NULL,
                                                       room_count INT NOT NULL,

                                                       freeze_status VARCHAR(32) NOT NULL,
                                                       source_trace_id VARCHAR(128),

                                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                                       UNIQUE KEY uk_freeze_token (freeze_token),
                                                       KEY idx_order_no (order_no),
                                                       KEY idx_rate_plan_id (rate_plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


##order_no：系统订单号
# quote_token：订单基于哪一次报价创建
# client_order_no：客户端幂等号，防止用户重复点击下单
# hotel_id/seller_id/room_type_id/rate_plan_id：订单锁定的商品信息
# check_in_date/check_out_date/room_count：入住信息
# order_amount/currency：订单金额
# order_status：PENDING_PAYMENT / PAID / CANCELLED
# freeze_token：库存冻结凭证
# contact_name/contact_phone：联系人信息
# source_trace_id：链路追踪
#########################
CREATE TABLE IF NOT EXISTS sales_order (
                                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                           order_no VARCHAR(128) NOT NULL,
                                           quote_token VARCHAR(128) NOT NULL,
                                           client_order_no VARCHAR(128) NOT NULL,

                                           hotel_id VARCHAR(64) NOT NULL,
                                           seller_id VARCHAR(64) NOT NULL,
                                           room_type_id VARCHAR(64) NOT NULL,
                                           rate_plan_id VARCHAR(64) NOT NULL,

                                           check_in_date DATE NOT NULL,
                                           check_out_date DATE NOT NULL,
                                           room_count INT NOT NULL,

                                           order_amount DECIMAL(10, 2) NOT NULL,
                                           currency VARCHAR(16) NOT NULL,

                                           order_status VARCHAR(32) NOT NULL,
                                           freeze_token VARCHAR(128) NOT NULL,

                                           contact_name VARCHAR(64),
                                           contact_phone VARCHAR(32),

                                           source_trace_id VARCHAR(128),

                                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                           UNIQUE KEY uk_order_no (order_no),
                                           UNIQUE KEY uk_client_order_no (client_order_no),
                                           KEY idx_quote_token (quote_token),
                                           KEY idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE sales_order
    ADD COLUMN cancel_reason VARCHAR(255) NULL AFTER source_trace_id,
    ADD COLUMN cancel_at DATETIME NULL AFTER cancel_reason;

ALTER TABLE sales_order
    ADD COLUMN payment_no VARCHAR(128) NULL AFTER order_status,
    ADD COLUMN paid_at DATETIME NULL AFTER payment_no;

DESC sales_order;


CREATE TABLE IF NOT EXISTS fulfillment_order (
                                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,

                                                 fulfillment_no VARCHAR(128) NOT NULL,
                                                 order_no VARCHAR(128) NOT NULL,

                                                 hotel_id VARCHAR(64) NOT NULL,
                                                 seller_id VARCHAR(64) NOT NULL,
                                                 room_type_id VARCHAR(64) NOT NULL,
                                                 rate_plan_id VARCHAR(64) NOT NULL,

                                                 check_in_date DATE NOT NULL,
                                                 check_out_date DATE NOT NULL,
                                                 room_count INT NOT NULL,

                                                 contact_name VARCHAR(64),
                                                 contact_phone VARCHAR(32),

                                                 fulfillment_status VARCHAR(32) NOT NULL,
                                                 supplier_confirm_no VARCHAR(128),

                                                 source_trace_id VARCHAR(128),
                                                 supplier_response_json TEXT,

                                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                 updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                                 UNIQUE KEY uk_fulfillment_no (fulfillment_no),
                                                 UNIQUE KEY uk_order_no (order_no),
                                                 KEY idx_fulfillment_status (fulfillment_status),
                                                 KEY idx_supplier_confirm_no (supplier_confirm_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE sales_order
    ADD COLUMN fulfillment_no VARCHAR(128) NULL AFTER freeze_token,
    ADD COLUMN supplier_confirm_no VARCHAR(128) NULL AFTER fulfillment_no,
    ADD COLUMN confirmed_at DATETIME NULL AFTER supplier_confirm_no;

CREATE TABLE IF NOT EXISTS order_status_log (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,

                                                order_no VARCHAR(128) NOT NULL,

                                                event_type VARCHAR(64) NOT NULL,
                                                from_status VARCHAR(32),
                                                to_status VARCHAR(32),

                                                event_desc VARCHAR(255),

                                                source_trace_id VARCHAR(128),

                                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                                                KEY idx_order_no (order_no),
                                                KEY idx_event_type (event_type),
                                                KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS trade_operation_log (
                                                   id BIGINT PRIMARY KEY AUTO_INCREMENT,

                                                   operation_type VARCHAR(64) NOT NULL,

                                                   order_no VARCHAR(128),
                                                   client_order_no VARCHAR(128),

                                                   trace_id VARCHAR(128),

                                                   success TINYINT NOT NULL,
                                                   error_code VARCHAR(64),
                                                   error_message VARCHAR(512),

                                                   cost_ms BIGINT NOT NULL,

                                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                                                   KEY idx_operation_type (operation_type),
                                                   KEY idx_order_no (order_no),
                                                   KEY idx_client_order_no (client_order_no),
                                                   KEY idx_trace_id (trace_id),
                                                   KEY idx_success (success),
                                                   KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;