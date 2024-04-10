package com.example.flugzeug.service;

import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class FlightService implements IFlightService
{
    @Autowired
    private FlightRepository flightRepository;


    @Override
    public List<FlightApi> getAllFlights()
    {
        return flightRepository.findAll();
    }

    @Override
    public void createFlight(FlightApi flight)
    {
        flightRepository.save(flight);
    }

    @Override
    public FlightApi getFlightByName(String name)
    {
        return flightRepository.findFlightApiByName(name);
    }

    @Override
    public boolean updateFlight(FlightApi flight)
    {
        flightRepository.save(flight);
        return true;//TODO
    }

    @Override
    public boolean deleteFlight(String name)
    {
        flightRepository.deleteByName(name);
        return true;//TODO
    }

    @Override
    public void bookFlight(String flightName, String sitplaceName)
    {

    }

    @Override
    public int[] GetMatchingRows(String flightName, int placesInRow) {
        return new int[0];
    }
}
