package com.springBoot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(AppException.class);

	public AppException(String message) {
        super(message);
        log.info("Inside AppException");
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        log.info("Inside AppException Throwable");
    }
}