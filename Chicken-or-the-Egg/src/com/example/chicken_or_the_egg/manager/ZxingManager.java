package com.example.chicken_or_the_egg.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Date;

/**
 * Created by Konstantin on 16.06.2014.
 */
public class ZxingManager implements AppManagerInitializer {
    private final int WIDTH = 300;
    private final int HEIGHT = 300;

    @Override
    public void init(Context context) {

    }

    public Bitmap generateQRCode(String data) {
        long start = new Date().getTime();
        Bitmap mBitmap = null;
        Writer writer = new QRCodeWriter();
        String finaldata = Uri.encode(data);
        if (TextUtils.isEmpty(finaldata))
            return null;
        try {
            BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            mBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    mBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Log.e("content", finaldata);
        Log.e("finish", String.valueOf(new Date().getTime() - start));
        return mBitmap;

    }

}
