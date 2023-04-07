package com.mikhail.tarasevich.university.service.exception;

public class EmailAlreadyExistsException extends IllegalArgumentException{

    public EmailAlreadyExistsException(){}

    public EmailAlreadyExistsException(String errMessage){
        super(errMessage);
    }

}
