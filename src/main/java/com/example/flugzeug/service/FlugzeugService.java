package com.example.flugzeug.service;

import com.example.flugzeug.model.Flugzeug;
import org.springframework.stereotype.Service;

@Service
public class FlugzeugService
{
    private final Flugzeug model = new Flugzeug();

    public String getSitzplan()
    {
        return model.toString();
    }

    public void bucheFlug(String platz)
    {
        model.belege(platz);
    }

    public int[] GetFreieReihen(int personenImRheihe)
    {
        return model.findeReihen(personenImRheihe);
    }

}
