package com.hci.carebase.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hci.carebase.R;
import com.hci.carebase.data.datasource.CollectionsProvider;
import com.hci.carebase.data.datasource.LocalCacheDb;
import com.hci.carebase.data.interfaces.PatientSource;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.ui.activities.IntroActivity;
import com.hci.carebase.ui.activities.LoginRegisterActivity;
import com.hci.carebase.ui.fragments.AppointmentsFragment;
import com.hci.carebase.ui.fragments.InboxFragment;
import com.hci.carebase.ui.fragments.ProfileFragment;
import com.hci.carebase.ui.fragments.MainActivityCallback;
import com.hci.carebase.ui.fragments.RxFragment;
import com.hci.carebase.util.Const;
import com.hci.carebase.util.Faker;

public class MainActivity extends AppCompatActivity implements MainActivityCallback {
    private Fragment selectedFragment = null;
    private Bundle fragmentArgs = null;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_inbox:
                    invalidateOptionsMenu();
                    selectedFragment = InboxFragment.newInstance(fragmentArgs);
                    currentFragment = Frag.INBOX;

                    break;
                case R.id.action_appointments:
                    invalidateOptionsMenu();
                    AppointmentsFragment f = AppointmentsFragment.newInstance(fragmentArgs);
                    f.setCallback(MainActivity.this);
                    selectedFragment = f;
                    currentFragment = Frag.APPOINTMENTS;

                    break;
                case R.id.action_rx:
                    invalidateOptionsMenu();
                    selectedFragment = RxFragment.newInstance(fragmentArgs);
                    currentFragment = Frag.RX;

                    break;
                case R.id.action_profile:
                    invalidateOptionsMenu();
                    selectedFragment = ProfileFragment.newInstance(fragmentArgs);
                    currentFragment = Frag.PROFILE_EDIT;

                    break;
            }

            refreshFragmentUI(selectedFragment);
            return true;
        }
    };


    @Override
    public void onSave(Patient p) {
                ILocalCache cache = new LocalCacheDb(MainActivity.this);
                String pId = cache.getUserId();
                if (!cache.isDemo()) {
                    src.updatePatient(pId, p, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Changes Saved!",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this,"Demo Application Cannot Update User!",Toast.LENGTH_SHORT).show();
                }

    }

    @Override
    public void onMoreInfoClick() {
        invalidateOptionsMenu();
        AppointmentsFragment f = AppointmentsFragment.newInstance(fragmentArgs);
        f.setCallback(MainActivity.this);
        selectedFragment = f;
        currentFragment = Frag.APPOINTMENTS;
        refreshFragmentUI(selectedFragment);
    }

    private enum Frag {INBOX, APPOINTMENTS,RX, PROFILE_EDIT, PROFILE_SAVE};
    private Frag currentFragment = Frag.INBOX;
    PatientSource src = CollectionsProvider.patients();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Set Toolbar
         */
        Toolbar myToolbar = (Toolbar) findViewById(R.id.general_fragments_toolbar);

        setSupportActionBar(myToolbar);

        ILocalCache cache = new LocalCacheDb(this);
        String userId = cache.getUserId();

        boolean isFirstInstall = cache.isFirstInstall();

        if (isFirstInstall) {
            cache.setFirstInstall(false);
            startActivity(new Intent(this, IntroActivity.class));
        }

        if (cache.isDemo()) {
            Patient p = Faker.fakePatient();
            fragmentArgs = new Bundle();
            fragmentArgs.putSerializable(Const.BUNDLE_KEY_PATIENT, p);

            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.main_bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

            selectedFragment = InboxFragment.newInstance(fragmentArgs);
            currentFragment = Frag.INBOX;
            refreshFragmentUI(selectedFragment);

        } else {

        /* Get Patient Info */
            src.getPatient(userId, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(MainActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                    Patient p = dataSnapshot.getValue(Patient.class);
                    fragmentArgs = new Bundle();
                    fragmentArgs.putSerializable(Const.BUNDLE_KEY_PATIENT, p);

                    BottomNavigationView bottomNavigationView = (BottomNavigationView)
                            findViewById(R.id.main_bottom_navigation);
                    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

                    selectedFragment = InboxFragment.newInstance(fragmentArgs);
                    currentFragment = Frag.INBOX;
                    refreshFragmentUI(selectedFragment);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ERR", "Could not load patient snapshot" + databaseError.getDetails());
                }
            });

        }
    }

    private void refreshFragmentUI(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.general_fragments_toolbar);


        switch (currentFragment) {

            case INBOX:
                getMenuInflater().inflate(R.menu.menu_fragment_inbox, menu);
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.color.colorPrimary));
                setSupportActionBar(myToolbar);
                break;
            case PROFILE_EDIT:
                getMenuInflater().inflate(R.menu.menu_fragment_profile, menu);
                menu.findItem(R.id.action_save).setVisible(false);
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.rect_gradient));
                setSupportActionBar(myToolbar);
                break;
            case PROFILE_SAVE:
                getMenuInflater().inflate(R.menu.menu_fragment_profile, menu);
                menu.findItem(R.id.action_edit).setVisible(false);
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.rect_gradient));
                setSupportActionBar(myToolbar);
                break;
            case APPOINTMENTS:
                getMenuInflater().inflate(R.menu.menu_fragment_appointments, menu);
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.color.colorPrimary));
                setSupportActionBar(myToolbar);
                break;
            case RX:
                getMenuInflater().inflate(R.menu.menu_fragment_rx, menu);
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.color.colorPrimary));
                setSupportActionBar(myToolbar);
                break;
            default:
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.color.colorPrimary));
                setSupportActionBar(myToolbar);
                getMenuInflater().inflate(R.menu.menu_fragment_inbox, menu);
                break;
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                currentFragment=Frag.PROFILE_SAVE;
                ((ProfileFragment) selectedFragment).onProfileEditClick();
                invalidateOptionsMenu();
                break;
            case R.id.action_save:
                currentFragment=Frag.PROFILE_EDIT;
                ((ProfileFragment) selectedFragment)
                        .onProfileSaveClick(this);
                invalidateOptionsMenu();

                break;
            case R.id.action_introduction:
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                break;
            case R.id.action_logout:
                ILocalCache c = new LocalCacheDb(this);
                c.setLoggedIn(false);
                if (c.isDemo()) c.setDemo(false);

                Toast.makeText(this,"Goodbye!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}



