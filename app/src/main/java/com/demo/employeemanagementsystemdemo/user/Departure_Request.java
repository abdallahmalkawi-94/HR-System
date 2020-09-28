package com.demo.employeemanagementsystemdemo.user;

public class Departure_Request {
    String departure_id , name , phone , designation , departure_date  , from , to , submit_date , status;

    public Departure_Request() {
    }

    public Departure_Request(String departure_id , String name, String phone, String designation, String departure_date, String from, String to, String submit_date, String status) {
        this.name = name;
        this.phone = phone;
        this.designation = designation;
        this.departure_date = departure_date;
        this.from = from;
        this.to = to;
        this.submit_date = submit_date;
        this.status = status;
        this.departure_id = departure_id;
    }

    public String getDeparture_id() {
        return departure_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeparture_id(String departure_id) {
        this.departure_id = departure_id;
    }
}
