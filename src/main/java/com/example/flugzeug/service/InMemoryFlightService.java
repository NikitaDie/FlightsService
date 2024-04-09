package com.example.flugzeug.service;

import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.repository.InMemoryFlightDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryFlightService implements IFlightService
{
    private final InMemoryFlightDAO repository = new InMemoryFlightDAO();

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
    public FlightApi getFlightByName(String name)
    {
        return findFlightByName(name).toApi();
    }

    @Override
    public boolean updateFlight(FlightApi flight)
    {
        return repository.updateFlight(new Flight(flight));
    }

    @Override
    public boolean deleteFlight(String name)
    {
        return repository.deleteFlight(name);
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
