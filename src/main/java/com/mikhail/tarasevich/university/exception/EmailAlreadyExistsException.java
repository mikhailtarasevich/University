package com.mikhail.tarasevich.university.exception;

public class EmailAlreadyExistsException extends IllegalArgumentException{

    public EmailAlreadyExistsException(){}

    public EmailAlreadyExistsException(String errMessage){
        super(errMessage);
    }

}
