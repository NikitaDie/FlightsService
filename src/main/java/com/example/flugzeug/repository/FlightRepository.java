package com.example.flugzeug.repository;

import com.example.flugzeug.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>
{
    void deleteByName(String name);
    Flight findFlightByName(String name);
    Flight findFlightById(Long id);
}
