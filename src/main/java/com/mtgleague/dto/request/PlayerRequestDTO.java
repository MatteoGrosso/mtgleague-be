package com.mtgleague.dto.request;

import java.util.Date;

public class PlayerRequestDTO {
    private String name;
    private String surname;
    private String email;
    private String password;

    public PlayerRequestDTO(){}

    public PlayerRequestDTO(String name, String surname, String email, String password){
        this.name= name;
        this.surname= surname;
        this.email= email;
        this.password= password;
    }

    //getters
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
