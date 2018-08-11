package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.List;

public class Doctor implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private List<Patient> patients;

    // Required for DataSnapshot.getValue(//.class)
    public Doctor() {
    }

    public Doctor(String fname, String lname) {
        this.firstName = fname;
        this.lastName = lname;
    }

    public Doctor(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", patients=" + patients +
                '}';
    }
}
