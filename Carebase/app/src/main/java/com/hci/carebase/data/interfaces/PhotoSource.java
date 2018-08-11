package com.hci.carebase.data.interfaces;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public interface PhotoSource {
    void uploadProfilePhoto(String patientId, Uri photo, OnSuccessListener<UploadTask.TaskSnapshot> callback);
    void getProfilePhoto(String patientId, OnSuccessListener<Uri> callback, OnFailureListener fail);
    void getRandomPhoto(boolean malePhoto, OnSuccessListener<Uri> callback);
    void uploadAppointmentPhoto(String patientId,Uri photo, Date dateOccuring, OnSuccessListener<UploadTask.TaskSnapshot> callback);
    void uploadHealthCardPhoto(String patientId,Uri photo, OnSuccessListener<UploadTask.TaskSnapshot> callback);
    void getHealthCardPhoto(String patientId, OnSuccessListener<Uri> callback, OnFailureListener fail);
    void getFakeHealthCardPhoto(OnSuccessListener<Uri> callback);
    void getAppointmentPhoto(String patientId, Date dateOccuring, OnSuccessListener<Uri> callback);
}
