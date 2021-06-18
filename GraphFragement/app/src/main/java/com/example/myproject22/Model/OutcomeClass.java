package com.example.myproject22.Model;

public class OutcomeClass {
    private String category;
    private Double money;
    //private Date date;

    public OutcomeClass(String category, Double money) {
        this.category = category;
        this.money = money;
        // this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

   /* public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/
}
