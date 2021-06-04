package com.example.foodapplication.auth;

public class user {
    public int id;
    public String name;
    public String username;
    public String email;
    public String password;
    public String age;


    public user (int id, String name, String username, String email, String age, String amount, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getUsername(){ return username; }
    public String getEmail(){return email; }
    public String getAge(){ return age; }
    public String getPassword() {return password; }
}
