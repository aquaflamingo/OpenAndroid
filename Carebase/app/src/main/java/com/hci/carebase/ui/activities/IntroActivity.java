package com.hci.carebase.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hci.carebase.R;
import com.hci.carebase.data.datasource.LocalCacheDb;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.ui.MainActivity;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        PaperOnboardingPage p1 = new PaperOnboardingPage(
                getString(R.string.intro_title_slide1),
                getString(R.string.intro_content_slide1),
                Color.parseColor("#8A79FF"),
                R.drawable.drawable_patient_doctor,
                R.drawable.onboarding_pager_circle_icon
        );

        PaperOnboardingPage p2 = new PaperOnboardingPage(
                getString(R.string.intro_title_slide2),
                getString(R.string.intro_content_slide2),
                Color.parseColor("#53A0FD"),R.drawable.drawable_manage_rx,
                R.drawable.onboarding_pager_circle_icon
        );

        PaperOnboardingPage p3 = new PaperOnboardingPage(
                getString(R.string.intro_title_slide3),
                getString(R.string.intro_content_slide3),
                Color.parseColor("#21B895"),R.drawable.drawable_new_communication,
                R.drawable.onboarding_pager_circle_icon
        );

        PaperOnboardingPage p4 = new PaperOnboardingPage(
                getString(R.string.intro_title_slide4),
                getString(R.string.intro_content_slide4),
                Color.parseColor("#FEAC00"),R.drawable.drawable_new_emr,
                R.drawable.onboarding_pager_circle_icon
        );

        PaperOnboardingPage p5 = new PaperOnboardingPage(
                getString(R.string.intro_title_slide5),
                getString(R.string.intro_content_slide5),
                Color.parseColor("#FF0082"),R.drawable.ic_launcher_full,
                R.drawable.onboarding_pager_circle_icon
        );

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();

        elements.add(p1);
        elements.add(p2);
        elements.add(p3);
        elements.add(p4);
        elements.add(p5);

        PaperOnboardingFragment frg = PaperOnboardingFragment.newInstance(elements);

        FragmentTransaction trxn = getSupportFragmentManager().beginTransaction();
        trxn.add(R.id.intro_fragment_container,frg);
        trxn.commit();

        frg.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
