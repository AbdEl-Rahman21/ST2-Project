package com.example.st2_project;

import java.io.Serializable;

public class User implements Serializable {
    private int id = 0;
    private String username = "";
    private String password = "";
    private String gender = "";
    private String country = "";

    public User(int id, String username, String password, String gender, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.country = country;
    }

    public User(String username, String password, String gender, String country) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }
}
