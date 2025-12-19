package com.example.pharmacymanagementsystem.model;

import java.time.LocalDateTime;

import com.example.pharmacymanagementsystem.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    private String customerName;

    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    // Many sales belong to one medicine
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    @JsonIgnoreProperties({"company", "agents", "sales", "hibernateLazyInitializer", "handler"})
    private Medicine medicine;

    // Many sales processed by one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"sales", "password", "userProfile", "location", "hibernateLazyInitializer", "handler"})
    private User user;

    // Many sales belong to one customer (optional for walk-in customers)
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"sales", "hibernateLazyInitializer", "handler"})
    private Customer customer;
}
