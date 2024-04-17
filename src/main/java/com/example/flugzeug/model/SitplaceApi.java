package com.example.flugzeug.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SitplaceApi
{
    private Long id;
    private String name;
    @JsonProperty("isReserved")
    private boolean isReserved;
    private int row;
    private int column;

    public SitplaceApi(Long id, String name, Position position, boolean isReserved)
    {
        this.id = id;
        this.name = name;
        this.column = position.getX();
        this.row = position.getY();
        this.isReserved = isReserved;
    }
}

