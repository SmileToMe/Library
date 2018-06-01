package com.p.library.cockraoch;


final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}