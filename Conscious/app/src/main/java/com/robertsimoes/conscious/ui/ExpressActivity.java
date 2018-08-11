/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.Const;

import com.robertsimoes.conscious.data.stores.local.ThoughtSource;
import com.robertsimoes.conscious.UserSettings;
import com.robertsimoes.conscious.ui.helpers.DialogFactory;
import com.pressurelabs.swissarmyknife.GTools;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpressActivity extends AppCompatActivity {

    @BindView(R.id.et_express_thought_body)
    EditText etBody;

    @BindView(R.id.et_express_thought_title)
    EditText etTitle;

    @BindView(R.id.tv_express_chars_remaining)
    TextView tvCharsRem;

    private int colorRed=0;
    private int colorWhite=0;


    private final String TAG =  getClass().getName();
    private MaterialDialog currentDialog;
    private UserSettings settings = UserSettings.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_express);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        etBody.getBackground().clearColorFilter();
        etTitle.getBackground().clearColorFilter();

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        colorRed=ContextCompat.getColor(ExpressActivity.this,R.color.color_material_red);
        colorWhite=ContextCompat.getColor(ExpressActivity.this,R.color.white);

        etBody.addTextChangedListener(liveWatcher);

        parseAction(getIntent().getAction());
    }

    private void parseAction(String action) {
        if (action.equals(Const.ACTION_EXPRESS)) {
            GTools.getInstance().log(TAG, "Express");
        } else if(action.equals(Const.ACTION_SPEAK)) {
            GTools.getInstance().log(TAG, "Speak");
            try {

            } catch (ActivityNotFoundException e) {
                Log.e("ERR", "ERR "+e.getMessage());
            }
            int minInputMillis=settings.getPrefs(this).getInt(UserSettings.KEY_EXPRESS_MIN_INPUT_MILLIS,Const.INT_MIN_INPUT_MILLIS);
            int minSilenceMillis = settings.getPrefs(this).getInt(UserSettings.KEY_EXPRESS_SILENCE_MILLIS,Const.INT_MIN_SILENCE_MILLIS);
           speakYourMind(minInputMillis,minSilenceMillis);
        }
    }

    private void speakYourMind(int inputMillis, int silenceMillis) {
        Intent speak = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speak.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speak.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your mind.");
        speak.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, silenceMillis);
        speak.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, inputMillis);
        startActivityForResult(speak, Const.RC.SPEECH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_express_activity, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {

            case R.id.action_expression_speak:
                parseAction(Const.ACTION_SPEAK);
                return true;

            case android.R.id.home:
                potentialLossDialog();
                return true;

            case R.id.action_commit:
                if (etTitle!=null && etBody!=null) {
                    commitThought(etTitle.getText().toString(), etBody.getText().toString());
                } else {
                    Toast.makeText(ExpressActivity.this, getString(R.string.default_failed_action),Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * There are no drafts. If the user navigates away then provide
     * a dialog to let them know they will lose it
     */
    private void potentialLossDialog() {
        if (etTitle!=null && !etTitle.getText().toString().equals("")
                || etBody!=null && !etBody.getText().toString().equals("")) {

            currentDialog = new DialogFactory(this).create(DialogFactory.DIALOG_POTENTIAL_LOSS);
            currentDialog.show();

        }else {
            finish();
        }
    }


    /**
     * Parses the users input of a thought and commits to the database
     *
     * @param title title of the thought
     * @param body body of the thought
     */
    private void commitThought(final String title, final String body) {

        if (isFirstCommit() && (!title.equals("") || !body.equals(""))) {
            settings.getEditor(this).putBoolean(UserSettings.KEY_FIRST_COMMIT, false).commit();

            currentDialog = createCommitWarning(title,body);

            currentDialog.show();
        }

         /* Blank Title and Body == error  */
        else if (title.equals("")&&body.equals("")) {
                Toast.makeText(ExpressActivity.this,getString(R.string.express_commit_failed_both_invalid),Toast.LENGTH_SHORT).show();

        }

        else {
            /* Both are non Empty so commit */
            commitDb(title,body);
            finish();
        }
    }

    private MaterialDialog createCommitWarning(final String title, final String body) {
            return new MaterialDialog.Builder(ExpressActivity.this)
                    .title(R.string.express_first_commit_dialog_title)
                    .content(R.string.express_first_commit_dialog_msg)
                    .positiveText(R.string.action_commit)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            commitDb(title, body);
                            finish();
                        }
                    })
                    .negativeText(R.string.dialog_response_nevermind)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).build();
        }

    /**
     * Commits the thought provided with the title, body and unique time stamp to
     * the database
     * @param title title of the thought to commit
     * @param body the message or body entered
     */
    private void commitDb(String title, String body) {

        ThoughtSource src=null;
        try {
            src = new ThoughtSource(ExpressActivity.this);
            src.open();
            src.think(
                    new Timestamp(Calendar.getInstance().getTimeInMillis()).toString(),
                    title,
                    body
                    );
        } catch (Exception e) {
            Toast.makeText(ExpressActivity.this,getString(R.string.default_failed_action),Toast.LENGTH_SHORT).show();
        }

        if (src!=null && src.isOpen()) {
            src.close();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.RC.SPEECH && resultCode == RESULT_OK) {
            final ArrayList<String> voiceInput = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String s = voiceInput.get(0);
            if (etTitle.hasFocus()) {
                etTitle.append(" "+s);
            } else {
                etBody.append(" "+s);
            }
            // I use this ArrayList to create a dialog.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentDialog!=null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    /**
     *
     * @return true if the first time user has ever committed
     */
    public boolean isFirstCommit() {
        return settings.getPrefs(this).getBoolean(UserSettings.KEY_FIRST_COMMIT,true);
    }


    /**
     * Live reminder of limit on text
     */
    private final TextWatcher liveWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvCharsRem.setText(String.valueOf(Const.INT_CHAR_LIMIT-s.length()));

            if ((Const.INT_CHAR_LIMIT - s.length()) < 50) {
                tvCharsRem.setTextColor(colorRed);
            } else {
                tvCharsRem.setTextColor(colorWhite);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
