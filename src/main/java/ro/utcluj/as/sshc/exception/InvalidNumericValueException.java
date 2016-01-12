package ro.utcluj.as.sshc.exception;

public class InvalidNumericValueException extends Exception {

    private static final long serialVersionUID = 5340950350901426019L;

    public InvalidNumericValueException() {}

    public InvalidNumericValueException(String exception) {
        super(exception);
    }
    
}
