package com.lcwd.electronicStore.exception;

public class BadApiRequestException extends  RuntimeException{

    public BadApiRequestException(String message){
        super(message);
    }

    public BadApiRequestException(){
        super("Bad Api request!!");
    }
}
