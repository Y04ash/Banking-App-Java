package app;
import java.util.Scanner;
import service.BankService;
import service.implementation.BankServiceConsole;
public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankServiceConsole();
        System.out.println("Welcome to Banking app!");
        boolean running = true;
        while(running){
            System.out.println("""
                    1) Open Account
                    2) Deposit Amount 
                    3) Withdraw Amount
                    4) Transfer Amount
                    5) Account Statement
                    6) List All Accounts
                    7) Search Account by Customer Name
                    0) Exit
                    """);
            System.out.println("Enter your choice: ");
            String choice = sc.nextLine().trim();
            System.out.println("Selected Option: " + choice);

            switch(choice){
                case "0" -> running =false;  
                case "1" -> openAccount(sc,bankService);
                case "2" -> deposit(sc,bankService);
                case "3" -> withdraw(sc,bankService);
                case "4" -> transfer(sc,bankService);
                case "5" -> statement(sc, bankService);
                case "6" -> listAccounts(sc, bankService);
                case "7" -> searchAccount(sc,bankService);
            }
        } 
    }

    private static void openAccount(Scanner sc, BankService bankService) {
        System.out.println("Customer Name: ");
        String name  = sc.nextLine().trim();
        System.out.println("Customer email: ");
        String email = sc.nextLine().trim();
        System.out.println("Account Type (SAVINGS/CURRENT): ");
        String type = sc.nextLine().trim();
        System.out.println("Initial Deposit Amount: ");
        String initialDepositStr = sc.nextLine().trim();
        if(initialDepositStr.isBlank())initialDepositStr="0";
        Double initialDeposit = Double.valueOf(initialDepositStr);
        String accountNumber  = bankService.openAccount(name,email,type);

        if(initialDeposit >0.0){
            bankService.deposit(accountNumber,initialDeposit, "Initial Deposit");
        }

        System.out.println("Account successfully created for " + name + " with type " + type + "Account Number: "+ accountNumber);
        System.out.println("");
    }
    
    private static void deposit(Scanner sc,BankService bankService) {
        System.out.println("Account Number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("Amount to Deposit: ");
        String amountStr = sc.nextLine().trim();
        Double amount = Double.valueOf(amountStr);
        bankService.deposit(accountNumber, amount,"Deposit via console");
        System.out.println("Amount Deposited Successfully");
        System.out.println("");
    }
    
    private static void withdraw(Scanner sc,BankService bankService) {
        System.out.println("Account Number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("Amount to Withdraw: ");
        String amountStr = sc.nextLine().trim();
        Double amount = Double.valueOf(amountStr);
        bankService.withdraw(accountNumber, amount,"Withdraw via console");
        System.out.println("Amount Withdrawn Successfully");
        System.out.println("");
    }
    
    private static void transfer(Scanner sc,BankService bankService) {
        System.out.println("From Account Number: ");
        String from = sc.nextLine().trim();

        System.out.println("To Account Number: ");
        String to = sc.nextLine().trim();

        System.out.println("Amount to Transfer: ");
        String amountStr = sc.nextLine().trim();
        Double amount = Double.valueOf(amountStr);
    
        bankService.transfer(from,to,amount,"Transfer via console");
        
        System.out.println("");
    }
    
    private static void statement(Scanner sc,BankService bankService) {
        System.out.println("Account Number: ");
        String accountNumber = sc.nextLine().trim();
        bankService.getStatement(accountNumber).forEach(t->{
            System.out.println(t.getAccountNumber() + " | " + t.getType() + " | " + t.getTimestamp() + " | " + t.getNote());
        });
    }
    
    private static void listAccounts(Scanner sc,BankService bankService) {
        bankService.listAccounts().forEach(a->{
            System.out.println(a.getAccountNumber()+ " | "  + a.getAccountType() + " | " + a.getBalance());
        });
    }
    
    private static void searchAccount(Scanner sc,BankService bankService) {
        System.out.println("Customer Name: ");
        String q  = sc.nextLine().trim();
        bankService.searchAccountsByCustomerName(q)
        .forEach(account -> 
            System.out.println(account.getAccountNumber()+ " | "  + account.getAccountType() + " | " + account.getBalance())
        );
    }

}
