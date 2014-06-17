package com.example.chicken_or_the_egg.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import com.example.chicken_or_the_egg.interfaces.ImageAndTextContainer;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;
import com.example.chicken_or_the_egg.ui.adapter.PrintShopPrintDocumentAdapter;

import java.io.ByteArrayOutputStream;

/**
 * Created by Konstantin on 17.06.2014.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class KitKatPrintDialogFragment extends BaseFragment implements ImageAndTextContainer {
    private String title;
    private Bitmap bitmap;
    private MainActivityCallback mainActivityCallback;

    final ImageAndTextContainer imageAndTextContainer = this;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivityCallback = (MainActivityCallback) activity;
    }

    private void printPhoto() {
        PrintShopPrintDocumentAdapter adapter = new PrintShopPrintDocumentAdapter(imageAndTextContainer, getActivity());
        // Get the print manager from the context
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        // And print the document
        printManager.print("PrintShop", adapter, null);
    }

    @Override
    public String getText() {
        return title;
    }

    @Override
    public Bitmap getImage() {
        return bitmap;
    }
}
