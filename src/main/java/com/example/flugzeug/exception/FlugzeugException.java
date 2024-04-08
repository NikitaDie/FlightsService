package com.example.flugzeug.exception;

public class FlugzeugException extends RuntimeException
{
    FlugzeugException(String errorMessage)
    {
        super(errorMessage);
    }

    FlugzeugException(String errorMessage, Throwable err)
    {
        super(errorMessage, err);
    }
}
