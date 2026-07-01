package com.hotel.checkout.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotel.checkout.domain.model.QuoteSnapshot;
import com.hotel.checkout.domain.repository.QuoteSnapshotRepository;
import com.hotel.checkout.infrastructure.persistence.mapper.QuoteSnapshotMapper;
import com.hotel.checkout.infrastructure.persistence.po.QuoteSnapshotPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuoteSnapshotRepositoryImpl implements QuoteSnapshotRepository {

    private final QuoteSnapshotMapper quoteSnapshotMapper;

    @Override
    public void save(QuoteSnapshot quoteSnapshot) {
        quoteSnapshotMapper.insert(toPO(quoteSnapshot));
    }

    @Override
    public Optional<QuoteSnapshot> findByQuoteToken(String quoteToken) {
        QuoteSnapshotPO po = quoteSnapshotMapper.selectOne(
                Wrappers.<QuoteSnapshotPO>lambdaQuery()
                        .eq(QuoteSnapshotPO::getQuoteToken, quoteToken)
        );

        return Optional.ofNullable(po).map(this::toDomain);
    }

    private QuoteSnapshotPO toPO(QuoteSnapshot quoteSnapshot) {
        QuoteSnapshotPO po = new QuoteSnapshotPO();

        po.setQuoteToken(quoteSnapshot.getQuoteToken());
        po.setHotelId(quoteSnapshot.getHotelId());
        po.setSellerId(quoteSnapshot.getSellerId());
        po.setRoomTypeId(quoteSnapshot.getRoomTypeId());
        po.setRatePlanId(quoteSnapshot.getRatePlanId());

        po.setCheckInDate(quoteSnapshot.getCheckInDate());
        po.setCheckOutDate(quoteSnapshot.getCheckOutDate());
        po.setRoomCount(quoteSnapshot.getRoomCount());

        po.setAvailable(quoteSnapshot.getAvailable());
        po.setMaxBookableRoomCount(quoteSnapshot.getMaxBookableRoomCount());
        po.setTotalAmount(quoteSnapshot.getTotalAmount());
        po.setCurrency(quoteSnapshot.getCurrency());

        po.setQuoteStatus(quoteSnapshot.getQuoteStatus());
        po.setExpireAt(quoteSnapshot.getExpireAt());
        po.setSourceTraceId(quoteSnapshot.getSourceTraceId());
        po.setDailySnapshotJson(quoteSnapshot.getDailySnapshotJson());

        return po;
    }

    private QuoteSnapshot toDomain(QuoteSnapshotPO po) {
        return new QuoteSnapshot(
                po.getQuoteToken(),
                po.getHotelId(),
                po.getSellerId(),
                po.getRoomTypeId(),
                po.getRatePlanId(),
                po.getCheckInDate(),
                po.getCheckOutDate(),
                po.getRoomCount(),
                po.getAvailable(),
                po.getMaxBookableRoomCount(),
                po.getTotalAmount(),
                po.getCurrency(),
                po.getQuoteStatus(),
                po.getExpireAt(),
                po.getSourceTraceId(),
                po.getDailySnapshotJson()
        );
    }
}