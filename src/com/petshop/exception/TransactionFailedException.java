package com.petshop.exception;

/**
 * Thrown when an atomic transaction fails validation or encounters an I/O error,
 * initiating a complete rollback (Syllabus Unit 4: Transaction Management).
 */
public class TransactionFailedException extends PetShopException {
    private final String transactionId;

    public TransactionFailedException(String transactionId, String reason) {
        super(String.format("Transaction [%s] aborted and rolled back: %s", transactionId, reason), "ERR_TX_FAILED");
        this.transactionId = transactionId;
    }

    public TransactionFailedException(String transactionId, String reason, Throwable cause) {
        super(String.format("Transaction [%s] aborted and rolled back: %s", transactionId, reason), "ERR_TX_FAILED", cause);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
