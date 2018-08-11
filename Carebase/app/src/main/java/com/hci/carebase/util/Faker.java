package com.hci.carebase.util;

import android.net.Uri;

import com.hci.carebase.domain.Appointment;
import com.hci.carebase.domain.Doctor;
import com.hci.carebase.domain.HealthInformation;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.domain.Prescription;
import com.hci.carebase.domain.Summary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Faker {
    private static Doctor[] doctors  = {
            new Doctor("Rob","Smoeis","email@email.com"),
            new Doctor("Arvinder","Bhathal","arv@email.com"),
            new Doctor("Nicole","Barachu","email@email.com"),
            new Doctor("Mario","Umar","email@email.com")
    };

    private static String[] conditions ={
            "Obesity", "Diabetes II", "Diabetes I", "Asthma", "Epilepsy", "Crohn's Disease", "Depression"
    };
    private static Prescription[] scripts ={
            new Prescription("Zonulex",
                    new Date(1521587400000L),
                    new Date(1520087355000L),
                    "50mg (two pill) every 3 hours", "Depression and Dizziness"),
            new Prescription("Amoxacilin",
                    new Date(1531886403200L),
                    new Date(1521585655000L),
                    "250mg (three pills) daily", "Heartburn, Indigestion, Headaches"),
            new Prescription("Riboflavin",
                    new Date(1538587303200L),
                    new Date(1521586655000L),
                    "150mg (one pill) daily", "Epilepsy")
    };



    private static HealthInformation[] healthInformations = {
            new HealthInformation(123, 156,
                    new Date(796259355000L),"O-",
                    "Cancer",
                    "Peanuts"),
            new HealthInformation(187, 145,
                    new Date(791222355000L),"O",
                    "Cancer",
                    "Peanuts"),
            new HealthInformation(156, 155,
                    new Date(789259355000L),"O",
                    "Cancer",
                    "Peanuts")
    };

    private static Patient[] patients = {
            new Patient("Jon","Bobby","LX4431232",
                    doctors[0],
                    new ArrayList<Appointment>(),
                    new ArrayList<Summary>(),
                    new ArrayList<Prescription>(),
                    healthInformations[0],
                    new Date()),
            new Patient("Barry","Conrad","LX452134",
                    doctors[1],
                    new ArrayList<Appointment>(),
                    new ArrayList<Summary>(),
                    new ArrayList<Prescription>(),
                    healthInformations[1],
                    new Date()),
            new Patient("Jessica","Porchen","LX752334",
                    doctors[2],
                    new ArrayList<Appointment>(),
                    new ArrayList<Summary>(),
                    new ArrayList<Prescription>(),
                    healthInformations[2],
                    new Date()),
    };

    private static Summary[] summaries ={
         new Summary("Great Progress","Patient is progressing exceptionally, there is an overall 18% reduction in tumour volume. In the later rounds of treatment we have been seeing incredible results.", new Date()),
         new Summary("Recommend Steps For Next Visit","Treatment has just become, patient is recommended to return in two weeks for new appointment and assignment", new Date()),
         new Summary("On Our Way","Halfway through chemotherapy of second round. Patient has mentioned symptoms of brain fog and vomiting. Prescribing Zonulex.", new Date()),
    };




    public static Doctor fakeDoctor() {
        int num = (int)Math.floor(Math.random()*3);
        return doctors[num];
    }

    public static HealthInformation fakeInfo() {
        int num =  (int)Math.floor(Math.random()*2);
        return healthInformations[num];
    }

    public static Patient fakePatient() {
        int num = (int)Math.floor(Math.random()*2);
        Patient p = patients[num];
        p.getSummaries().add(summaries[num]);
        //p.getHealthInfo().getMedicalConditions() = conditions[num];
        p.getScripts().add(scripts[num]);
        return p;
    }

    public static Summary fakeSummary() {
        int num = (int)Math.floor(Math.random()*2);
        return summaries[num];
    }

    public static Prescription fakeScript() {
        int num = (int)Math.floor(Math.random()*2);
        return scripts[num];
    }

    public static HealthInformation defaultInfo() {
        return new HealthInformation(175,180,new Date(791222355000L),"A","Cancer","None");
    }
}
