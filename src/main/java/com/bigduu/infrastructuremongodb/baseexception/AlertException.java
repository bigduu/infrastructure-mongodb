package com.bigduu.infrastructuremongodb.baseexception;


/**
 * @author mugeng.du
 */
public class AlertException extends RuntimeException {
    private String message;

    public AlertException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
