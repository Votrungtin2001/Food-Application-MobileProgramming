package com.example.foodapplication;

import org.jetbrains.annotations.NotNull;

public class IdWithNameListItem {
    private int id;
    private String name;

    public IdWithNameListItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @NotNull
    @Override
    public String toString() {
        return id + " - " + name;
    }
}