package com.example.chicken_or_the_egg.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import com.example.chicken_or_the_egg.R;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;
import com.example.chicken_or_the_egg.manager.App;
import com.example.chicken_or_the_egg.ui.fragment.KitKatPrintDialogFragment;
import com.example.chicken_or_the_egg.ui.fragment.MainFragment;
import com.example.chicken_or_the_egg.ui.fragment.PrintDialogFragment;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends BaseActivity implements MainActivityCallback {
    private static final int REQUEST_CODE_SCAN = 846;

    private int scanCount = 0;
    private int OFFSET_ASCII_POSITION = 65;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        fragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(qrCodeContent())).commit();
        onPrintFinishing();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            try {
                startPrintFragment(App.zxingManager.encodeAsBitmap(scanResult.getContents()), scanResult.getContents());
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }


    private String qrCodeContent() {
        return Character.toString((char) (scanCount + OFFSET_ASCII_POSITION));
    }

    @Override
    public void onBitmapCreated(Bitmap bitmap, String title) {
        scanCount++;
        startPrintFragment(bitmap, title);
    }

    private void startPrintFragment(Bitmap bitmap, String title) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.KITKAT) {
            fragmentManager.beginTransaction().replace(R.id.container, KitKatPrintDialogFragment.newInstance(title, bitmap)).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, PrintDialogFragment.newInstance(title, bitmap)).commit();
        }

    }

    @Override
    public void onPrintFinishing() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


}