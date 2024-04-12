package com.example.flugzeug.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class FlightApi
{
    private Long id;
    private String name;
    private List<SitplaceApi> seats;
    private int totalSeatsCount;
    private int bookedSeatsCount;

    public FlightApi(Long id, String name, List<Sitplace> seats)
    {
        this.id = id;
        this.name = name;
        this.seats = seats.stream()
                .map(Sitplace::toApi)
                .collect(Collectors.toList());
        totalSeatsCount = seats.size();
        bookedSeatsCount = (int) seats.stream().filter(Sitplace::isReserved).count();
    }
}
