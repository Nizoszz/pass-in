package rockseat.com.passin.domain.event.exception;

public class SlugAlreadyExistException extends RuntimeException{
    public SlugAlreadyExistException(String message) {
        super(message);
    }
}
