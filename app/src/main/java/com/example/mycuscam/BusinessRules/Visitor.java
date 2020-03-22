package com.example.mycuscam.BusinessRules;

import java.util.Date;

public class Visitor {

    // All information from id card
    protected int id;
    protected String name;
    protected String surname;
    protected String gender;
    protected Date birthDate;
    protected String birthCity;

    // Visitor constructor
    public Visitor(int id, String name, String surname, String gender, Date birthDate, String birthCity) {
        this.id = id;
        this.name = name;
        this.surname =  surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.birthCity = birthCity;
    }
}
