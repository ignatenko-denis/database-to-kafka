package com.sample.dao;

import com.sample.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface TradeRepository extends PagingAndSortingRepository<Trade, Long> {
    @Lock(PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "100")})
    @Query("SELECT r FROM Trade r WHERE r.sent = FALSE")
    Page<Trade> findNotSentScheduled(Pageable pageable);
}