package app.core.exceptions;

public class ControllerException extends CouponSystemException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
