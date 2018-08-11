package com.robertsimoes.conscious.utility;

import android.content.Context;
import android.util.Log;

import com.robertsimoes.conscious.utility.interfaces.IOable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright (c) 2017 Pressure Labs.
 */

public class PINLoader implements IOable<byte[]> {

    private final String TAG = "PINGuru";
    private Context c;

    public PINLoader(Context c) {
        this.c = c;
    }
    /**
     * c.getString(R.string.file_pin)
     *
     * In this case the save Object is the hashed PIN
     * @param fileName the file to read
     * @param saveObject byte[] array object to save
     */
    @Override
    public void write(String fileName, byte[] saveObject) {
        FileOutputStream fos=null;
        try {
            fos= c.openFileOutput(fileName,Context.MODE_PRIVATE);
            fos.write(saveObject);
        } catch (IOException e) {
            Log.e(TAG, "Failed to save pin");
        } finally {
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException q) {
                    Log.e(TAG, "Failed to close.");
                }

            }
        }
    }

    @Override
    public byte[] read(String fileName) {
        FileInputStream fis=null;
        BufferedInputStream buf =null;
        File file = new File(c.getFilesDir(),fileName);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            fis= c.openFileInput(fileName);
            buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            return bytes;
        } catch (IOException e) {
            Log.e(TAG, "Failed to read pin");
        } finally {
            try {
                if(fis!=null) {
                    fis.close();
                }

                if (buf!=null) {
                    buf.close();
                }
            } catch (IOException q) {
                Log.e(TAG, "Failed to close.");
            }
        }
        return null;
    }

    @Override
    public boolean destroy(String fileName) {
        File fileToDestroy = new File(fileName);
        if (fileToDestroy!=null && fileToDestroy.exists()) {
            return fileToDestroy.delete();
        }

        return false;
    }
}
