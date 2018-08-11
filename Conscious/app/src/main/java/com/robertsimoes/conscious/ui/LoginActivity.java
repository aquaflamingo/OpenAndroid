package com.robertsimoes.conscious.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.robertsimoes.conscious.Const;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.utility.PINChecker;
import com.robertsimoes.conscious.utility.PINLoader;
import com.robertsimoes.conscious.utility.SimpleSecurity;
import com.robertsimoes.conscious.UserSettings;
import com.pressurelabs.swissarmyknife.GTools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.pin_lock_view)
    PinLockView mPinLockView;

    @BindView(R.id.pin_lock_indicator_dots)
    IndicatorDots mIndicatorDots;

    @BindView(R.id.tv_login_subhead)
    TextView tvLoginSubhead;

    private UserSettings settings = UserSettings.getInstance();
    private String TAG = getClass().getName();
    private PinLockListener mPinLockListener;
    private MaterialDialog currentDialog;
    private boolean isChangingOrEnablingPin = false;
    private PinLockListener genPinListener() {
        return new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                GTools logger = GTools.getInstance();
                PINLoader guru = new PINLoader(LoginActivity.this);
                String pinFile = getString(R.string.file_pin);

                // If the pin is null or the intent was to change or enable pin
                if(guru.read(pinFile)==null || isChangingOrEnablingPin) {
                    logger.log(TAG, "Changing or no PIN");



                    secureCredentials(pin);

                    if (!isChangingOrEnablingPin) {
                        logger.showMessage(LoginActivity.this, "PIN saved..");
                        settings.getEditor(LoginActivity.this).putBoolean(UserSettings.KEY_PIN_LOCK_ENABLED,true).commit();
                        goToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, "Re-enter Pin", Toast.LENGTH_SHORT).show();
                        beginSecondEntryValidate(pin);

                    }
                } else {
                    if(isPINCorrect(pin)) {
                        goToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onEmpty() {
                GTools.getInstance().showMessage(LoginActivity.this,"Incorrect Pin..");
                Log.d("LOGIN", "Pin empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.d("LOGIN", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
            }
        };
    }

    private void beginSecondEntryValidate(final String userPinEntered) {
        final SimpleSecurity securitize = SimpleSecurity.getInstance();

        final PINChecker checker = new PINChecker(securitize.sha256(userPinEntered));
        // hash the pin user gave for confirmation later

        mPinLockView.resetPinLockView();
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                checker.verify(pin);
                // Verify with the checker if PIN is correct
                if (checker.getResult()) {
                    // If the user entered correct pin twice secure the credentials
                    secureCredentials(pin);
                    setResult(RESULT_OK);
                    settings.getEditor(LoginActivity.this).putBoolean(UserSettings.KEY_PIN_LOCK_ENABLED,true).commit();
                        // Only when pin is right second time do we enable pin lock
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Failed try again.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }

    /**
     * Determines if hashed pin is same as stored PIN
     * @param pin
     */
    private boolean isPINCorrect(String pin) {
        GTools logger = GTools.getInstance();
        logger.log(TAG, "Validating PIN..");
        PINLoader guru = new PINLoader(this);
        PINChecker checker = new PINChecker(guru.read(getString(R.string.file_pin))); // Read PIN into validator
        checker.verify(pin);
        return checker.getResult();
    }


    /**
     * Uses securatize to hash and save pin to file storage
     * @param pin
     */
    private void secureCredentials(String pin) {
        GTools logger = GTools.getInstance();

        SimpleSecurity secure = SimpleSecurity.getInstance();
        PINLoader guru = new PINLoader(this);

        byte[] hashedPin= null;
        try {
            hashedPin=secure.sha256(pin);
            guru.write(getString(R.string.file_pin),hashedPin);
        } catch (Exception e) {
            logger.error(TAG, "No such algorithm for securing credentials...");
        }
    }




    private void goToMain() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        GTools.getInstance().debugging(true);

        boolean firstInstall = settings.getPrefs(this).getBoolean(UserSettings.KEY_FIRST_INSTALL,true);
        isChangingOrEnablingPin = getIntent().getAction().equals(Const.ACTION_ENABLE_PIN_LOCK);
        boolean pinLockEnabled = settings.getPrefs(this).getBoolean(UserSettings.KEY_PIN_LOCK_ENABLED,false);

        if (!firstInstall) {
            if(pinLockEnabled || isChangingOrEnablingPin) {
               displayPinLock();

            } else {
                displayNoPinLock();
                runDelayedMainIntent(1000);
            }
        } else {
            settings.getEditor(this).putBoolean(UserSettings.KEY_FIRST_INSTALL,false).commit();
            Intent onBoardIntent = new Intent(LoginActivity.this,OnBoardingActivity.class);
            onBoardIntent.setAction(Const.ACTION_ONBOARD);
            startActivityForResult(onBoardIntent,Const.RC.ON_BOARD);
            displayNoPinLock();
        }
    }

    /**
     * Sets up the Pin Lock views and prevents user from access app without the appropriate PIN
     */
    private void displayPinLock() {
        mPinLockListener = genPinListener();
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL);
    }

    /**
     * Removes the PIN lock and access blockage from the screen
     */
    private void displayNoPinLock() {
        mPinLockView.setVisibility(View.GONE);
        mIndicatorDots.setVisibility(View.GONE);

        tvLoginSubhead.setText(getString(R.string.welcome));
    }

    /**
     * Sends an intent to the Main Activity after 1 second delay
     */
    private void runDelayedMainIntent(int milisecs) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                if (LoginActivity.this != null) {
                    LoginActivity.this.startActivity(mainIntent);
                    LoginActivity.this.finish();
                }
            }
        }, milisecs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Const.RC.ON_BOARD) {
            runDelayedMainIntent(1000);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // If user double back presses, verify if settings activity was waiting for a
        // result and if so set the status and return.
        if(getIntent()!=null && getIntent().getAction().equals(Const.ACTION_ENABLE_PIN_LOCK)) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}

