package com.example.flugzeug.exception;

public class FlightsException extends RuntimeException
{
    FlightsException(String errorMessage)
    {
        super(errorMessage);
    }

    FlightsException(String errorMessage, Throwable err)
    {
        super(errorMessage, err);
    }
}
