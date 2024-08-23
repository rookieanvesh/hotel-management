package com.RestIn.HotelBooking.exception;

public class OurException extends RuntimeException{
    public OurException(String message){
        super(message);
        /*So, in summary, this code defines a custom exception class OurException that extends Exception.
         It provides a constructor that takes a String message and passes it to the Exception constructor
         using super(message). This allows you to create instances of OurException with a specific error
         message*/
    }
}
