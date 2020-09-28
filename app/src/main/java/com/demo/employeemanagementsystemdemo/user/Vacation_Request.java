package com.demo.employeemanagementsystemdemo.user;

public class Vacation_Request {
    String vacation_id , name , phone , designation , vacation_date , submit_date , status;

    public Vacation_Request() {
    }

    public Vacation_Request(String vacation_id , String name, String phone, String designation, String vacation_date, String submit_date , String status) {
        this.name = name;
        this.phone = phone;
        this.designation = designation;
        this.vacation_date = vacation_date;
        this.submit_date = submit_date;
        this.status = status;
        this.vacation_id = vacation_id;
    }

    public String getVacation_id() {
        return vacation_id;
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

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setVacation_date(String vacation_date) {
        this.vacation_date = vacation_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVacation_id(String vacation_id) {
        this.vacation_id = vacation_id;
    }
}
