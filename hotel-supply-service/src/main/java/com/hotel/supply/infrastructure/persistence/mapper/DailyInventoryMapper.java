package com.hotel.supply.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.supply.infrastructure.persistence.po.DailyInventoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.time.LocalDate;

@Mapper
public interface DailyInventoryMapper extends BaseMapper<DailyInventoryPO> {
//    关键是这一句：
//
//    AND total_count - frozen_count - sold_count - safety_stock >= #{roomCount}
//
//    它保证只有库存足够时才会更新成功。


    @Update("""
            UPDATE daily_inventory
            SET frozen_count = frozen_count + #{roomCount},
                version = version + 1
            WHERE rate_plan_id = #{ratePlanId}
              AND biz_date = #{bizDate}
              AND total_count - frozen_count - sold_count - safety_stock >= #{roomCount}
            """)
    int freezeInventory(
            @Param("ratePlanId") String ratePlanId,
            @Param("bizDate") LocalDate bizDate,
            @Param("roomCount") Integer roomCount
    );


    @Update("""
        UPDATE daily_inventory
        SET frozen_count = frozen_count - #{roomCount},
            version = version + 1
        WHERE rate_plan_id = #{ratePlanId}
          AND biz_date = #{bizDate}
          AND frozen_count >= #{roomCount}
        """)
    int releaseInventory(
            @Param("ratePlanId") String ratePlanId,
            @Param("bizDate") LocalDate bizDate,
            @Param("roomCount") Integer roomCount
    );

    @Update("""
        UPDATE daily_inventory
        SET frozen_count = frozen_count - #{roomCount},
            sold_count = sold_count + #{roomCount},
            version = version + 1
        WHERE rate_plan_id = #{ratePlanId}
          AND biz_date = #{bizDate}
          AND frozen_count >= #{roomCount}
        """)
    int confirmInventory(
            @Param("ratePlanId") String ratePlanId,
            @Param("bizDate") LocalDate bizDate,
            @Param("roomCount") Integer roomCount
    );
//    只有 frozen_count 足够时，才能确认售出。
//    确认售出时，不是凭空增加 sold_count，而是从 frozen_count 转过去。
}