package com.example.flugzeug.service;

import com.example.flugzeug.exception.NotAvailableException;
import com.example.flugzeug.model.Flight;
import com.example.flugzeug.model.FlightApi;
import com.example.flugzeug.model.Sitplace;
import com.example.flugzeug.model.SitplaceApi;
import com.example.flugzeug.repository.FlightRepository;
import com.example.flugzeug.repository.SeatRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Primary
public class FlightService implements IFlightService
{
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;


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
//        Flight checkFlight = getFlightByName(flightApi.getName());

        List<SitplaceApi> sortedSeats = flightApi.getSeats().stream()
                .sorted(Comparator.comparingInt(SitplaceApi::getRow)
                        .thenComparingInt(SitplaceApi::getColumn).reversed()).toList();
        flightApi.setSeats(sortedSeats);

        createFlight(flightApi);

        var oldFlight = flightRepository.findFlightById(flightApi.getId());
        var seats = flightApi.getSeats().stream().map(Sitplace::new).toList();

        flightApi.setSeats(new ArrayList<>());
        updateSeats(oldFlight, new ArrayList<>());
        deleteSeats(oldFlight, seats);
//        Flight flight = new Flight(flightApi);
//
//        List<Sitplace> sortedSeats = flight.getSeats().stream()
//                .sorted(Comparator.comparingInt((Sitplace seat) -> seat.getPosition().getX())
//                        .thenComparingInt(seat -> seat.getPosition().getY()).reversed()).toList();
//
//        flight.setSeats(sortedSeats);
//
//        for (Sitplace seat : flight.getSeats()) {
//
//            Optional<Sitplace> oldSeat = checkFlight.getSeats()
//                    .stream()
//                    .filter(s -> s.getName().equals(seat.getName()))
//                    .findFirst();
//
//            if (oldSeat.isPresent())
//            {
//                if (oldSeat.get().equals(seat))
//                    continue;
////                else
////                {
////                    seatRepository.customDeleteById(oldSeat.get().getId());
////                    seatRepository.flush();
////                }
//            }
//
//            seat.setFlight(flight);
//            seatRepository.save(seat);
//        }
    }

    private void updateSeats(Flight oldFlight, List<Sitplace> newSeats) {

        List<Sitplace> sortedSeats = newSeats.stream()
                .sorted(Comparator.comparingInt(Sitplace::getX)
                        .thenComparingInt(Sitplace::getY).reversed()).toList();

        Map<String, Sitplace> seatMap = new HashMap<>();
        if (oldFlight != null)
            oldFlight.getSeats().forEach(seat -> seatMap.put(seat.getName(), seat));

        for (Sitplace seat : sortedSeats) {
            Sitplace oldSeat = seatMap.get(seat.getName());

            if (oldSeat != null && oldSeat.equals(seat)) {
                continue;
            }

            seat.setFlight(oldFlight);
            seatRepository.save(seat);
        }

    }

    protected void deleteSeats(Flight oldFlight, List<Sitplace> newSeats)
    {
        if(oldFlight == null)
            return;

        Map<Long, Sitplace> newSeatMap = newSeats.stream()
                .collect(Collectors.toMap(Sitplace::getId, Function.identity()));

        oldFlight.getSeats().forEach(seat -> {
            if (!newSeatMap.containsKey(seat.getId())) {
                seatRepository.customDeleteById(seat.getId());
            }
        });
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
