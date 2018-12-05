package com.github.zablvit.map.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceStillInUseException extends RuntimeException {
    public ResourceStillInUseException(String cause) {
        super(cause);
    }
}
