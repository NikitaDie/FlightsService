package com.example.flugzeug.controller;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.exception.WrongPositionException;
import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.service.IFlightService;
import com.example.flugzeug.service.InMemoryFlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController
{
    private final IFlightService service = new InMemoryFlightService();

    @GetMapping
    public List<FlightApi> getFlights()
    {
        return service.getAllFlights();
    }

    @PostMapping("/create-flight")
    public void createFlight(@RequestBody FlightApi newFlight)
    {
        service.createFlight(newFlight);
    }

    @GetMapping("/{flightName}")
    @ResponseBody
    public FlightApi getFlight(@PathVariable String flightName)
    {
        var response = service.getFlightByName(flightName);
        if (response != null)
            return response;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update-flight")
    public void updateFlight(@RequestBody FlightApi updatedFlight)
    {
        if (!service.updateFlight(updatedFlight))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{flightName}")
    public void deleteFlight(@PathVariable String flightName)
    {
        if (!service.deleteFlight(flightName))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


    @PutMapping("book/{flightName}")
    public void bookFlug(@PathVariable String flightName, @RequestParam String sitplace)
    {
        try {
            service.bookFlight(flightName, sitplace);
        } catch (Exception e) {
            if (e instanceof WrongPositionException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );

            if (e instanceof NotAvailableException)
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, e.getMessage()
                );

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }

    @GetMapping("/{flightName}/free-rows")
    public int[] GetMatchingRows(@PathVariable String flightName, @RequestParam int placesInRow)
    {
        try {
            return service.GetMatchingRows(flightName, placesInRow);
        }
        catch (Exception e)
        {
            if (e instanceof InvalidParameterException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }

}
