package com.example.flugzeug.exception;

public class NotAvailableException extends FlightsException
{
    public NotAvailableException()
    {
        super("Sitz ist schon besetzt.");
    }

    public NotAvailableException(String message)
    {
        super(message);
    }
}
