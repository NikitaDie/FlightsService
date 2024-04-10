package com.example.flugzeug.repository;

import com.example.flugzeug.model.FlightApi;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightApi, Long>
{
    void deleteByName(String name);
    FlightApi findFlightApiByName(String name);
}
