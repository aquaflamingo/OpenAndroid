package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthInformation implements Serializable {
    public enum BloodTypes {A,B,O};

    private int weight;
    private int height;
    private Date dateOfBirth;
    private String bloodType;
    private String medicalConditions;
    private String allergies;


    public HealthInformation(int weight, int height, Date dateOfBirth, String bloodType, String medicalConditions, String allergies) {
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.medicalConditions = medicalConditions;
        this.allergies = allergies;
    }

    public HealthInformation() {
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() { return height; }

    public void setHeight(int height){this.height = height;}

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "HealthInformation{" +
                "weight=" + weight +
                ", height=" + height +
                ", dateOfBirth=" + dateOfBirth +
                ", bloodType=" + bloodType +
                ", medicalConditions=" + medicalConditions +
                ", allergies=" + allergies +
                '}';
    }
}
