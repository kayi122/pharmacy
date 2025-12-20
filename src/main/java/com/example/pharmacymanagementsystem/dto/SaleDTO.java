package com.example.pharmacymanagementsystem.dto;

import java.time.LocalDateTime;

import com.example.pharmacymanagementsystem.model.Sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {

    private Long id;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime saleDate;
    private String customerName;
    private String customerPhone;
    private String paymentMethod;
    private String deliveryMethod;

    // Medicine information
    private Long medicineId;
    private String medicineName;
    private String medicineCategory;
    private Double medicineSellingPrice;

    // User information
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    // Calculated fields
    private Double unitPrice;
    private Double profit;

    public SaleDTO(Sale sale) {
        this.id = sale.getId();
        this.quantity = sale.getQuantity();
        this.totalPrice = sale.getTotalPrice();
        this.saleDate = sale.getSaleDate();
        this.customerName = sale.getCustomerName();
        this.customerPhone = sale.getCustomerPhone();
        this.paymentMethod = sale.getPaymentMethod() != null ? sale.getPaymentMethod().toString() : null;
        this.deliveryMethod = sale.getDeliveryMethod();

        // Medicine info
        if (sale.getMedicine() != null) {
            this.medicineId = sale.getMedicine().getId();
            this.medicineName = sale.getMedicine().getName();
            this.medicineCategory = sale.getMedicine().getCategory();
            this.medicineSellingPrice = sale.getMedicine().getSellingPrice();

            // Calculated fields
            this.unitPrice = this.totalPrice / this.quantity;
            this.profit = (sale.getMedicine().getSellingPrice() - sale.getMedicine().getPurchasePrice()) * this.quantity;
        }

        // User info
        if (sale.getUser() != null) {
            this.userId = sale.getUser().getId();
            this.userFirstName = sale.getUser().getFirstName();
            this.userLastName = sale.getUser().getLastName();
            this.userEmail = sale.getUser().getEmail();
        }
    }
}
