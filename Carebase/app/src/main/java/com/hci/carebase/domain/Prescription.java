package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.Date;

public class Prescription implements Serializable {

    private String drugName;
    private String dosage;
    private Date dateIssued;
    private Date dateExpires;
    private String sideEffects;

    public Prescription() {
    }


    public Prescription(String drugName, Date dateIssued, Date dateExpires, String dosage, String sideEffects) {
        this.drugName = drugName;
        this.dosage = dosage;
        this.dateIssued = dateIssued;
        this.dateExpires = dateExpires;
        this.sideEffects = sideEffects;
    }



    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Date getIssued() {
        return dateIssued;
    }

    public void setIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public Date getExpires() {
        return dateExpires;
    }

    public void setExpires(Date dateExpires) {
        this.dateExpires = dateExpires;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "drugName='" + drugName + '\'' +
                "dosageDirections=" + dosage + '\''+
                ", issued=" + dateIssued +
                ", expires=" + dateExpires +
                '}';
    }
}
