package com.example.flugzeug.model;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.exception.WrongPositionException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;


import java.security.InvalidParameterException;
import java.util.*;

/*@Entity*/
@Table(name = "flights")
public class Flight
{
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String name;

    List<Sitplace> sitplaces;

    @JsonIgnore
    public Flight(String name, boolean[][] sitsplan)
    {
        this.name = name;
        sitplaces = new ArrayList<>();
        InitSitsplan(sitsplan);
    }

    public Flight (FlightApi flightApi)
    {
        this.name = flightApi.getName();
        this.sitplaces = flightApi.getSeats();
    }

    public FlightApi toApi()
    {
        return new FlightApi(name, sitplaces);
    }

    public void InitSitsplan(boolean[][] sitzplan)
    {
        for (int i = 0; i < sitzplan.length; ++i)
        {
            for (int j = 0; j < sitzplan.length; ++j)
            {
                if (sitzplan[i][j])
                    sitplaces.add(new Sitplace(new Position(i, j)));
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getSomeNumber() {
        return 42;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean[][] getSitsplan()
    {
        boolean[][] sitsplan = new boolean[getHeightOfSitsplan() + 1][getLengthOfSitsplan() + 1];

        sitplaces.stream().forEach(s ->
        {
           var pos = s.getPosition();
           sitsplan[pos.getY()][pos.getX()] = s.isReserved();
        });

        return sitsplan;
    }

    private static void checkInput(int... values)
    {
        for (int value : values)
        {
            if (value < 0)
                throw new InvalidParameterException("Die Parameter müssen positiv sein.");
        }
    }

    public void book(String sitplaceName)
    {
        Sitplace sitplace = sitplaces.stream().filter(x -> x.getName().equals(sitplaceName)).findFirst().orElse(null);
        try
        {
            if (sitplace == null)
                throw new WrongPositionException(sitplaceName);

            if (sitplace.isReserved())
                throw new NotAvailableException(sitplaceName);

            sitplace.setReservation(true);
        }
        catch (RuntimeException e)
        {
            if (e instanceof NotAvailableException || e instanceof WrongPositionException)
                throw e;

            throw new WrongPositionException(sitplaceName);
        }
    }

    public int[] findMatchingRows(int placesInRow) {
        checkInput(placesInRow);

        int maxY = getHeightOfSitsplan();
        List<Integer> freeRows = new ArrayList<>();

        for (int i = 0; i < maxY; ++i)
            if (placesInRow <= getNumberOfFreePlaces(i))
                freeRows.add(i);

        return freeRows.stream().mapToInt(Integer::intValue).toArray();
    }

    private short getNumberOfFreePlaces(int reihe)
    {
        short currentFreePlaces = 0;
        List<Short> freePlaces = new ArrayList<>();

        Sitplace[] sitzplaetzeReihe = sitplaces.stream()
                .filter(s -> s.getPosition().getY() == reihe)
                .sorted(Comparator.comparingInt(s -> s.getPosition().getX()))
                .toArray(Sitplace[]::new);

        for (Sitplace platz : sitzplaetzeReihe) {
            if (platz.isReserved()) {
                freePlaces.add(currentFreePlaces);
                currentFreePlaces = 0;
            } else {
                ++currentFreePlaces;
            }
        }

        freePlaces.add(currentFreePlaces);
        return Collections.max(freePlaces);
    }

    private int getLengthOfSitsplan()
    {
        return Collections.max(sitplaces, Comparator.comparingInt(s -> s.getPosition().getX())).getPosition().getX();
    }

    private int getHeightOfSitsplan()
    {
        return Collections.max(sitplaces, Comparator.comparingInt(s -> s.getPosition().getY())).getPosition().getY();
    }


//    @Override
//    public String toString()
//    {
//        StringBuilder builder = new StringBuilder();
//
//        Sitplace[][] sitsplan = new Sitplace[getHeightOfSitsplan()][getLengthOfSitsplan()];
//
//        sitplaces.stream().forEach(s ->
//        {
//            var pos = s.getPosition();
//            sitsplan[pos.getY()][pos.getX()] = s;
//        });
//
//        for (Sitplace[] value : sitsplan) {
//            for (Sitplace sitplace : value) {
//                String field = sitplace.isReserved() ? "X" : sitplace.getName();
//                builder.append(field).append(' ');
//            }
//            builder.append('\n');
//        }
//
//        return builder.toString();
//    }

    @Override
    public String toString()
    {
        return name;
    }
}
