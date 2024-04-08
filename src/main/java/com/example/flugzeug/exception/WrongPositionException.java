package com.example.flugzeug.exception;

public class WrongPositionException extends FlugzeugException
{
    public WrongPositionException()
    {
        super("Position existiert nicht");
    }

    public WrongPositionException(String position)
    {
        super("Position: " + position + " existiert nicht.");
    }
}
