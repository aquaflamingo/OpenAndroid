package com.hci.carebase.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.hci.carebase.domain.Appointment;
import com.hci.carebase.domain.Patient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Commons {

    public static void sendFeedback(
            Context callingActivity,
            String emailAddress,
            String subject,
            String message) {

        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT,  message);

        try {
            callingActivity.startActivity(email);
        } catch(ActivityNotFoundException ex) {
            try {
                /* If no mailing client, send to playstore */
                final String appPackageName = callingActivity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    callingActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    /*Fails if GPlay Not Installed */
                    callingActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            } catch (Exception e) {
                Toast.makeText(callingActivity, "Can't send feedback at this time", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static Uri saveTmpAndGetUri(Context c, Bitmap bitmap) {
        File tmp = c.getDir("tmp", Context.MODE_PRIVATE);
        // Create imageDir
        String aptTmp = "apptmp.jpg";

        File path=new File(tmp,aptTmp);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Uri.fromFile(path);

    }

    public static int getMostUpcomingAppointmentIndex(Patient p) {
        ArrayList<Appointment> appointments = p.getAppointments();
        Date smallest = appointments.get(0).getDateFor();
        int index =0;
        for(int i=0; i<appointments.size(); i++ ){
            if (appointments.get(i).getDateFor().before(smallest)) {
                index = i;
                smallest = appointments.get(i).getDateFor();
            }
        }
        return index;
    }
}
