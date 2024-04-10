package com.example.flugzeug.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sits")
public class SitplaceApi
{
    @Id
    @GeneratedValue
    Long id;
    private String name;
    @Column(columnDefinition = "boolean")
    private boolean isReserved;
    @ManyToOne
    @JoinColumn(name="flightApi_id", nullable=false)
    private FlightApi flight;

    public SitplaceApi() {}

    SitplaceApi(Sitplace sitplace)
    {
        this.name = sitplace.getName();
        this.isReserved = sitplace.isReserved();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public FlightApi getFlight() {
        return flight;
    }

    public void setFlight(FlightApi flight) {
        this.flight = flight;
    }
}

