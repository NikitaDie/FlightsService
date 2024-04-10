package com.example.flugzeug.controller;

import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.service.IFlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController
{
    @Autowired
    private IFlightService service;

    @GetMapping
    public List<FlightApi> getFlights()
    {
        return service.getAllFlights();
    }

    @PostMapping
    public void createFlight(@RequestBody FlightApi newFlight)
    {
        service.createFlight(newFlight);
    }

    @GetMapping("/{flightName}")
    @ResponseBody
    public FlightApi getFlight(@PathVariable String flightName)
    {
        return service.getFlightApiByName(flightName);
    }

    @PutMapping
    public void updateFlight(@RequestBody FlightApi updatedFlight)
    {
        service.updateFlight(updatedFlight);
    }

    @DeleteMapping("/{flightName}")
    public void deleteFlight(@PathVariable String flightName)
    {
        service.deleteFlight(flightName);
    }


    @PutMapping("{flightName}/book")
    public void bookFlight(@PathVariable String flightName, @RequestParam String sitplace)
    {
        service.bookFlight(flightName, sitplace);
    }

    @GetMapping("/{flightName}/free-rows")
    public int[] GetMatchingRows(@PathVariable String flightName, @RequestParam int placesInRow)
    {
        return service.GetMatchingRows(flightName, placesInRow);
    }

}
