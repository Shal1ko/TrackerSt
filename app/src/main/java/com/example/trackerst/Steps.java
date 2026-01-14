package com.example.trackerst;

public class Steps {
    private int id, steps;
    private String date;

    public Steps(int id, int steps, String date) {
        this.id = id;
        this.steps = steps;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getSteps() {
        return steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
