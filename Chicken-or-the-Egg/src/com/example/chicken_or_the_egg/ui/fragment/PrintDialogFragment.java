package com.example.chicken_or_the_egg.ui.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.chicken_or_the_egg.R;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by Konstantin on 16.06.2014.
 */
public class PrintDialogFragment extends BaseFragment {
    private static final String PRINT_DIALOG_URL = "https://www.google.com/cloudprint/dialog.html";
    private static final String JS_INTERFACE = "AndroidPrintDialog";
    private static final String CONTENT_TRANSFER_ENCODING = "base64";

    private static final String ZXING_URL = "http://zxing.appspot.com";
    private static final int ZXING_SCAN_REQUEST = 65743;

    /**
     * Post message that is sent by Print Dialog web page when the printing dialog
     * needs to be closed.
     */
    private static final String CLOSE_POST_MESSAGE_NAME = "cp-dialog-on-close";

    /**
     * Web view element to show the printing dialog in.
     */
    private WebView dialogWebView;

    private String title;
    private byte[] data;
    private MainActivityCallback mainActivityCallback;

    public static PrintDialogFragment newInstance(String title, Bitmap bitmap) {
        PrintDialogFragment printDialogFragment = new PrintDialogFragment();
        Bundle bundle = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bundle.putByteArray(Bitmap.class.getName(), byteArray);
        bundle.putString("title", title);
        printDialogFragment.setArguments(bundle);
        return printDialogFragment;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            data = getArguments().getByteArray(Bitmap.class.getName());
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivityCallback = (MainActivityCallback) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_print_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogWebView = (WebView) view.findViewById(R.id.webview);
        WebSettings settings = dialogWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        dialogWebView.setWebViewClient(new PrintDialogWebClient());
        dialogWebView.addJavascriptInterface(
                new PrintDialogJavaScriptInterface(), JS_INTERFACE);

        dialogWebView.loadUrl(PRINT_DIALOG_URL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ZXING_SCAN_REQUEST && resultCode == getActivity().RESULT_OK) {
            dialogWebView.loadUrl(intent.getStringExtra("SCAN_RESULT"));
        }
    }

    final class PrintDialogJavaScriptInterface {
        public String getType() {
            return "image/jpeg";
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return Base64.encodeToString(data, Base64.DEFAULT);
        }

        public String getEncoding() {
            return CONTENT_TRANSFER_ENCODING;
        }

        public void onPostMessage(String message) {
            if (message.startsWith(CLOSE_POST_MESSAGE_NAME)) {
                Log.e(PrintDialogFragment.class.getName(), message);
                mainActivityCallback.onPrintFinishing();
            }
        }
    }

    private final class PrintDialogWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(ZXING_URL)) {
                Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
                intentScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
                try {
                    startActivityForResult(intentScan, ZXING_SCAN_REQUEST);
                } catch (ActivityNotFoundException error) {
                    view.loadUrl(url);
                }
            } else {
                view.loadUrl(url);
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (PRINT_DIALOG_URL.equals(url)) {
                // Submit print document.
                view.loadUrl("javascript:printDialog.setPrintDocument(printDialog.createPrintDocument("
                        + "window." + JS_INTERFACE + ".getType(),window." + JS_INTERFACE + ".getTitle(),"
                        + "window." + JS_INTERFACE + ".getContent(),window." + JS_INTERFACE + ".getEncoding()))");

                // Add post messages listener.
                view.loadUrl("javascript:window.addEventListener('message',"
                        + "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)");
            }
        }
    }

}
