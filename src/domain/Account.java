package domain;

public class Account {
    private String accountNumber;
    private String customerId;
    private Double balance;
    private String accountType;

    public Account(String accountNumber, String customerId, Double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType(){
        return accountType;
    }
    public Double getBalance(){
        return balance;
    }
    public void setBalance(Double balance){
        this.balance = balance;
    }
    public String getCustomerId(){
        return customerId;
    }
    
}
