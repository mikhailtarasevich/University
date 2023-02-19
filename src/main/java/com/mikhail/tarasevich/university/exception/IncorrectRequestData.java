package com.mikhail.tarasevich.university.exception;

public class IncorrectRequestData extends IllegalArgumentException{

    public IncorrectRequestData(){}

    public IncorrectRequestData(String errMessage){
        super(errMessage);
    }

}
