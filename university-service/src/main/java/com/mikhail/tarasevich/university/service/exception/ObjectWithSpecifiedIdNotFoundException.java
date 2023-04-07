package com.mikhail.tarasevich.university.service.exception;

public class ObjectWithSpecifiedIdNotFoundException extends IllegalArgumentException{

    public ObjectWithSpecifiedIdNotFoundException(){}

    public ObjectWithSpecifiedIdNotFoundException(String errMessage){
        super(errMessage);
    }

}
