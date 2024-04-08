package com.example.flugzeug.controller;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.exception.WrongPositionException;
import com.example.flugzeug.service.FlugzeugService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/api/v1/flugzeug")
public class FlugzeugController
{
    private final FlugzeugService service;

    public FlugzeugController(FlugzeugService service)
    {
        this.service = service;
    }

    @GetMapping
    public String getFlugzeug()
    {
        return service.getSitzplan();
    }

    @GetMapping("/freie-reihen")
    public int[] GetFreieReihen(@RequestParam int personenImRheihe)
    {
        try {
            return service.GetFreieReihen(personenImRheihe);
        }
        catch (Exception e)
        {
            if (e instanceof InvalidParameterException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }

    @PutMapping("buche/{platz}")
    public void bucheFlug(@PathVariable String platz)
    {
        try {
            service.bucheFlug(platz);
        }
        catch (Exception e)
        {
            if (e instanceof WrongPositionException)
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
                );

            if (e instanceof NotAvailableException)
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, e.getMessage()
                );

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }
}
