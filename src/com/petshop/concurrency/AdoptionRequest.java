package com.petshop.concurrency;

import java.time.LocalDateTime;

/**
 * Encapsulates an asynchronous adoption request submitted to the concurrent order queue.
 */
public class AdoptionRequest {
    private static int seq = 1;

    private final int requestId;
    private final int petId;
    private final int customerId;
    private final double payment;
    private final LocalDateTime timestamp;

    public AdoptionRequest(int petId, int customerId, double payment) {
        this.requestId = seq++;
        this.petId = petId;
        this.customerId = customerId;
        this.payment = payment;
        this.timestamp = LocalDateTime.now();
    }

    public int getRequestId() { return requestId; }
    public int getPetId() { return petId; }
    public int getCustomerId() { return customerId; }
    public double getPayment() { return payment; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[REQ #%d] Pet #%d -> Customer #%d ($%.2f) @ %s",
                requestId, petId, customerId, payment, timestamp.toLocalTime());
    }
}
