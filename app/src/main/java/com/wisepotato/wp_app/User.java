package com.wisepotato.wp_app;


public class User {
    private int id , Age;
    private String username, email;

    public User(int id, String username,String email ,int Age) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.Age=Age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge(){
        return Age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}