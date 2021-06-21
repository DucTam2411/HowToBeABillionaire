package com.example.myproject22.Model;

public class UserClass {
    private int ID_USER;
    private String USERNAME;
    private String EMAIL;
    private String PASSWORD;
    private String FULLNAME;
    private String DATESTART;
    private String IMAGE;
    private double SALARY;

    public UserClass(String USERNAME, String EMAIL, String PASSWORD, String FULLNAME, String DATESTART, String IMAGE, double SALARY) {
        this.USERNAME = USERNAME;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.FULLNAME = FULLNAME;
        this.DATESTART = DATESTART;
        this.IMAGE = IMAGE;
        this.SALARY = SALARY;
    }

    public UserClass(String FULLNAME, String DATESTART) {
        this.FULLNAME = FULLNAME;
        this.DATESTART = DATESTART;
    }

    public UserClass(String FULLNAME, String DATESTART, String IMAGE) {
        this.FULLNAME = FULLNAME;
        this.DATESTART = DATESTART;
        this.IMAGE = IMAGE;
    }

    public int getID_USER() {
        return ID_USER;
    }

    public void setID_USER(int ID_USER) {
        this.ID_USER = ID_USER;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getDATESTART() {
        return DATESTART;
    }

    public void setDATESTART(String DATESTART) {
        this.DATESTART = DATESTART;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public double getSALARY() {
        return SALARY;
    }

    public void setSALARY(double SALARY) {
        this.SALARY = SALARY;
    }
}
