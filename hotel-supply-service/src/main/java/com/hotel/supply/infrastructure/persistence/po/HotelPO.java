package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hotel")
public class HotelPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String hotelId;

    private String hotelName;

    private String country;

    private String city;

    private String address;

    private Integer starLevel;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}