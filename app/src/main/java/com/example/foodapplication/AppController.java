package com.example.foodapplication;

import java.util.HashMap;

public class AppController {
    private static AppController ourInstance = new AppController();
    //    private static AppController ourInstance = null;
    public static final String TAG = AppController.class
            .getSimpleName();

    private User user;
    private String token;
    private HashMap<String, String> ticketFields;


    public static AppController getInstance() {
        if(null == ourInstance){
            ourInstance = new AppController();
        }
        return ourInstance;
    }

    protected AppController() {}

    public void setUser(User user) { this.user = user; }
    public User getUser() { return user; }

    public void set_token(String token) {
        this.token = token;
    }

    public String get_token() {
        return token;
    }


    public void setTicketFields(HashMap<String, String> ticketFields) {
        this.ticketFields = ticketFields;
    }

    public HashMap<String, String> getTicketFields() {
        return ticketFields;
    }
}
