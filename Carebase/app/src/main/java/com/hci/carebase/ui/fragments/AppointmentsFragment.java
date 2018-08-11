package com.hci.carebase.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hci.carebase.R;
import com.hci.carebase.data.datasource.CollectionsProvider;
import com.hci.carebase.data.datasource.LocalCacheDb;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.data.interfaces.PhotoSource;
import com.hci.carebase.domain.Appointment;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.util.Commons;
import com.hci.carebase.util.Const;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentsFragment extends Fragment {

    @BindView(R.id.vs_appointments_appts_pictures) ViewSwitcher vsMainAppointmentArea;
    @BindView(R.id.card_button_save_comments) Button saveComments;
    @BindView(R.id.card_et_patient_comments) EditText etPatientComments;
    @BindView(R.id.card_tv_appt_dr) TextView tvAppointmentDoctor;
    @BindView(R.id.card_tv_appt_hospital) TextView tvAppointmentHospital;
    @BindView(R.id.card_tv_appt_prep) TextView tvAppointmentPrep;
    @BindView(R.id.card_tv_appt_time) TextView tvAppointmentTime;
    @BindView(R.id.fab_appt_camera) FloatingActionButton fabPhoto;
    @BindView(R.id.fab_appt_make_note) FloatingActionButton fabMakeNote;
    @BindView(R.id.fab_appt_schedule) FloatingActionButton fabSchedule;
    @BindView(R.id.vs_et_appointment_notes) ViewSwitcher vsAppointmentNotes;
    @BindView(R.id.card_tv_patient_comments) TextView tvPatientComments;
    @BindView(R.id.fab_menu_appts) FloatingActionsMenu fabMenu;
    @BindView(R.id.appts_ll_no_appts_available) LinearLayout noApptsLinearLayout;
    @BindView(R.id.card_img_patient_attachment) ImageView imgViewPatientAttachment;

    private ProgressDialog dlg;

    private enum CommentState {EDIT, SHOW};
    private CommentState state = CommentState.SHOW;
    private  Patient p;
    private Appointment currentAppointment;
    private MainActivityCallback callback;

    public void setCallback(MainActivityCallback callback) {
        this.callback = callback;
    }

    public static AppointmentsFragment newInstance(Bundle args) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        if (args!=null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        ButterKnife.bind(this, view);


        if (getArguments()==null) {
            noApptsLinearLayout.setVisibility(View.VISIBLE);
            vsMainAppointmentArea.setVisibility(View.GONE);
        } else {
            p = (Patient) getArguments().getSerializable(Const.BUNDLE_KEY_PATIENT);
            if (p.getAppointments() == null || p.getAppointments().isEmpty() || p.getAppointments().get(0)==null) {
                noApptsLinearLayout.setVisibility(View.VISIBLE);
                vsMainAppointmentArea.setVisibility(View.GONE);
            } else{

                currentAppointment = p.getAppointments().get(Commons.getMostUpcomingAppointmentIndex(p));

                refreshUI(currentAppointment);

                dlg = new ProgressDialog(getContext());
            }
        }

        return view;
    }

    private void refreshUI(Appointment mostUpcomingAppointment) {
        SimpleDateFormat f = new SimpleDateFormat("EEE, MMM d, yyyy");
        String date = f.format(mostUpcomingAppointment.getDateFor());
        String hospital = mostUpcomingAppointment.getHospitalLocation()=="" ? "N/A please contact physician.":mostUpcomingAppointment.getHospitalLocation();
        String doctor = mostUpcomingAppointment.getDoctor().getFirstName() +" "+mostUpcomingAppointment.getDoctor().getLastName();
        String prep =mostUpcomingAppointment.getPreparations()==""? "N/A" : mostUpcomingAppointment.getPreparations();
        String comments = mostUpcomingAppointment.getPatientComments()=="" ? "N/A" : mostUpcomingAppointment.getPatientComments();

        tvAppointmentDoctor.setText(doctor);
        tvAppointmentPrep.setText(prep);
        tvAppointmentHospital.setText(hospital);
        tvAppointmentTime.setText(date);
        tvPatientComments.setText(comments);

        if(mostUpcomingAppointment!=null ) {
            ILocalCache cache = new LocalCacheDb(getContext());
            if (!cache.isDemo()) {
                PhotoSource s = CollectionsProvider.photos();
                s.getAppointmentPhoto(cache.getUserId(), mostUpcomingAppointment.getDateFor(),
                        new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext())
                                        .load(uri)
                                        .into(imgViewPatientAttachment);
                            }
                        });
            }
        }

    }


    @OnClick(R.id.fab_appt_make_note)
    public void onFABMakeNoteClicked(View v) {
        if(state == CommentState.SHOW) {
            state = CommentState.EDIT;
            vsAppointmentNotes.showNext();
            fabMenu.collapse();
        }

    }

    @OnClick(R.id.card_button_save_comments)
    public void onSaveCommentClicked(View v) {
        if (callback!=null) {
            currentAppointment.setPatientComments(etPatientComments.getText().toString());
            int indexUpcoming = Commons.getMostUpcomingAppointmentIndex(p);
            p.getAppointments().set(indexUpcoming, currentAppointment);
            callback.onSave(p);
            refreshUI(currentAppointment);
            state = CommentState.SHOW;
        }

        vsAppointmentNotes.showPrevious();
        fabMenu.collapse();

    }

    @OnClick(R.id.card_button_cancel_edit_comments)
    public void onCancelCommentEditClicked(View v) {

        vsAppointmentNotes.showPrevious();

    }
    @OnClick(R.id.fab_appt_camera)
    public void onFABCameraClicked(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // this is the capturing of the immage and stores in media store
        startActivityForResult(intent,Const.REQUEST_CODE_CAPTURE_IMAGE_ACTIVITY);

        Toast.makeText(getContext(), "Add Photo to Your Comments.", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.fab_appt_schedule)
    public void onFABScheduleClicked(View v) {
        String subject = "Appointment Booking Request for Dr." +p.getDoctor().getLastName();
        String body = "Dear Dr. " + p.getDoctor().getLastName() +
                ", \n I would like to book an upcoming appointment on <DATE-TO-BOOK> in order " +
                "to discuss <DISCUSSION-TOPIC>. " +
                "\n\n Are there any appointments available for <DATE-TO-BOOK>? " +
                "\n\n Regards, " + p.getFirstName();
        String email = p.getDoctor().getEmail()==null ? "":p.getDoctor().getEmail();
        Commons.sendFeedback(getContext(),email,subject,body);
        Toast.makeText(getContext(), "Opening Email Client..", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQUEST_CODE_CAPTURE_IMAGE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Uri uri = Commons.saveTmpAndGetUri(getContext(),bmp);

                // convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);




                imgViewPatientAttachment.setImageBitmap(bitmap);

                PhotoSource s = CollectionsProvider.photos();
                ILocalCache cache = new LocalCacheDb(getContext());
                if (!cache.isDemo()) {
                    String userId = cache.getUserId();
                    s.uploadAppointmentPhoto(userId, uri, currentAppointment.getDateFor(),  taskSnapshot -> {

                        currentAppointment.setHasImageAttachment(true);

                        int indexUpcoming = Commons.getMostUpcomingAppointmentIndex(p);
                        p.getAppointments().set(indexUpcoming, currentAppointment);

                        callback.onSave(p);

                        Toast.makeText(getContext(),"Saved image.",Toast.LENGTH_SHORT).show();
                    });
                } else {
                    callback.onSave(p);
                }

            }
        }
    }

}
