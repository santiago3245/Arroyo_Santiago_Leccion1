package com.arroyo_santiago_leccion1.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arroyo_santiago_leccion1.exceptions.InvalidFilterException;
import com.arroyo_santiago_leccion1.models.entities.SupportTicket;
import com.arroyo_santiago_leccion1.models.enums.Currency;
import com.arroyo_santiago_leccion1.models.enums.TicketStatus;
import com.arroyo_santiago_leccion1.services.SupportTicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/support-tickets")
public class SupportTicketController {
    
    @Autowired
    private SupportTicketService service;
    
    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@Valid @RequestBody SupportTicket ticket) {
        SupportTicket createdTicket = service.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
    
    @GetMapping
    public ResponseEntity<Page<SupportTicket>> getTickets(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) BigDecimal minCost,
            @RequestParam(required = false) BigDecimal maxCost,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        
        // Validar status
        TicketStatus ticketStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidFilterException("Estado inválido: " + status + 
                    ". Valores permitidos: OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED");
            }
        }
        
        // Validar currency
        Currency curr = null;
        if (currency != null && !currency.isEmpty()) {
            try {
                curr = Currency.valueOf(currency.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidFilterException("Moneda inválida: " + currency + 
                    ". Valores permitidos: USD, EUR");
            }
        }
        
        // Validar minCost
        if (minCost != null && minCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidFilterException("El minCost debe ser mayor o igual a 0");
        }
        
        // Validar maxCost
        if (maxCost != null && maxCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidFilterException("El maxCost debe ser mayor o igual a 0");
        }
        
        // Parsear y validar fechas
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        
        if (from != null && !from.isEmpty()) {
            try {
                fromDateTime = LocalDateTime.parse(from);
            } catch (DateTimeParseException e) {
                throw new InvalidFilterException("Formato de fecha 'from' inválido. Use ISO-8601: yyyy-MM-dd'T'HH:mm:ss");
            }
        }
        
        if (to != null && !to.isEmpty()) {
            try {
                toDateTime = LocalDateTime.parse(to);
            } catch (DateTimeParseException e) {
                throw new InvalidFilterException("Formato de fecha 'to' inválido. Use ISO-8601: yyyy-MM-dd'T'HH:mm:ss");
            }
        }
        
        // Validar regla combinada: from <= to
        if (fromDateTime != null && toDateTime != null && fromDateTime.isAfter(toDateTime)) {
            throw new InvalidFilterException("La fecha 'from' debe ser menor o igual a 'to'");
        }
        
        // Configurar ordenamiento
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<SupportTicket> tickets = service.getTicketsWithFilters(
            q,
            ticketStatus,
            curr,
            minCost,
            maxCost,
            fromDateTime,
            toDateTime,
            pageable
        );
        
        return ResponseEntity.ok(tickets);
    }
}
