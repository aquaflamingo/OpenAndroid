package com.hci.carebase.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.hci.carebase.R;
import com.hci.carebase.data.datasource.CollectionsProvider;
import com.hci.carebase.data.datasource.LocalCacheDb;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.data.interfaces.PatientSource;
import com.hci.carebase.domain.Appointment;
import com.hci.carebase.domain.Doctor;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.domain.Prescription;
import com.hci.carebase.domain.Summary;
import com.hci.carebase.ui.MainActivity;
import com.hci.carebase.util.Faker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginRegisterActivity extends AppCompatActivity {
    private final String DEMO_EMAIL = "demo@example.com";
    private final String DEMO_PASS = "password";
    private final String DEMO_UID = "demo";

    private FirebaseAuth fauth;
    private ILocalCache cache;
    private DatabaseReference ref;
    private ProgressDialog progressDialog;

    @BindView(R.id.login_register_viewswitcher) ViewSwitcher viewSwitcher;
    @BindView(R.id.vs_login_email_et) EditText login_emailLoginEt;
    @BindView(R.id.vs_login_password_et) EditText login_passwordLoginEt;
    @BindView(R.id.vs_signup_patient_fname) EditText signup_patient_fname;
    @BindView(R.id.vs_signup_patient_lname) EditText signup_patient_lname;
    @BindView(R.id.vs_signup_email_et) EditText signup_signUpEmailEt;
    @BindView(R.id.vs_signup_password_et) EditText signup_signUpPasswordEt;
    @BindView(R.id.vs_signup_password_confirm_et) EditText signup_signUpConfirmEt;
    @BindView(R.id.vs_signup_patient_doctor_fname) EditText signup_signUpDoctorFnameEt;
    @BindView(R.id.vs_signup_patient_doctor_lname) EditText signup_signUpDoctorLnameEt;
    @BindView(R.id.vs_signup_patient_hcard_num) EditText signup_signUpHCardNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
        fauth = FirebaseAuth.getInstance();
        cache = new LocalCacheDb(this);
        if (cache.isLoggedIn()) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }


    @OnClick(R.id.vs_login_button_i_dont_have_an_account)
    public void onIDontHaveAnAccountButtonClick(View v) {
        viewSwitcher.showNext();
    }

    @OnClick(R.id.vs_signup_button_exec_signup)
    public void onExecSignUpButtonClick(View v) {


        if (signup_signUpPasswordEt.getText().toString().equals("")||
                signup_patient_fname.getText().toString().equals("")||
                signup_patient_lname.getText().toString().equals("")||
                signup_signUpConfirmEt.getText().toString().equals("")||
                signup_signUpDoctorFnameEt.getText().toString().equals("")||
                signup_signUpDoctorLnameEt.getText().toString().equals("")||
                signup_signUpHCardNum.getText().toString().equals("")||
                signup_signUpPasswordEt.getText().toString().equals("")) {
            Toast.makeText(this, "Fields cannot be blank. Please check again.", Toast.LENGTH_LONG).show();
            return;
        }

        PatientSource src = CollectionsProvider.patients();
        if (signup_signUpPasswordEt.getText().toString().equals(signup_signUpConfirmEt.getText().toString())){

            progressDialog = ProgressDialog.show(this, "Please wait...", "Processing...", true);

            (fauth.createUserWithEmailAndPassword(signup_signUpEmailEt.getText().toString(), signup_signUpPasswordEt.getText().toString())).addOnCompleteListener( task -> {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show();
                    String uid = fauth.getUid();
                    cache.storeUserId(uid);

                    Doctor d = new Doctor(signup_signUpDoctorFnameEt.getText().toString(), signup_signUpDoctorLnameEt.getText().toString());
                    Appointment a = new Appointment(new Date(),"University Hospital",d,"", "");

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.YEAR,1);

                    Date finishDate = c.getTime();

                    Patient p = new Patient(
                            signup_patient_fname.getText().toString(),
                            signup_patient_lname.getText().toString(),
                            signup_signUpHCardNum.getText().toString(),
                            d,
                            new ArrayList<Appointment>(){{}},
                            new ArrayList<Summary>(){{}},
                            new ArrayList<Prescription>(){{}},
                            Faker.defaultInfo(),
                            finishDate);

                    src.createPatient(p,uid, task1 -> {
                        if (task1.isSuccessful()) {
                            cache.setLoggedIn(true);
                            Intent intent = new Intent(this, IntroActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, task1.getException().getMessage() != null ? task1.getException().getMessage() : "Error signing up, please try again or use demo.", Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    Toast.makeText(this, task.getException().getMessage() != null ? task.getException().getMessage() : "Error signing up, please try again or use demo." , Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Passwords not equal!", Toast.LENGTH_LONG).show();
            signup_signUpPasswordEt.getText().clear();
            signup_signUpConfirmEt.getText().clear();
        }

    }
    @OnClick(R.id.vs_login_button_exec_demo)
    public void onDemoButtonClick(View v) {
            cache.storeUserId(DEMO_UID);
            cache.setDemo(true);
            cache.setLoggedIn(true);
            if (cache.isFirstInstall()) {
                cache.setFirstInstall(false);
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
    }
    @OnClick(R.id.vs_login_button_exec_login)
    public void onExecLoginButtonClick(View v) {
        String loginEmail = login_emailLoginEt.getText().toString();
        String loginPass = login_passwordLoginEt.getText().toString();
        /** Basic Example Login to Explore the App **/

            if (loginEmail.equals("") ||
                    loginPass.equals("")) {
                Toast.makeText(this,"Invalid email or password",Toast.LENGTH_LONG).show();
                return;
            }
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait...", "Logging in...", true);
            (fauth.signInWithEmailAndPassword(login_emailLoginEt.getText().toString(), login_passwordLoginEt.getText().toString())).addOnCompleteListener(task -> {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    cache.storeUserId(fauth.getUid());
                    cache.setLoggedIn(true);
                    if (cache.isFirstInstall()) {
                        cache.setFirstInstall(false);
                        Intent intent = new Intent(this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(this, task.getException().getMessage() != null ? task.getException().getMessage() : "Error logging in, please try again or try demo.", Toast.LENGTH_LONG).show();
                }
            });

    }

    @OnClick(R.id.vs_signup_button_go_to_login)
    public void onGoToLoginFromSignUpClick(View v) {
        viewSwitcher.showPrevious();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
    }
}


