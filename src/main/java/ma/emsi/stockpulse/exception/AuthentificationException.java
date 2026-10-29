package ma.emsi.stockpulse.exception;

/**
 * Thrown on incorrect credentials, a blocked account or a denied access.
 */
public class AuthentificationException extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthentificationException(String message) {
        super(message);
    }
}
