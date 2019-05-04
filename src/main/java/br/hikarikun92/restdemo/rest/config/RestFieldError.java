package br.hikarikun92.restdemo.rest.config;

public class RestFieldError {
    private String field;
    private String message;

    public RestFieldError() {
    }

    public RestFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
