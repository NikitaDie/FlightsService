package com.example.flugzeug.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SitplaceApi
{
    private String name;
    private boolean isReserved;
    private int row;
    private int column;

    public SitplaceApi(String name, Position position, boolean isReserved)
    {
        this.name = name;
        this.column = position.getX();
        this.row = position.getY();
        this.isReserved = isReserved;
    }
}

