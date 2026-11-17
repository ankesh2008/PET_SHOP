package com.petshop.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an adoption transaction record.
 */
public class Order {
    private static int nextOrderId = 5001;

    private final int orderId;
    private final int petId;
    private final int customerId;
    private final LocalDate orderDate;
    private final double amountPaid;
    private final String status; // COMPLETED, ROLLED_BACK

    public Order(int orderId, int petId, int customerId, LocalDate orderDate, double amountPaid, String status) {
        this.orderId = (orderId > 0) ? orderId : nextOrderId++;
        this.petId = petId;
        this.customerId = customerId;
        this.orderDate = (orderDate != null) ? orderDate : LocalDate.now();
        this.amountPaid = amountPaid;
        this.status = (status != null) ? status : "COMPLETED";
    }

    public Order(int petId, int customerId, double amountPaid) {
        this(nextOrderId++, petId, customerId, LocalDate.now(), amountPaid, "COMPLETED");
    }

    public int getOrderId() { return orderId; }
    public int getPetId() { return petId; }
    public int getCustomerId() { return customerId; }
    public LocalDate getOrderDate() { return orderDate; }
    public double getAmountPaid() { return amountPaid; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("[ORDER #%d] Pet #%d -> Customer #%d | Date: %s | Paid: $%7.2f | Status: %s",
                orderId, petId, customerId, orderDate, amountPaid, status);
    }
}
