package com.hci.carebase.ui.fragments;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.database.Cursor;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.hci.carebase.R;
import com.hci.carebase.data.datasource.CollectionsProvider;
import com.hci.carebase.data.interfaces.PhotoSource;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.util.Const;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.data.datasource.LocalCacheDb;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class ProfileFragment extends Fragment {
    private Patient p;
    private String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private String imgDecodableString;
    private int thisYear, thisMonth, thisDay, thisHour, thisMinute;
    private ILocalCache cache;

    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        if (args!=null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @BindView(R.id.patient_profile_photo) ImageView profilePhoto;
    @BindView(R.id.patient_first_name) TextView patientFName;
    @BindView(R.id.patient_last_name) TextView patientLName;
    @BindView(R.id.patient_birth_date) TextView patientBirthday;
    @BindView(R.id.patient_medical_conditions) TextView patientMedCon;
    @BindView(R.id.patient_allergies) TextView patientAllergies;
    @BindView(R.id.patient_blood_type) TextView patientBloodType;
    @BindView(R.id.patient_height) TextView patientHeight;
    @BindView(R.id.patient_weight) TextView patientWeight;
    @BindView(R.id.patient_health_card) ImageView patientHealthCard;

    @BindView(R.id.patient_profile_input_photo) ImageView profilePhotoInput;
    @BindView(R.id.patient_first_name_input) EditText patientFNameInput;
    @BindView(R.id.patient_last_name_input) EditText patientLNameInput;
    @BindView(R.id.patient_birth_date_input) EditText patientBirthdayInput;
    @BindView(R.id.patient_medical_conditions_input) EditText patientMedConInput;
    @BindView(R.id.patient_allergies_input) EditText patientAllergiesInput;
    //@BindView(R.id.patient_blood_type_input) EditText patientBloodTypeInput;
    @BindView(R.id.patient_height_input) EditText patientHeightInput;
    @BindView(R.id.patient_weight_input) EditText patientWeightInput;
    @BindView(R.id.patient_health_card_input) ImageView patientHealthCardInput;
    @BindView(R.id.button_profile_photo_upload) Button buttonProfilePhotoUpload;
    @BindView(R.id.patient_upload_health_card_button) Button buttonHealthCardUpload;
    @BindView(R.id.tv_hc_non_found) TextView hcNotFound;

    @BindView(R.id.patient_select_birthdate_button) Button selectBirthdateButton;

    @BindView(R.id.blood_type_selector) Spinner selectBloodType;

    @BindView(R.id.fragment_profile_vs) ViewSwitcher mViewSwitcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this,root);

        Bundle b = getArguments();
        p = (Patient) b.getSerializable(Const.BUNDLE_KEY_PATIENT);

        refreshPatientViewDetails(p);
        hcNotFound.setVisibility(GONE);;
        return root;
    }



    public void refreshPatientViewDetails(Patient p){
        SimpleDateFormat f = new SimpleDateFormat("MMM, dd yyyy");
        String readableBday = f.format(p.getHealthInfo().getDateOfBirth());
         /* Set First and Last Name */
        patientFName.setText(p.getFirstName());
        patientLName.setText(p.getLastName());
        patientBirthday.setText(readableBday);
        patientFNameInput.setText(p.getFirstName());
        patientLNameInput.setText(p.getLastName());
        patientBirthdayInput.setText(readableBday);


        /* Update Medical Conditions */
        try {
            String medicalConditions = p.getHealthInfo().getMedicalConditions();
            patientMedCon.setText(medicalConditions);
            patientMedConInput.setText(medicalConditions);
        } catch (Exception e) {
            Log.e(TAG, "Error no medical conditions... " +e.getMessage());
            patientMedCon.setText("None set.");
            patientMedConInput.setText("None set.");
        }



        try {
            String allergies = p.getHealthInfo().getAllergies();

            patientAllergies.setText(allergies);
            patientAllergiesInput.setText(allergies);

        } catch (Exception e) {
            Log.e(TAG, "Error no allergies... " +e.getMessage());
            patientAllergies.setText("None set.");
            patientAllergiesInput.setText("None set.");
        }

        /* Update Allergies */

        String weight = String.valueOf(p.getHealthInfo().getWeight()) ;
        String height = String.valueOf(p.getHealthInfo().getHeight()) ;
        String blood = String.valueOf(p.getHealthInfo().getBloodType());

        patientBloodType.setText(blood);
        patientWeight.setText(weight);
        patientHeight.setText(height);
        //patientBloodTypeInput.setText(blood);
        patientWeightInput.setText(weight);
        patientHeightInput.setText(height);

        ///HEALTH CARD INFO TO GO HERE
    }

    private String parseBloodType(int s) {
        switch (s) {
            case 1:
                return "A-";
            case 2:
                return "A+";
            case 3:
                return "O-";
            case 4:
                return "O";
            default:
                return "AB";
        }
    }


    public void onProfileEditClick() {
        refreshPatientViewDetails(p);
        mViewSwitcher.setDisplayedChild(1);
    }

    public void onProfileSaveClick(MainActivityCallback callback) {
        String origPatientFName = patientFName.getText().toString();
        String origPatientLName = patientLName.getText().toString();

        if (patientFNameInput.getText().toString().trim().length() == 0){
            Toast.makeText(getContext(), "First name not updated. Cannot be empty.", Toast.LENGTH_LONG).show();
            patientFNameInput.setText(origPatientFName);
        }
        else{
            p.setFirstName(patientFNameInput.getText().toString());
        }

        if (patientLNameInput.getText().toString().trim().length() == 0){
            Toast.makeText(getContext(), "Last name not updated. Cannot be empty.", Toast.LENGTH_LONG).show();
            patientLNameInput.setText(origPatientLName);
        }
        else{
            p.setLastName(patientLNameInput.getText().toString());
        }


        String medicalConditions = patientMedConInput.getText().toString();
        try{
            p.getHealthInfo().setMedicalConditions(medicalConditions);
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }

        String allergies = patientAllergiesInput.getText().toString();

        try{
            p.getHealthInfo().setAllergies(allergies);
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }

        String bloodType = selectBloodType.getSelectedItem().toString();
        String weight = patientWeightInput.getText().toString();
        String height = patientHeightInput.getText().toString();
        try{
            p.getHealthInfo().setBloodType(bloodType);
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }
        try{
            p.getHealthInfo().setWeight(Integer.parseInt(weight));
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }
        try{
            p.getHealthInfo().setHeight(Integer.parseInt(height));
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }


        callback.onSave(p);
        refreshPatientViewDetails(p);
        refreshPhotos();
        mViewSwitcher.setDisplayedChild(0);

    }

    private void refreshPhotos() {
        cache = new LocalCacheDb(getContext());
        String pId = cache.getUserId();
        fillHealthCard(pId);
        fillProfilePhoto(pId);
    }

    @OnClick(R.id.button_profile_photo_upload)
    public void onProfilePhotoUploadClick(View v) {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, Const.RESULT_LOAD_PROFILE_IMG);
    }


    @OnClick(R.id.patient_upload_health_card_button)
    public void onHealthCardPhotoUploadClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, Const.RESULT_LOAD_HC_IMG);

    }


    @OnClick(R.id.patient_select_birthdate_button)
    public void onPatientSelectBirthdateClick(View v){
        Calendar c = Calendar.getInstance();
        thisDay = c.get(Calendar.DAY_OF_MONTH);
        thisMonth = c.get(Calendar.MONTH);
        thisYear = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                onPatientBirthdateSubmit(year, month, day);
               // patientBirthdayInput.setText(p.getHealthInfo().getDateOfBirth().toString());
                SimpleDateFormat f = new SimpleDateFormat("MMM, dd yyyy");
                String readableBday = f.format(p.getHealthInfo().getDateOfBirth());
                patientBirthdayInput.setText(readableBday);
                patientBirthday.setText(readableBday);

            }
        }, thisYear, thisMonth, thisDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void onPatientBirthdateSubmit(int year, int month, int day){
        Date d = p.getHealthInfo().getDateOfBirth();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        Date newDate = c.getTime();
        try{
            p.getHealthInfo().setDateOfBirth(newDate);
        }catch  (NumberFormatException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case Const.RESULT_LOAD_PROFILE_IMG:
                try {
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        if (selectedImage != null) {
                            cache = new LocalCacheDb(getContext());
                            // FirebaseStorage storage;
                            String pId = cache.getUserId();
                            PhotoSource sr = CollectionsProvider.photos();

                            progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Processing...", true);
                            sr.uploadProfilePhoto(pId, selectedImage, taskSnapshot -> {
                                progressDialog.dismiss();
                                Glide.with(getContext())
                                        .load(taskSnapshot.getDownloadUrl())
                                        .into(profilePhotoInput);
                                Glide.with(getContext())
                                        .load(taskSnapshot.getDownloadUrl())
                                        .into(profilePhoto);

                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    super.onActivityResult(requestCode, resultCode, data);
                }
                super.onActivityResult(requestCode, resultCode, data);
                break;

            case Const.RESULT_LOAD_HC_IMG:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    if (selectedImage != null) {
                        cache = new LocalCacheDb(getContext());
                        // FirebaseStorage storage;
                        PhotoSource sr = CollectionsProvider.photos();
//                        sr.uploadProfilePhoto(pId, selectedImage, OnSuccessListener callback);


                        cache = new LocalCacheDb(getContext());
                        String pId = cache.getUserId();

                        progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Processing...", true);

                        sr.uploadHealthCardPhoto(pId, selectedImage, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Uri downloadUri = taskSnapshot.getDownloadUrl();
                                Glide.with(getContext()).load(downloadUri).into(patientHealthCardInput);
                                Glide.with(getContext()).load(downloadUri).into(patientHealthCard);

                            }
                        });
                        Toast.makeText(getContext(), "Photo uploading...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Something has gone very very wrong... Please upload again.", Toast.LENGTH_SHORT).show();
                    }
                    super.onActivityResult(requestCode, resultCode, data);

                }
                break;
        }

    }

    @Override
    public void onStart() {
        refreshPhotos();

        super.onStart();

    }

    private void fillProfilePhoto(String pId) {
        PhotoSource sr  = CollectionsProvider.photos();
        sr.getProfilePhoto(pId, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profilePhoto.invalidate();
                Glide.with(getContext()).load(uri).into(profilePhoto);
                Glide.with(getContext()).load(uri).into(profilePhotoInput);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void fillHealthCard(String pId) {
        if (cache.isDemo()) {
            patientHealthCard.setVisibility(GONE);
            hcNotFound.setVisibility(View.VISIBLE);
        } else {
            PhotoSource sr  = CollectionsProvider.photos();

            sr.getHealthCardPhoto(pId, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    hcNotFound.setVisibility(GONE);
                    patientHealthCard.setVisibility(View.VISIBLE);
                    patientHealthCard.invalidate();
                    Glide.with(getContext()).load(uri).into(patientHealthCard);
                    Glide.with(getContext()).load(uri).into(patientHealthCardInput);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    patientHealthCard.setVisibility(GONE);
                    hcNotFound.setVisibility(View.VISIBLE);

                }
            });
        }
    }
}



