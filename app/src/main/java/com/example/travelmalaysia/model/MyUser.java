package com.example.travelmalaysia.model;

public class MyUser {
    private int id;
    private String name;
    private String email;
    private String password;
    private int points;

    public MyUser() {
    }

    public MyUser(int id, String name, String email, String password, int points) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public MyUser(String name, String email, String password, int points) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public MyUser(MyUser user) {
        this.id = user.id;
        this.name = user.name;
        this.points = user.points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
