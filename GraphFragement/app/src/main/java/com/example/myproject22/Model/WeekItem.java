package com.example.myproject22.Model;

public class WeekItem {
    private String name;
    private String datestart;
    private String dateend;

    public WeekItem(String name, String datestart, String dateend) {
        this.name = name;
        this.datestart = datestart;
        this.dateend = dateend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatestart() {
        return datestart;
    }

    public void setDatestart(String datestart) {
        this.datestart = datestart;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }
}
