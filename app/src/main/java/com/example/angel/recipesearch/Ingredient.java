package com.example.angel.recipesearch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ingredient {

    private Integer amount;

    private String name;

    private String expiration;

    public Ingredient(String name) {
        this.amount = 0;
        this.name = name;
        this.expiration = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
    }

    public Ingredient(String name, int amount) {
        this.amount = amount;
        this.name = name;
        this.expiration = "";
    }

    public Ingredient(String name, int amount, String date) {
        this.amount = amount;
        this.name = name;
        this.expiration = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expiration;
    }

    public void setExpirationDate(String expiration) {
        this.expiration = expiration;
    }
}
