package com.example.flugzeug.service;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.repository.FlightRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Primary
public class FlightService implements IFlightService
{
    @Autowired
    private FlightRepository flightRepository;


    @Override
    public List<FlightApi> getAllFlights()
    {
        return flightRepository.findAll().stream()
                .map(Flight::toApi)
                .collect(Collectors.toList());
    }

    @Override
    public void createFlight(FlightApi flightApi)
    {
        Flight checkFlight = getFlightByName(flightApi.getName());
            if (checkFlight != null && !Objects.equals(checkFlight.getId(), flightApi.getId()))
                throw new NotAvailableException("Flight with Name: " + flightApi.getName() +  " ,already exists");

        Flight flight = new Flight(flightApi);
        for (var seat : flight.getSeats())
            seat.setFlight(flight);
        flightRepository.save(flight);
    }

    @Nullable
    private Flight getFlightByName(String name)
    {
        return flightRepository.findFlightByName(name);
    }

    @Override
    public FlightApi getFlightApiByName(String name)
    {
        Flight flight = flightRepository.findFlightByName(name);
        if (flight == null)
            throw new EntityNotFoundException(String.format("Flight was not found for parameters {name=%s}", name));

        return flight.toApi();
    }

    @Override
    public FlightApi getFlightApiById(long id) {
        Flight flight = flightRepository.findFlightById(id);
        if (flight == null)
            throw new EntityNotFoundException(String.format("Flight was not found for parameters {id=%s}", id));

        return flight.toApi();
    }

    @Override
    public void updateFlight(FlightApi flightApi)
    {
        createFlight(flightApi);
    }

    @Override
    @Transactional
    public void deleteFlight(String name)
    {
        Flight flight = flightRepository.findFlightByName(name);
        if (flight == null)
            throw new EntityNotFoundException(String.format("Flight was not found during deleting for parameters {name=%s}", name));

        flightRepository.deleteByName(name);
    }

    @Override
    public void bookFlight(String flightName, String sitplaceName)
    {
       Flight flight = new Flight(getFlightApiByName(flightName));
       flight.book(sitplaceName);
       updateFlight(flight.toApi());
    }

    @Override
    public int[] GetMatchingRows(String flightName, int placesInRow) {
        Flight flight = getFlightByName(flightName);
        return flight.findMatchingRows(placesInRow);
    }
}
