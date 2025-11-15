package domain;

import java.time.LocalDateTime;

public class Transaction{
    private String transactionId;
    private String accountNumber;
    private Double amount;
    private LocalDateTime timestamp;
    private String note;
    private Type type;

    // 
    public Transaction(String accountNumber, Double amount, String transactionId, String note, LocalDateTime timestamp, Type type) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.timestamp = timestamp;
        this.note = note;
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getNote() {
        return note;
    }
    public Type getType() {
        return type;
    }
}