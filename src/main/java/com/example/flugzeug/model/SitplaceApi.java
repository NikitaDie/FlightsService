package com.example.flugzeug.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SitplaceApi
{
    private String name;
    private boolean isReserved;

    public SitplaceApi(String name, boolean isReserved)
    {
        this.name = name;
        this.isReserved = isReserved;
    }
}

