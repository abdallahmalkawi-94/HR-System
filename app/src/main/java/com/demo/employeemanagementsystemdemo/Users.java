package com.demo.employeemanagementsystemdemo;

public class Users {
    String Full_Name , Email , Phone , Joining_Date , Designation;

    public Users(String full_Name, String email, String phone, String joining_Date, String designation) {
        Full_Name = full_Name;
        Email = email;
        Phone = phone;
        Joining_Date = joining_Date;
        Designation = designation;
    } // end Const()

    public String getFull_Name() {
        return Full_Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getJoining_Date() {
        return Joining_Date;
    }

    public String getDesignation() {
        return Designation;
    }
} // end class
