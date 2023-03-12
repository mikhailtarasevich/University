package com.mikhail.tarasevich.university.exception;

public class ObjectWithSpecifiedIdNotFoundException extends IllegalArgumentException{

    public ObjectWithSpecifiedIdNotFoundException(){}

    public ObjectWithSpecifiedIdNotFoundException(String errMessage){
        super(errMessage);
    }

}
