package org.shboland.api.convert;

public class ConvertException extends RuntimeException {

    ConvertException(String message) {
        super(message);
    }
    
    ConvertException(String message, Throwable e) {
        super(message, e);
    }
}