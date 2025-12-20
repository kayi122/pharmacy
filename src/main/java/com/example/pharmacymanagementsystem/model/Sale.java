package com.example.pharmacymanagementsystem.model;

import java.time.LocalDateTime;

import com.example.pharmacymanagementsystem.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    private String deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    @JsonIgnoreProperties({"company", "agents", "sales", "hibernateLazyInitializer", "handler"})
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"sales", "password", "userProfile", "location", "hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"sales", "hibernateLazyInitializer", "handler"})
    private Customer customer;
}
