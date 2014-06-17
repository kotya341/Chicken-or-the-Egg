package com.example.chicken_or_the_egg.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.chicken_or_the_egg.R;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;
import com.example.chicken_or_the_egg.manager.App;

/**
 * Created by Konstantin on 16.06.2014.
 */
public class MainFragment extends BaseFragment {
    private String content;

    private MainActivityCallback mainActivityCallback;

    public static MainFragment newInstance(String qrCodeContent) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(String.class.getName(), qrCodeContent);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString(String.class.getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivityCallback = (MainActivityCallback) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQrCodeBitmap();
            }
        });
    }

    private void generateQrCodeBitmap() {
        sendBitmapToPrinter(App.zxingManager.generateQRCode(content));
    }


    private void sendBitmapToPrinter(Bitmap bitmap) {
        mainActivityCallback.onBitmapCreated(bitmap, content);
    }
}

