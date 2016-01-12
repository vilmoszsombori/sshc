package ro.utcluj.as.sshc.exception;

public class DivisionByZeroException extends Exception {
    
    private static final long serialVersionUID = 8664001504992958078L;

    public DivisionByZeroException() {}

    public DivisionByZeroException(String exception) {
        super(exception);
    }
    
}
