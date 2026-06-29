INSERT INTO hotel (
    hotel_id,
    hotel_name,
    country,
    city,
    address,
    star_level,
    status
) VALUES (
             'H1001',
             '首尔明洞天空酒店',
             '韩国',
             '首尔',
             '明洞中心街 12 号',
             4,
             'ONLINE'
         );

INSERT INTO seller (
    seller_id,
    seller_name,
    seller_type,
    confirm_type,
    pay_type,
    status
) VALUES (
             'S1001',
             '平台自营卖家',
             'SELF',
             'SUPPLIER',
             'PREPAY',
             'ONLINE'
         );

INSERT INTO room_type (
    room_type_id,
    hotel_id,
    room_type_name,
    bed_type,
    max_guests,
    area,
    status
) VALUES (
             'R1001',
             'H1001',
             '高级大床房',
             '1张大床',
             2,
             '36㎡',
             'ONLINE'
         );

INSERT INTO rate_plan (
    rate_plan_id,
    hotel_id,
    seller_id,
    room_type_id,
    rate_plan_name,
    breakfast_type,
    cancel_policy_id,
    guest_requirement_id,
    pay_type,
    status
) VALUES (
             'RP1001',
             'H1001',
             'S1001',
             'R1001',
             '双早限时取消预付套餐',
             'DOUBLE',
             'CP1001',
             'GR1001',
             'PREPAY',
             'ONLINE'
         );

INSERT INTO daily_rate (
    rate_plan_id,
    biz_date,
    sale_price,
    currency,
    status
) VALUES
      ('RP1001', '2026-07-01', 1280.00, 'CNY', 'ONLINE'),
      ('RP1001', '2026-07-02', 1480.00, 'CNY', 'ONLINE');

INSERT INTO daily_inventory (
    rate_plan_id,
    biz_date,
    total_count,
    frozen_count,
    sold_count,
    safety_stock,
    version
) VALUES
      ('RP1001', '2026-07-01', 10, 0, 2, 1, 0),
      ('RP1001', '2026-07-02', 10, 0, 3, 1, 0);