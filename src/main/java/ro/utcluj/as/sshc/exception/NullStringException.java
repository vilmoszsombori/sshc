package ro.utcluj.as.sshc.exception;

public class NullStringException extends Exception {

    private static final long serialVersionUID = 2892098579936928419L;

    public NullStringException() {}

    public NullStringException(String exception) {
        super(exception);
    }
    
}
