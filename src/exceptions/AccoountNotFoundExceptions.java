package exceptions;

import javax.security.auth.login.AccountNotFoundException;

public class AccoountNotFoundExceptions extends RuntimeException {
    public AccoountNotFoundExceptions (String message) {
        super(message);
    }
}
