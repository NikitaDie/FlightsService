package com.example.flugzeug.model;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.exception.WrongPositionException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "flights")
public class Flight
{
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String name;

    @Setter
    @OneToMany(mappedBy="flight", cascade = CascadeType.ALL)
    private List<Sitplace> seats;

    protected Flight() {}

    /*public Flight(String name, boolean[][] sitsplan)
    {
        this.name = name;
        seats = new ArrayList<>();
        InitSeatsplan(sitsplan);
    }*/

    public Flight (FlightApi flightApi)
    {
        this.id = flightApi.getId();
        this.name = flightApi.getName();
        this.seats = flightApi.getSeats().stream()
                .map(Sitplace::new)
                .collect(Collectors.toList());
    }

    public FlightApi toApi()
    {
        return new FlightApi(id, name, seats);
    }

    /*public void InitSeatsplan(boolean[][] seatsplan)
    {
        for (int i = 0; i < seatsplan.length; ++i)
        {
            for (int j = 0; j < seatsplan.length; ++j)
            {
                if (seatsplan[i][j])
                    seats.add(new Sitplace(new Position(i, j)));
            }
        }
    }

    public boolean[][] getSeatsplan()
    {
        boolean[][] seatsplan = new boolean[getHeightOfSitsplan() + 1][getLengthOfSitsplan() + 1];

        seats.stream().forEach(s ->
        {
           var pos = s.getPosition();
            seatsplan[pos.getY()][pos.getX()] = s.isReserved();
        });

        return seatsplan;
    }*/

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
        Sitplace sitplace = seats.stream().filter(x -> x.getName().equals(sitplaceName)).findFirst().orElse(null);
        try
        {
            if (sitplace == null)
                throw new WrongPositionException(sitplaceName);

            if (sitplace.isReserved())
                throw new NotAvailableException("The sit: " + sitplaceName + ", has been already booked.");

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

        Sitplace[] sitzplaetzeReihe = seats.stream()
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
        return Collections.max(seats, Comparator.comparingInt(s -> s.getPosition().getX())).getPosition().getX() + 1;
    }

    private int getHeightOfSitsplan()
    {
        return Collections.max(seats, Comparator.comparingInt(s -> s.getPosition().getY())).getPosition().getY() + 1;
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
