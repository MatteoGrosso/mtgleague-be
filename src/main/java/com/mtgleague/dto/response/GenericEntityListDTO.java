package com.mtgleague.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GenericEntityListDTO<T> {
    @JsonProperty("results")
    private List<T> entities;

    public GenericEntityListDTO(List<T> entities) {
        this.entities = entities;
    }
}
