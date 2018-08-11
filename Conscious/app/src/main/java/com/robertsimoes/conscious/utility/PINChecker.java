package com.robertsimoes.conscious.utility;

import com.robertsimoes.conscious.ui.Verifiable;

/**
 * Copyright (c) 2017 Pressure Labs.
 */

public class PINChecker implements Verifiable<String> {
    private final byte[] secret;
    private boolean mIsCorrect;

    public PINChecker(byte[] secretValue) {
        this.secret = secretValue;
    }

    public boolean getResult() {
        return mIsCorrect;
    }

    private void setCorrectness(boolean mIsValid) {
        this.mIsCorrect = mIsValid;
    }

    @Override
    public void verify(String valueToCheck) {
        if(isEqual(valueToCheck)) {
            setCorrectness(true);
        } else {
            setCorrectness(false);
        }
    }

    // Uses
    private boolean isEqual(String valueToCheck) {
        SimpleSecurity securitize = SimpleSecurity.getInstance();
        String hashedCandidate = securitize.stringify256(securitize.sha256(valueToCheck)); // Convert byte[] hash to string
        String usrPin = securitize.stringify256(getSecret()); // convert byte[] hash to string
        return (hashedCandidate.equals(usrPin));
    }

    private byte[] getSecret() {
        return secret;
    }
}