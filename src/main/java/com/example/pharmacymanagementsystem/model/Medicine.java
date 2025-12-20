package com.example.pharmacymanagementsystem.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicines", indexes = {
    @Index(name = "idx_medicine_name", columnList = "name"),
    @Index(name = "idx_medicine_category", columnList = "category"),
    @Index(name = "idx_medicine_company", columnList = "company_id"),
    @Index(name = "idx_medicine_expiry", columnList = "expiryDate"),
    @Index(name = "idx_medicine_expired", columnList = "isExpired")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    private Double purchasePrice;

    @Column(nullable = false)
    private Double sellingPrice;

    @Column(nullable = false)
    private Integer quantity;

    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    private LocalDate entryDate;

    private String batchNumber;

    @Builder.Default
    private Boolean isExpired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"medicines", "hibernateLazyInitializer", "handler"})
    private Company company;

    @javax.persistence.PrePersist
    protected void onCreate() {
        if (entryDate == null) {
            entryDate = LocalDate.now();
        }
        if (purchasePrice == null) {
            purchasePrice = sellingPrice != null ? sellingPrice * 0.7 : 0.0;
        }
        if (manufactureDate == null) {
            manufactureDate = LocalDate.now();
        }
        if (isExpired == null) {
            isExpired = false;
        }
    }

    @ManyToMany
    @JoinTable(
            name = "medicine_agent",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    @JsonIgnore  // Keep this - we don't want agents in request/response
    private List<Agent> agents;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    @JsonIgnore  // Keep this - we don't want sales in request/response
    private List<Sale> sales;
}
