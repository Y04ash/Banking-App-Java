package service.implementation;
import java.util.UUID;

import domain.Account;
import domain.Transaction;
import domain.Customer;
import domain.Type;
import exceptions.AccoountNotFoundExceptions;
import exceptions.InsufficientFundsExceptions;
import exceptions.ValidationExceptions;
import service.BankService;
import util.Validation;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import repository.CustomerRepository;
// now this is console (terminal) based implementation of BankService , infututure we can have web based implementation also that "implements" BankService hence we have created interface of  BankService and not class of BankService
public class BankServiceConsole implements BankService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final Validation<String> validateName = name->{
        if(name == null || name.trim().isEmpty()){
            throw new ValidationExceptions("Name cannot be empty");
        }
    };
    private final Validation<String> validateEmail = (value)->{
        if(value == null || value.trim().isEmpty() || !value.contains("@")){
            throw new ValidationExceptions("Email cannot be empty");
        }
    };
    private final Validation<String> validateType = (value)->{
         if(value == null || value.trim().isEmpty() || !value.trim().equalsIgnoreCase("SAVINGS") && !value.trim().equalsIgnoreCase("CURRENT")){
            throw new ValidationExceptions("Account type must be SAVINGS or CURRENT");
        }
    };
    private final Validation<Double> validateAmount = amount -> {
        if(amount == null || amount<0){
            throw new ValidationExceptions("Please entr valid amount");
        }
    };
    @Override
    public String openAccount(String name, String email, String accountType) {
        
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);
        
        // create customer Id
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer( customerId, name, email);

        customerRepository.save(customer);

        // create Account Numbe
        String accountNumber = getAccountNumber();

        // create account
        Account a = new Account(accountNumber, customerId, (double) 0 , accountType );

        // save account
        accountRepository.save(a);
      
        return accountNumber;

    }

    @Override
    public List<Account> listAccounts(){
        return accountRepository
        .findAll()
        .stream()
        .sorted(Comparator.comparing(Account::getAccountNumber))
        .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note){
        validateAmount.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber)
        .orElseThrow(()-> new AccoountNotFoundExceptions("Account not Found: "+ accountNumber));
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(account.getAccountNumber(),amount,UUID.randomUUID().toString(),note, LocalDateTime.now(),Type.DEPOSIT);
        transactionRepository.add(transaction);
        
    }
    
    @Override
    public void withdraw(String accountNumber, Double amount, String note){
        validateAmount.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber)
        .orElseThrow(()-> new AccoountNotFoundExceptions("Account not Found: "+ accountNumber));
        if(account.getBalance().compareTo(amount) < 0){
            throw new InsufficientFundsExceptions("Insufficient Balance in account: "+ accountNumber);
        }
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(account.getAccountNumber(),amount,UUID.randomUUID().toString(),note, LocalDateTime.now(),Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note){
        validateAmount.validate(amount);
        if(fromAcc.equals(toAcc)){
            throw new ValidationExceptions("Cannot transfer to same account");
        }

        Account from = accountRepository.findByNumber(fromAcc)
        .orElseThrow(()-> new AccoountNotFoundExceptions("Account not Found: "+ fromAcc));
        Account to = accountRepository.findByNumber(toAcc)
        .orElseThrow(()-> new AccoountNotFoundExceptions("Account not Found: "+ toAcc));

        if(from.getBalance() < 0){
            throw new InsufficientFundsExceptions("Insufficient Balance in account: "+ fromAcc);
        }

        from.setBalance(from.getBalance() - amount);

        to.setBalance(to.getBalance() + amount);
        System.out.println(from.getBalance());
        Transaction fromTransaction = new Transaction(from.getAccountNumber(),amount,UUID.randomUUID().toString(),note, LocalDateTime.now(),Type.TRANSFEROUT);
        Transaction toTransaction = new Transaction(to.getAccountNumber(),amount,UUID.randomUUID().toString(),note, LocalDateTime.now(),Type.TRANSFERIN);

        transactionRepository.add(fromTransaction);
        transactionRepository.add(toTransaction);
    }


    @Override
    public List<Transaction> getStatement(String accountNumber) {
        return transactionRepository.findByAccount(accountNumber)
        .stream()
        .sorted(Comparator.comparing(Transaction::getTimestamp))
        .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query = (q==null) ? "" : q.toLowerCase();
        List<Account> result = new ArrayList<>();
        for(Customer c: customerRepository.findAll()){
            if(c.getCustomerName().toLowerCase().contains(query)){
                result.addAll(accountRepository.findByCustomerId(c.getCustomerId()));
            }
        }
        result.sort(Comparator.comparing(Account::getAccountNumber));

        return result;
    }
    private String getAccountNumber(){
        int size = accountRepository.findAll().size() + 1;
        String accountNumber  = String.format("AC%06d",size);
        return accountNumber;
    }


}
