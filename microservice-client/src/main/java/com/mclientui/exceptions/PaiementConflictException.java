package com.mclientui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PaiementConflictException extends RuntimeException{

    public PaiementConflictException(String s){
        super(s);
    }
}
