package com.hotel.checkout.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.checkout.application.assembler.QuoteAssembler;
import com.hotel.checkout.application.command.QuoteCreateCommand;
import com.hotel.checkout.domain.model.QuoteSnapshot;
import com.hotel.checkout.domain.repository.QuoteSnapshotRepository;
import com.hotel.checkout.infrastructure.client.supply.SupplyClient;
import com.hotel.checkout.infrastructure.client.supply.response.SupplyAvailabilityResponse;
import com.hotel.checkout.interfaces.response.QuoteCreateResponse;
import com.hotel.checkout.interfaces.response.QuoteDetailResponse;
import com.hotel.common.exception.BizException;
import com.hotel.common.exception.ErrorCode;
import com.hotel.common.trace.TraceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteAppService {

    private final SupplyClient supplyClient;

    private final QuoteSnapshotRepository quoteSnapshotRepository;

    private final QuoteAssembler quoteAssembler;

    private final ObjectMapper objectMapper;

    @Value("${hotel.checkout.quote-expire-minutes:15}")
    private Integer quoteExpireMinutes;

    public QuoteCreateResponse createQuote(QuoteCreateCommand command) {
        validate(command);

        SupplyAvailabilityResponse availability = supplyClient.queryAvailability(
                command.ratePlanId(),
                command.checkInDate(),
                command.checkOutDate(),
                command.roomCount()
        );

        if (!Boolean.TRUE.equals(availability.available())) {
            throw new BizException(ErrorCode.INVENTORY_NOT_ENOUGH, "库存不足，无法生成报价");
        }

        String dailySnapshotJson = toJson(availability);

        QuoteSnapshot quoteSnapshot = new QuoteSnapshot(
                generateQuoteToken(),

                command.hotelId(),
                command.sellerId(),
                command.roomTypeId(),
                command.ratePlanId(),

                command.checkInDate(),
                command.checkOutDate(),
                command.roomCount(),

                availability.available(),
                availability.maxBookableRoomCount(),
                availability.totalAmount(),
                availability.currency(),

                "VALID",
                LocalDateTime.now().plusMinutes(quoteExpireMinutes),

                TraceContext.getTraceId(),
                dailySnapshotJson
        );

        quoteSnapshotRepository.save(quoteSnapshot);

        return quoteAssembler.toCreateResponse(quoteSnapshot);
    }

    private void validate(QuoteCreateCommand command) {
        if (!StringUtils.hasText(command.hotelId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "hotelId 不能为空");
        }
        if (!StringUtils.hasText(command.sellerId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "sellerId 不能为空");
        }
        if (!StringUtils.hasText(command.roomTypeId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomTypeId 不能为空");
        }
        if (!StringUtils.hasText(command.ratePlanId())) {
            throw new BizException(ErrorCode.PARAM_INVALID, "ratePlanId 不能为空");
        }
        if (command.checkInDate() == null || command.checkOutDate() == null) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }
        if (!command.checkInDate().isBefore(command.checkOutDate())) {
            throw new BizException(ErrorCode.DATE_RANGE_INVALID);
        }
        if (command.roomCount() == null || command.roomCount() <= 0) {
            throw new BizException(ErrorCode.PARAM_INVALID, "roomCount 必须大于 0");
        }
    }

    private String generateQuoteToken() {
        return "QT-" + UUID.randomUUID().toString().replace("-", "");
    }

    private String toJson(SupplyAvailabilityResponse availability) {
        try {
            return objectMapper.writeValueAsString(availability.dailyPrices());
        } catch (JsonProcessingException e) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "生成报价快照失败");
        }
    }
    public QuoteDetailResponse toDetailResponse(QuoteSnapshot quoteSnapshot) {
        return new QuoteDetailResponse(
                quoteSnapshot.getQuoteToken(),
                quoteSnapshot.getExpireAt(),

                quoteSnapshot.getHotelId(),
                quoteSnapshot.getSellerId(),
                quoteSnapshot.getRoomTypeId(),
                quoteSnapshot.getRatePlanId(),

                quoteSnapshot.getCheckInDate(),
                quoteSnapshot.getCheckOutDate(),
                quoteSnapshot.getRoomCount(),

                quoteSnapshot.getAvailable(),
                quoteSnapshot.getMaxBookableRoomCount(),
                quoteSnapshot.getTotalAmount(),
                quoteSnapshot.getCurrency(),

                quoteSnapshot.getQuoteStatus()
        );
    }
    public QuoteDetailResponse getQuote(String quoteToken) {
        if (!StringUtils.hasText(quoteToken)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "quoteToken 不能为空");
        }

        QuoteSnapshot quoteSnapshot = quoteSnapshotRepository.findByQuoteToken(quoteToken)
                .orElseThrow(() -> new BizException(ErrorCode.QUOTE_NOT_FOUND));

        return quoteAssembler.toDetailResponse(quoteSnapshot);
    }
}