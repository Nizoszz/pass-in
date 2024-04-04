package rockseat.com.passin.domain.checkin.exception;

public class CheckInAlreadyExistException extends RuntimeException {
    public CheckInAlreadyExistException(String message) {
        super(message);
    }
}
