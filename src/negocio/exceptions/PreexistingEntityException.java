package negocio.exceptions;

public class PreexistingEntityException extends Exception {
    public Object entity;
    
    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
    
    public PreexistingEntityException(String message, Object entity) {
        super(message);
        this.entity = entity;
    }
}
