package com.arroyo_santiago_leccion1.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arroyo_santiago_leccion1.models.entities.SupportTicket;
import com.arroyo_santiago_leccion1.models.enums.Currency;
import com.arroyo_santiago_leccion1.models.enums.TicketStatus;

@Repository
@Transactional
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    @Query("SELECT t FROM SupportTicket t WHERE " +
           "(:query IS NULL OR LOWER(t.ticketNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.requesterName) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:currency IS NULL OR t.currency = :currency) AND " +
           "(:minCost IS NULL OR t.estimatedCost >= :minCost) AND " +
           "(:maxCost IS NULL OR t.estimatedCost <= :maxCost) AND " +
           "(:from IS NULL OR t.createdAt >= :from) AND " +
           "(:to IS NULL OR t.createdAt <= :to)")
    Page<SupportTicket> findByFilters(
        @Param("query") String query,
        @Param("status") TicketStatus status,
        @Param("currency") Currency currency,
        @Param("minCost") BigDecimal minCost,
        @Param("maxCost") BigDecimal maxCost,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to,
        Pageable pageable
    );
}
