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
    public SupportTicket getTicketById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + id));
    }
    
    @Override
    @Transactional
    public SupportTicket updateTicket(Long id, SupportTicket ticket) {
        SupportTicket existingTicket = getTicketById(id);
        
        // Actualizar solo los campos que existen en la entidad
        existingTicket.setTicketNumber(ticket.getTicketNumber());
        existingTicket.setRequesterName(ticket.getRequesterName());
        existingTicket.setStatus(ticket.getStatus());
        existingTicket.setPriority(ticket.getPriority());
        existingTicket.setCategory(ticket.getCategory());
        existingTicket.setEstimatedCost(ticket.getEstimatedCost());
        existingTicket.setCurrency(ticket.getCurrency());
        existingTicket.setDueDate(ticket.getDueDate());
        
        return repository.save(existingTicket);
    }
    
    @Override
    @Transactional
    public void deleteTicket(Long id) {
        SupportTicket ticket = getTicketById(id);
        repository.delete(ticket);
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
