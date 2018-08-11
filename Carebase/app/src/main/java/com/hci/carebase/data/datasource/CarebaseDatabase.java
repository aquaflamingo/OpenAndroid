package com.hci.carebase.data.datasource;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.hci.carebase.data.datasource.interfaces.FirebaseCollections;
import com.hci.carebase.data.interfaces.PhotoSource;
import com.hci.carebase.data.interfaces.PatientSource;
import com.hci.carebase.domain.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is our general database object which is used to update Firebase cloud database.
 *
 * It implements the different sources because fancy, why not. But more realistically, just in case clients want more
 * granular access.
 */
public class CarebaseDatabase implements PatientSource, PhotoSource {
    private DatabaseReference patientsCollection = FirebaseDatabase.getInstance().getReference(FirebaseCollections.REF_PATIENTS);
    private StorageReference profilePhotoStorage = FirebaseStorage.getInstance().getReference(FirebaseCollections.REF_STORAGE_PROFILE_PHOTOS);
    private StorageReference appointmentPhotoStorage = FirebaseStorage.getInstance().getReference(FirebaseCollections.REF_STORAGE_APPOINTMENTS);
    private StorageReference healthCardPhotoStorage = FirebaseStorage.getInstance().getReference(FirebaseCollections.REF_STORAGE_HEALTHCARDS);

    protected CarebaseDatabase() {
    }

    @Override
    public void createPatient(Patient p, String userId, OnCompleteListener<Void> callback) {
        /* Creates new child node in "patientsCollection" collection and grabs it's key */

        patientsCollection.child(userId).setValue(p).addOnCompleteListener(callback);


    }

    @Override
    public void deletePatient(String userId, OnCompleteListener<Void> callback) {
        patientsCollection.child(userId).removeValue().addOnCompleteListener(callback);
    }

    @Override
    public void getPatient(String userId, ValueEventListener callback) {
        patientsCollection.child(userId).addListenerForSingleValueEvent(callback);
    }

    @Override
    public void updatePatient(String userId, Patient p, OnCompleteListener<Void> callback) {
        patientsCollection.child(userId).setValue(p).addOnCompleteListener(callback);
    }

    @Override
    public void uploadProfilePhoto(String userId, Uri photo, OnSuccessListener callback) {
        profilePhotoStorage.child(userId).putFile(photo).addOnSuccessListener(callback).addOnFailureListener(fail->{
            Log.e("FAIL", "FAILURE = " + fail.getMessage());
        });
    }

    @Override
    public void getProfilePhoto(String userId, OnSuccessListener callback,OnFailureListener fail) {
        profilePhotoStorage.child(userId).getDownloadUrl().addOnSuccessListener(callback).addOnFailureListener(fail);
    }



    private String[] photosMale = {
            "user_male_1.jpg","user_male_2.jpg","user_male_3.jpg"
    };

    private String[] photosFemale = {
            "user_female_1.jpg","user_female_2.jpg","user_female_3.jpg"
    };

    public String fakePhotoName(boolean malePhoto) {
        int num = (int)Math.floor(Math.random()*2);
        if (malePhoto) {
            return photosMale[num];
        }

        return photosMale[num];

    }

    @Override
    public void getRandomPhoto(boolean malePhoto, OnSuccessListener callback) {
        String fakePhoto = fakePhotoName(malePhoto);
        profilePhotoStorage.child(fakePhoto).getDownloadUrl().addOnSuccessListener(callback);
    }

    @Override
    public void uploadAppointmentPhoto(String userId, Uri photo, Date dateOccuring, OnSuccessListener callback) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String d = fmt.format(dateOccuring);
        String apptChild =  d+"-"+userId;
        appointmentPhotoStorage.child(apptChild).putFile(photo).addOnSuccessListener(callback);
    }

    @Override
    public void uploadHealthCardPhoto(String userId, Uri photo, OnSuccessListener callback) {
        healthCardPhotoStorage.child(userId).putFile(photo).addOnSuccessListener(callback);
    }

    @Override
    public void getHealthCardPhoto(String userId, OnSuccessListener callback,OnFailureListener fail) {
        healthCardPhotoStorage.child(userId).getDownloadUrl().addOnSuccessListener(callback).addOnFailureListener(fail);
    }

    @Override
    public void getFakeHealthCardPhoto(OnSuccessListener callback) {
        healthCardPhotoStorage.child("health_card_1.jpg").getDownloadUrl().addOnSuccessListener(callback).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIREBASE:","Error loading fake health card.. " + e.getMessage());
                    }
                }
        );
    }

    @Override
    public void getAppointmentPhoto(String userId, Date dateOccuring, OnSuccessListener callback) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String d = fmt.format(dateOccuring);
        String apptChild =  d+"-"+userId;
        appointmentPhotoStorage.child(apptChild).getDownloadUrl().addOnSuccessListener(callback);
    }
}
