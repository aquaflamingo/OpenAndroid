package com.hci.carebase.ui.fragments;

import com.hci.carebase.domain.Patient;

public interface MainActivityCallback {
    void onSave(Patient p);

    void onMoreInfoClick();

}
