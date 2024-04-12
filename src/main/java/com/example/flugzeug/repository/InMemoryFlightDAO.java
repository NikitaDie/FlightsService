package com.example.flugzeug.repository;

import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryFlightDAO
{
    private final List<Flight> FLIGHTS = new ArrayList<>();

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

    public void updateFlight(Flight flight) {
        var flightIndex = IntStream.range(0, FLIGHTS.size())
                .filter(index -> FLIGHTS.get(index).getId().equals(flight.getId()))
                .findFirst()
                .orElse(-1);

        if (flightIndex == -1)
            throw new EntityNotFoundException(String.format("Flight was not found for parameters during updating {name=%s}", flight.getName()));

        FLIGHTS.set(flightIndex, flight);
    }

    public void deleteFlight(String name) {
        var flight = findByName(name);
        if (flight == null)
            throw new EntityNotFoundException(String.format("Flight was not found for parameters during deleting {name=%s}", name));

        FLIGHTS.remove(flight);
    }
}
