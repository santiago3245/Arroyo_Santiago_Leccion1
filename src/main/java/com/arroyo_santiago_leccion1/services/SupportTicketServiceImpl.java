package com.arroyo_santiago_leccion1.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arroyo_santiago_leccion1.models.entities.SupportTicket;
import com.arroyo_santiago_leccion1.models.enums.Currency;
import com.arroyo_santiago_leccion1.models.enums.TicketStatus;
import com.arroyo_santiago_leccion1.repositories.SupportTicketRepository;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {
    
    @Autowired
    private SupportTicketRepository repository;
    
    @Override
    @Transactional
    public SupportTicket createTicket(SupportTicket ticket) {
        return repository.save(ticket);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupportTicket> getTicketsWithFilters(
            String query,
            TicketStatus status,
            Currency currency,
            BigDecimal minCost,
            BigDecimal maxCost,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable) {
        
        // Normalizar query: si está vacío, convertirlo a null
        String normalizedQuery = (query != null && query.trim().isEmpty()) ? null : query;
        
        return repository.findByFilters(
            normalizedQuery,
            status,
            currency,
            minCost,
            maxCost,
            from,
            to,
            pageable
        );
    }
}
