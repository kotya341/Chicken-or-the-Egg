package com.example.chicken_or_the_egg.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import com.example.chicken_or_the_egg.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Konstantin on 17.06.2014.
 */
public class KitKatPrintDialogFragment extends BaseFragment {
    private String title;
    private Bitmap bitmap;


    public static KitKatPrintDialogFragment newInstance(String title, Bitmap bitmap) {
        KitKatPrintDialogFragment kitKatPrintDialogFragment = new KitKatPrintDialogFragment();
        Bundle bundle = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bundle.putByteArray(Bitmap.class.getName(), byteArray);
        bundle.putString("title", title);
        kitKatPrintDialogFragment.setArguments(bundle);
        return kitKatPrintDialogFragment;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            byte[] data = getArguments().getByteArray(Bitmap.class.getName());
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            printPhoto();
        }

    }
    private void printPhoto() {
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap(title, bitmap);
    }

}
