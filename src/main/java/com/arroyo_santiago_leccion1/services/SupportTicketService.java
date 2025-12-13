package com.arroyo_santiago_leccion1.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arroyo_santiago_leccion1.models.entities.SupportTicket;
import com.arroyo_santiago_leccion1.models.enums.Currency;
import com.arroyo_santiago_leccion1.models.enums.TicketStatus;

public interface SupportTicketService {
    
    SupportTicket createTicket(SupportTicket ticket);
    
    Page<SupportTicket> getTicketsWithFilters(
        String query,
        TicketStatus status,
        Currency currency,
        BigDecimal minCost,
        BigDecimal maxCost,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    );
}
