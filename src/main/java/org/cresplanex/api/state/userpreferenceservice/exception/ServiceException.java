package org.cresplanex.api.state.userpreferenceservice.exception;

import build.buf.gen.userpreference.v1.UserPreferenceServiceErrorCode;

public abstract class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    abstract public UserPreferenceServiceErrorCode getServiceErrorCode();
    abstract public String getErrorCaption();
}
