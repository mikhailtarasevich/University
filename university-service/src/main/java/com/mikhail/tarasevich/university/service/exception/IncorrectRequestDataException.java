package com.mikhail.tarasevich.university.service.exception;

public class IncorrectRequestDataException extends IllegalArgumentException{

    public IncorrectRequestDataException(){}

    public IncorrectRequestDataException(String errMessage){
        super(errMessage);
    }

}
