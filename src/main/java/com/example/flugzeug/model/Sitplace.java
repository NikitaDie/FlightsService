package com.example.flugzeug.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seats")
public class Sitplace
{
    @Id
    @Getter
    private String name;

    @Column(columnDefinition = "boolean")
    private boolean isReserved;

    @Id
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    protected Sitplace() { }

    Sitplace(Position position)
    {
        name = positionToPlace(position);
    }

    Sitplace(SitplaceApi sitplaceApi)
    {
        this.name = sitplaceApi.getName();
        this.isReserved = sitplaceApi.isReserved();
    }

    public SitplaceApi toApi()
    {
        return new SitplaceApi(name, isReserved);
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReservation(boolean reserve) {
        isReserved = reserve;
    }

    private String positionToPlace(Position position) {
        return String.format("%d%s", position.getY(), numberToLetter(position.getX()));
    }

    public Position getPosition()
    {
        int x = LetterToNumber(name.charAt(1));
        int y = name.charAt(0) - '0';
        return new Position(x, y);
    }

    private static int LetterToNumber(char buchstabe)
    {
        return (buchstabe - 'A');
    }

    private static char numberToLetter(int i)
    {
        final int Alphabetlaenge = 25;
        return (char) ('A' + (i % Alphabetlaenge));
    }

}
