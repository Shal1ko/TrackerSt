package com.example.trackerst;

import androidx.annotation.Nullable;

public class Weight {
    private int id;
    private Double caloriesBurnt;
    private double weight;
    private String date;
    private String inputType;

    public Weight(int id, @Nullable Double caloriesBurnt, double weight, String date, String inputType) {
        this.id = id;
        this.caloriesBurnt = caloriesBurnt;
        this.weight = weight;
        this.date = date;
        this.inputType = inputType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public double getCaloriesBurnt() {
        if (caloriesBurnt != null) {
            return caloriesBurnt;
        }
        else {
            return 0.0;
        }
    }

    public void setCaloriesBurnt(Double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

}
