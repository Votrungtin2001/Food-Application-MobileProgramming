package com.example.foodapplication.auth;

public class user {
    public int id;
    public int city_id;
    public String name;
    public String username;
    public String phone;
    public String email;
    public int gender;
    public String password;
    public String DoB;
    public String job;
    public String Fb;

public user(){}
    public user (int id, int city_id,String name, String username, String phone,String email,int gender, String password, String DoB,String job,String Fb){
        this.id = id;
        this.city_id = city_id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.DoB = DoB;
        this.job = job;
        this.Fb = Fb;
    }

    public int getId(){return id; }
    public int getCity_id(){return city_id; }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUsername(){ return username; }

    public String getPhone(){return phone; }

    public String getEmail(){return email; }

    public void setEmail(String email)
    {
        this.email= email;
    }

    public int getGender(){return gender;}

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password; }

    public String getDoB(){return DoB;}

    public String getJob(){return job;}
    public String getFb(){return Fb;}


}
