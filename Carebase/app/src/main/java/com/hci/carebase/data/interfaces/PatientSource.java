package com.hci.carebase.data.interfaces;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;
import com.hci.carebase.domain.Patient;

public interface PatientSource {
    void createPatient(Patient p,String userId, OnCompleteListener<Void> callback);
    void deletePatient(String userId,  OnCompleteListener<Void> callback);
    void getPatient(String userId,ValueEventListener callback);
    void updatePatient(String userId, Patient p,OnCompleteListener<Void> callback);
}
