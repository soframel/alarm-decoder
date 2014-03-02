package org.soframel.alarm.decoder.contactid;

/**
 * User: sophie
 * Date: 2/3/14
 */
public class ContactIDException extends Exception {
    public ContactIDException() {
    }

    public ContactIDException(String detailMessage) {
        super(detailMessage);
    }

    public ContactIDException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ContactIDException(Throwable throwable) {
        super(throwable);
    }
}
