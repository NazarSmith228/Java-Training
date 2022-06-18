package org.java.training.web.net.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.java.training.web.common.HttpStatusCode;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class SocketException extends RuntimeException {

    HttpStatusCode httpStatus;

    public SocketException(String message, Throwable cause, HttpStatusCode httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
