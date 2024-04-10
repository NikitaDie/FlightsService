package com.example.flugzeug.model;

public class Sitplace
{
    private final String name;

    private boolean isReserved;

    Sitplace(Position position)
    {
        name = positionToPlace(position);
    }

    Sitplace(SitplaceApi sitplaceApi)
    {
        this.name = sitplaceApi.getName();
        this.isReserved = sitplaceApi.isReserved();
    }

    public String getName() {
        return name;
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
