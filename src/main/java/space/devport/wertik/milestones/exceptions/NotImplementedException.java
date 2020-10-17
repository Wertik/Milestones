package space.devport.wertik.milestones.exceptions;

public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        super("Feature not implemented.");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}