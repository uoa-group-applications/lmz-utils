package nz.ac.auckland.lmz.errors;

import java.util.Map;

/**
 * This type of Exception is for when an error response should be returned by the quickest path to a controller method.
 * This should always be caught, and declared to be thrown to avoid it bubbling past controller code, and returning a
 * raw Http-500 response to the client. Implement specific types of this exception for different areas of the
 * application.
 */
public abstract class ExpectedErrorException extends Exception {

    protected Map context;

    /** @see Exception#Exception(String) */
    public ExpectedErrorException(String message, Map context) {
        super(message);
        this.context = context;
    }

    /** @see Exception#Exception(String, Throwable) */
    public ExpectedErrorException(String message, Map context, Throwable cause) {
        super(message, cause);
        this.context = context;
    }

    public Map getContext() {
        return context;
    }

    public void setContext(Map context) {
        this.context = context;
    }

}
