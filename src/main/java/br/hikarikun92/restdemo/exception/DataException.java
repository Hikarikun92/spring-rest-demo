package br.hikarikun92.restdemo.exception;

import org.springframework.dao.DataAccessException;

public class DataException extends DataAccessException {
    public DataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
