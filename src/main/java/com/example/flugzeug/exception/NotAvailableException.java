package com.example.flugzeug.exception;

public class NotAvailableException extends FlugzeugException
{
    public NotAvailableException()
    {
        super("Sitz ist schon besetzt.");
    }

    public NotAvailableException(String position)
    {
        super("Sitz: " + position + " ist schon besetzt.");
    }
}
