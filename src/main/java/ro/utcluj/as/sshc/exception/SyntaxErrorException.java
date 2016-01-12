package ro.utcluj.as.sshc.exception;

public class SyntaxErrorException extends Exception {

    private static final long serialVersionUID = 1582135134109460741L;

    public SyntaxErrorException() {}

    public SyntaxErrorException(String exception) {
        super(exception);
    }
    
}
