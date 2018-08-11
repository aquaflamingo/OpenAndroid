package com.robertsimoes.conscious.ui.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.UserSettings;
import com.robertsimoes.conscious.ui.helpers.interfaces.IDialogFactory;

/**
 * Copyright (c) 2017 Pressure Labs.
 */

public class DialogFactory implements IDialogFactory {
    private final Activity c; // Dependency 2
    public final static int DIALOG_NAME_CHANGE = 0;
    public final static int DIALOG_OPT_OUT = 1;
    public final static int DIALOG_POTENTIAL_LOSS = 2;

    public DialogFactory(Activity c) {
        this.c=c;
    }

    private MaterialDialog CreateNameChange() {
        final UserSettings settings = UserSettings.getInstance();
        return new MaterialDialog.Builder(c)
                .title(R.string.dialog_name_change_title)
                .input(R.string.dialog_name_change_hint, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        settings.getEditor(c).putString(UserSettings.KEY_USER_NAME,input.toString()).commit();
                    }
                })
                .positiveText(R.string.dialog_name_change_positive)
                .build();
    }

    private MaterialDialog CreateOptOut() {
        final UserSettings settings = UserSettings.getInstance();
        return new MaterialDialog.Builder(c)
                .title(R.string.dialog_opt_out_title).content(R.string.dialog_opt_out_message)
                .positiveText(R.string.dialog_opt_out_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        settings.getEditor(c).putBoolean(UserSettings.KEY_OPT_IN,true).commit();
                        Toast.makeText(c, "Great thanks for opting in!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .negativeText(R.string.dialog_opt_out_negative)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        settings.getEditor(c).putBoolean(UserSettings.KEY_OPT_IN,false).commit();
                        Toast.makeText(c, "Okay, you've opted out!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .build();
    }

    private MaterialDialog CreatePotentialLoss() {

        return new MaterialDialog.Builder(c)
                .title(R.string.express_navigate_away_dialog_title)
                .content(R.string.express_navigate_away_dialog_message)
                .positiveText(R.string.express_navigate_away_dialog_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        c.finish();
                    }
                })
                .negativeText(R.string.express_navigate_away_dialog_negative)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .autoDismiss(true)
                .build();
    }


    @Override
    public MaterialDialog create(int dialogName) {
        switch (dialogName) {
            case DIALOG_NAME_CHANGE:
                return CreateNameChange();
            case DIALOG_OPT_OUT:
                return CreateOptOut();
            case DIALOG_POTENTIAL_LOSS:
                return CreatePotentialLoss();
            default:
                return null;
        }
    }
}
