package com.arroyo_santiago_leccion1.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.arroyo_santiago_leccion1.models.enums.Currency;
import com.arroyo_santiago_leccion1.models.enums.Priority;
import com.arroyo_santiago_leccion1.models.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El número de ticket es obligatorio")
    @Pattern(regexp = "^ST-\\d{4}-\\d{6}$", message = "El formato del ticket debe ser ST-YYYY-NNNNNN (ej: ST-2025-000145)")
    @Column(unique = true, nullable = false)
    private String ticketNumber;
    
    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Column(nullable = false)
    private String requesterName;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;
    
    @NotNull(message = "La prioridad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false)
    private String category;
    
    @NotNull(message = "El costo estimado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo estimado debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedCost;
    
    @NotNull(message = "La moneda es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
