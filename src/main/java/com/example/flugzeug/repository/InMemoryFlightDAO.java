package com.example.flugzeug.repository;

import com.example.flugzeug.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryFlightDAO
{
    private final List<Flight> FLIGHTS = new ArrayList<>(Arrays.asList(
            new Flight("A380", new boolean[][]{{true, false},{true, true}})
    ));

    public List<Flight> getAllFlights() {
    return FLIGHTS;
}

    public void addFlight(Flight flight) {
        FLIGHTS.add(flight);
    }

    public Flight findByName(String name) {
        return FLIGHTS.stream()
                .filter(element -> element.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean updateFlight(Flight flight) {
        var flightIndex = IntStream.range(0, FLIGHTS.size())
                .filter(index -> FLIGHTS.get(index).getName().equals(flight.getName()))
                .findFirst()
                .orElse(-1);

        if (flightIndex == -1)
            return false;

        FLIGHTS.set(flightIndex, flight);
        return true;
    }

    public boolean deleteFlight(String name) {
        var flight = findByName(name);
        if (flight == null)
            return false;

        FLIGHTS.remove(flight);
        return true;
    }
}
