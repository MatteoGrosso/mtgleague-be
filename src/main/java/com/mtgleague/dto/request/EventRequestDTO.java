package com.mtgleague.dto.request;

import com.mtgleague.model.Player;

import java.util.Date;
import java.util.Set;

public class EventRequestDTO {
    private String name;
    private Date date;
    private int cap;
    private String description;

    public EventRequestDTO(){}

    public EventRequestDTO(String name, Date date, int cap, String description){
        this.name= name;
        this.date= date;
        this.cap= cap;
        this.description= description;
    }

    //getters
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public int getCap() {
        return cap;
    }
    public String getDescription() {
        return description;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setCap(int cap) {
        this.cap = cap;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
