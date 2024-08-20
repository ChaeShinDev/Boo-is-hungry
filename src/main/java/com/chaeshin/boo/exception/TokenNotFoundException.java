package com.chaeshin.boo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException() {
        super("Token not found");
    }
}
