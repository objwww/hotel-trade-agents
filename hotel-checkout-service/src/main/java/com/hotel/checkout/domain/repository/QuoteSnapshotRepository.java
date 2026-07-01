package com.hotel.checkout.domain.repository;

import com.hotel.checkout.domain.model.QuoteSnapshot;

import java.util.Optional;

public interface QuoteSnapshotRepository {

    void save(QuoteSnapshot quoteSnapshot);

    Optional<QuoteSnapshot> findByQuoteToken(String quoteToken);
}