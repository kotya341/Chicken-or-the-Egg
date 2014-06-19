package com.example.chicken_or_the_egg.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.example.chicken_or_the_egg.R;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;
import com.example.chicken_or_the_egg.manager.App;
import com.example.chicken_or_the_egg.ui.fragment.KitKatPrintDialogFragment;
import com.example.chicken_or_the_egg.ui.fragment.MainFragment;
import com.example.chicken_or_the_egg.ui.fragment.PrintDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends BaseActivity implements MainActivityCallback {
    private final String START_SYMBOL = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        fragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(START_SYMBOL)).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents())) {
            startPrintFragment(App.zxingManager.generateQRCode(
                            newQrCodeContent(scanResult.getContents())),
                    scanResult.getContents());
        }
    }

    /**
     * generate next symbol in abc from current
     *
     * @param content current symbol
     * @return next symbol in abc
     */
    private String newQrCodeContent(String content) {
        int temp = (int) content.charAt(0);
        return Character.toString((char) ++temp);
    }

    @Override
    public void onBitmapCreated(Bitmap bitmap, String title) {
        startPrintFragment(bitmap, title);
    }

    /**
     * Start Print Fragment.
     * for android KitKat use PrintShopPrintDocumentAdapter and PrintManager
     * for older version use old approach with WebView dialog and js requests
     *
     * @param bitmap Bitmap to print in remote google print service
     * @param title  print title
     */
    private void startPrintFragment(Bitmap bitmap, String title) {
        if (bitmap == null) {
            Toast.makeText(this, "Content is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            fragmentManager.beginTransaction().replace(R.id.container, KitKatPrintDialogFragment.newInstance(bitmap)).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, PrintDialogFragment.newInstance(title, bitmap)).commit();
        }

    }

    @Override
    public void onPrintFinishing() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }


}
