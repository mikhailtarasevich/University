package com.mikhail.tarasevich.university.exception;

public class IncorrectRequestDataException extends IllegalArgumentException{

    public IncorrectRequestDataException(){}

    public IncorrectRequestDataException(String errMessage){
        super(errMessage);
    }

}
