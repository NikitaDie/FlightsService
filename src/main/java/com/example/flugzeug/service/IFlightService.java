package com.example.flugzeug.service;

import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;

import java.util.List;

public interface IFlightService
{
    public List<FlightApi> getAllFlights();

    public void createFlight(FlightApi flight);
    public FlightApi getFlightByName(String name);
    public void updateFlight(FlightApi flight);
    public void deleteFlight(String name);
    public void bookFlight(String flightName, String sitplaceName);
    public int[] GetMatchingRows(String flightName, int placesInRow);
}
