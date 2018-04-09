package com.example.travelmalaysia.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private double points;

    public User() {
    }

    public User(int id, String name, String email, String password, double points) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public User(String name, String email, String password, double points) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
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

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String toStringWithId() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", points=" + points +
                '}';
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", points=" + points +
                '}';
    }

}
