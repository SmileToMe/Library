package com.p.library.en;

public class Result<T> {
    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int errcode) {
        this.code = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
