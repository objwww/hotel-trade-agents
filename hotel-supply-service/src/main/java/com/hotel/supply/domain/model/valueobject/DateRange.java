package com.hotel.supply.domain.model.valueobject;

import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DateRange {

    private final LocalDate checkInDate;

    private final LocalDate checkOutDate;

    private DateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        if (!checkInDate.isBefore(checkOutDate)) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public static DateRange of(LocalDate checkInDate, LocalDate checkOutDate) {
        return new DateRange(checkInDate, checkOutDate);
    }

    public List<LocalDate> stayDates() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = checkInDate;

        while (current.isBefore(checkOutDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        return dates;
    }
}