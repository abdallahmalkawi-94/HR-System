package com.demo.employeemanagementsystemdemo.user;

public class Vacation_Request {
    String name , phone , designation , vacation_date , submit_date;

    public Vacation_Request(String name, String phone, String designation, String vacation_date, String submit_date) {
        this.name = name;
        this.phone = phone;
        this.designation = designation;
        this.vacation_date = vacation_date;
        this.submit_date = submit_date;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDesignation() {
        return designation;
    }

    public String getVacation_date() {
        return vacation_date;
    }

    public String getSubmit_date() {
        return submit_date;
    }
}
