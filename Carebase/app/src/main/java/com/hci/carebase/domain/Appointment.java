package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {
    private Date dateFor;
    private Doctor doctor;
    private String patientComments;
    private String hospitalLocation;
    private String preparations;
    private boolean hasImageAttachment;

    public Appointment() {
    }

    public Appointment(Date dateFor, String hospitalLocation, Doctor doctor, String patientComments, String prep) {
        this.dateFor = dateFor;
        this.hospitalLocation = hospitalLocation;
        this.doctor = doctor;
        this.preparations = prep;
        this.patientComments = patientComments;
    }

    public Appointment(Date dateFor, Doctor doctor, String patientComments, String hospitalLocation, String prep, boolean imageURL) {
        this.dateFor = dateFor;
        this.doctor = doctor;
        this.patientComments = patientComments;
        this.hospitalLocation = hospitalLocation;
        this.preparations = prep;
        this.hasImageAttachment = imageURL;
    }


    public boolean hasImageAttachment() {
        return hasImageAttachment;
    }

    public void setHasImageAttachment(boolean hasImageAttachment) {
        this.hasImageAttachment = hasImageAttachment;
    }

    public String getPreparations() {
        return preparations;
    }

    public void setPreparations(String preparations) {
        this.preparations = preparations;
    }

    public Date getDateFor() {
        return dateFor;
    }

    public void setDateFor(Date dateFor) {
        this.dateFor = dateFor;
    }

    public String getPatientComments() {
        return patientComments;
    }

    public void setPatientComments(String patientComments) {
        this.patientComments = patientComments;
    }

    public String getHospitalLocation() {
        return hospitalLocation;
    }

    public void setHospitalLocation(String hospitalLocation) {
        this.hospitalLocation = hospitalLocation;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


}

