package com.example.chicken_or_the_egg.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chicken_or_the_egg.R;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;
import com.example.chicken_or_the_egg.manager.App;
import com.google.zxing.WriterException;

/**
 * Created by Konstantin on 16.06.2014.
 */
public class MainFragment extends BaseFragment {
    private TextView textContent;
    private Button button;
    private ImageView imageView;
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
        textContent = (TextView) view.findViewById(R.id.txt_qr_code_content);
        imageView = (ImageView) view.findViewById(R.id.image_qr_code);
        imageView.setVisibility(View.GONE);
        button = (Button) view.findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQrCode();
            }
        });
    }

    private void generateQrCode() {
        try {
            Bitmap bitmap = App.zxingManager.encodeAsBitmap(content);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            button.setVisibility(View.GONE);
            sendBitmapToPrinter(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void sendBitmapToPrinter(Bitmap bitmap) {
        mainActivityCallback.onBitmapCreated(bitmap, content);
    }
}

