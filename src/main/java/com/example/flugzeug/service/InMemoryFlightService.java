package com.example.flugzeug.service;

import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.repository.InMemoryFlightDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryFlightService implements IFlightService
{
    @Autowired
    private InMemoryFlightDAO repository;

    @Override
    public List<FlightApi> getAllFlights() {
        return repository.getAllFlights().stream()
                .map(Flight::toApi)
                .collect(Collectors.toList());
    }

    @Override
    public void createFlight(FlightApi flight)
    {
        repository.addFlight(new Flight(flight));
    }

    @Override
    public FlightApi getFlightApiByName(String name)
    {
        return findFlightByName(name).toApi();
    }

    @Override
    public void updateFlight(FlightApi flight)
    {
        repository.updateFlight(new Flight(flight));
    }

    @Override
    public void deleteFlight(String name)
    {
        repository.deleteFlight(name);
    }

    @Override
    public void bookFlight(String flightName, String sitplaceName)
    {
        findFlightByName(flightName).book(sitplaceName);
    }

    @Override
    public int[] GetMatchingRows(String flightName, int placesInRow)
    {
        return findFlightByName(flightName).findMatchingRows(placesInRow);
    }

    private Flight findFlightByName(String name)
    {
        return repository.findByName(name);
    }
}
