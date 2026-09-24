package org.example.exception;

public class RuntimeException extends java.lang.RuntimeException {

    // TODO: Лучше никогда не называть классы именами стандартных классов Java.
        // Это может приводить к путанице и трудно отлавливаемым ошибкам.
        // Например, сейчас в PlayerDaoImpl выбрасывается именно java.lang.RuntimeException.

    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}