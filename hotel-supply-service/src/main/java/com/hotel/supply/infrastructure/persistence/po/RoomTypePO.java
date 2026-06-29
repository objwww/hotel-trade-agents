package com.hotel.supply.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("room_type")
public class RoomTypePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String roomTypeId;

    private String hotelId;

    private String roomTypeName;

    private String bedType;

    private Integer maxGuests;

    private String area;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}