package com.example.myproject22.Model;

public class WeekItem {

    //region Component
    private String name;
    private String datestart;
    private String dateend;
    //endregion

    //region Constructor
    public WeekItem(String name, String datestart, String dateend) {
        this.name = name;
        this.datestart = datestart;
        this.dateend = dateend;
    }
    //endregion

    //region Get and Set
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
    //endregion

}
