package com.robertsimoes.conscious.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.robertsimoes.conscious.BuildConfig;
import com.robertsimoes.conscious.Const;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.UserSettings;
import com.robertsimoes.conscious.ui.helpers.DialogFactory;
import com.robertsimoes.conscious.ui.helpers.SimpleTwoLineAdapterCreator;
import com.robertsimoes.conscious.utility.PINLoader;
import com.pressurelabs.swissarmyknife.GTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settings_footer_version)
    TextView footerVersion;

    @BindView(R.id.settings_pin_lock_enabled_indicator)
    TextView pinLockEnabled;

    @BindView(R.id.settings_seekbar_silence_millis)
    SeekBar silenceSeekbar;

    @BindView(R.id.settings_seekbar_input_millis)
    SeekBar inputSeekbar;

    private final String TAG = getClass().getName();
    private UserSettings settings = UserSettings.getInstance();
    private GTools logger = GTools.getInstance();
    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mSettingsItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            DialogFactory factory = new DialogFactory(SettingsActivity.this);
            switch (position) {
                case 0:
                    MaterialDialog dialog = factory.create(DialogFactory.DIALOG_NAME_CHANGE);
                    dialog.show();
                    return;
                case 1:
                    Intent loginActivity = new Intent(SettingsActivity.this,LoginActivity.class);
                    loginActivity.setAction(Const.ACTION_ENABLE_PIN_LOCK);
                    startActivityForResult(loginActivity,Const.RC.ENTER_PIN_LOCK);
                    return;
                case 2:
                    destroyPIN();
                    settings.getEditor(SettingsActivity.this).putBoolean(UserSettings.KEY_PIN_LOCK_ENABLED,false).commit();
                    refreshPinLockNotice();
                default:
                    return;
            }
            // Do something in response to the click
        }
    };

    private void destroyPIN() {
        PINLoader loader = new PINLoader(SettingsActivity.this);
        loader.destroy(getString(R.string.file_pin));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        constructSettingsList();
        displayFooter();
        constructSeekbars();
    }

    private void constructSeekbars() {
        int currentInputLength = settings.getPrefs(this).getInt(UserSettings.KEY_EXPRESS_MIN_INPUT_MILLIS,Const.INT_MIN_INPUT_MILLIS)/10000;
        int currentSilenceLength = settings.getPrefs(this).getInt(UserSettings.KEY_EXPRESS_SILENCE_MILLIS,Const.INT_MIN_SILENCE_MILLIS)/1000;
        inputSeekbar.setProgress(currentInputLength);
        inputSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int inputMillis =seekBar.getProgress()*10000;
                Log.d(TAG, "Input Length Pref Saved. " +inputMillis);
                settings.getEditor(SettingsActivity.this).putInt(UserSettings.KEY_EXPRESS_MIN_INPUT_MILLIS,inputMillis).commit();
                Toast.makeText(SettingsActivity.this, getString(R.string.toast_preferences_saved), Toast.LENGTH_SHORT).show();
            }
        });
        silenceSeekbar.setProgress(currentSilenceLength);
        silenceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int silenceMillis = seekBar.getProgress()*1000;
                Log.d(TAG, "Silence Length Pref Saved. " +silenceMillis);
                settings.getEditor(SettingsActivity.this).putInt(UserSettings.KEY_EXPRESS_SILENCE_MILLIS,silenceMillis).commit();
                Toast.makeText(SettingsActivity.this, getString(R.string.toast_preferences_saved), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPinLockNotice();
    }

    private void refreshPinLockNotice() {
        UserSettings settings = UserSettings.getInstance();
        if(settings.getPrefs(this).getBoolean(UserSettings.KEY_PIN_LOCK_ENABLED,false)) {
            pinLockEnabled.setText(getString(R.string.pinlock_enabled));
            pinLockEnabled.setTextColor(ContextCompat.getColor(this,R.color.accent));
        } else {
            pinLockEnabled.setText(getString(R.string.pinlock_disabled));
            pinLockEnabled.setTextColor(ContextCompat.getColor(this,R.color.color_dark_material_red));
        }
    }

    private void displayFooter() {
        footerVersion.setText("Conscious " + BuildConfig.VERSION_NAME);
    }

    private void constructSettingsList() {
        String[][] settingsArray = {
                {"Change Name", "The name current used in Conscious."},
                {"Enable PIN Lock", "Lock your thoughts with a PIN only you know. If you forget it you will not be able to access Conscious.\n"},
                {"Disable PIN Lock", "Remove PIN required to unlock Conscious"}
        };

        SimpleTwoLineAdapterCreator twoLine = new SimpleTwoLineAdapterCreator();

        ListView lv = (ListView) findViewById(R.id.settings_list_view);
        lv.setAdapter(twoLine.createAdapter(this, settingsArray));
        lv.setOnItemClickListener(mSettingsItemClicked);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Const.RC.ENTER_PIN_LOCK) {
            if (resultCode==RESULT_OK) {
                logger.showMessage(SettingsActivity.this, "PIN set. Don't forget it!");
            } else {
                logger.error(TAG, "PIN Failed to save..");
            }
        }
    }
}
