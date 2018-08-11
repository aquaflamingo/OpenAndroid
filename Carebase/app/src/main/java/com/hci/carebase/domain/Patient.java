package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient implements Serializable {
    private String firstName;
    private String lastName;
    private String healthCardNumber;
    private Doctor doctor;
    private ArrayList<Appointment> appointments;
    private ArrayList<Summary> summaries;
    private ArrayList<Prescription> scripts;
    private HealthInformation healthInfo;
    private Date finishDate;

    // Required for DataSnapshot.getValue(//.class)
    public Patient() {
    }

    public Patient(String firstName,
                   String lastName,
                   String healthCardNumber,
                   Doctor doctor,
                   ArrayList<Appointment> appointments,
                   ArrayList<Summary> summaries,
                   ArrayList<Prescription> scripts,
                   HealthInformation info,
                   Date finishDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthCardNumber = healthCardNumber;
        this.doctor = doctor;
        this.appointments = appointments;
        this.summaries = summaries;
        this.scripts = scripts;
        this.healthInfo = info;
        this.finishDate = finishDate;
    }


    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public ArrayList<Summary> getSummaries() {
        return summaries;
    }

    public void setSummaries(ArrayList<Summary> summaries) {
        this.summaries = summaries;
    }

    public ArrayList<Prescription> getScripts() {
        return scripts;
    }

    public void setScripts(ArrayList<Prescription> scripts) {
        this.scripts = scripts;
    }


    public HealthInformation getHealthInfo() {
        return healthInfo;
    }

    public void setHealthInfo(HealthInformation healthInfo) {
        this.healthInfo = healthInfo;
    }


    @Override
    public String toString() {
        return "Patient{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", healthCardNumber='" + healthCardNumber + '\'' +
                ", healthInfo=" + healthInfo +
                ",\n doctor=" + doctor +
                ",\n appointments=" + appointments +
                ",\n summaries=" + summaries +
                ",\n scripts=" + scripts +
                '}';
    }
}
