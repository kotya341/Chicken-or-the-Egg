package com.example.chicken_or_the_egg.ui.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import com.example.chicken_or_the_egg.interfaces.ImageAndTextContainer;
import com.example.chicken_or_the_egg.interfaces.MainActivityCallback;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Konstantin on 17.06.2014.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PrintShopPrintDocumentAdapter extends PrintDocumentAdapter {

    private ImageAndTextContainer imageAndTextContainer;
    private Context context;
    private PrintedPdfDocument pdfDocument;
    private MainActivityCallback mainActivityCallback;

    public PrintShopPrintDocumentAdapter(ImageAndTextContainer container, Activity activity) {
        imageAndTextContainer = container;
        context = activity;
        mainActivityCallback = (MainActivityCallback) activity;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, final LayoutResultCallback callback, Bundle extras) {

        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                callback.onLayoutCancelled();
            }
        });

        pdfDocument = new PrintedPdfDocument(context, newAttributes);
        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("qr_code.png")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1)
                .build();

        callback.onLayoutFinished(info, false);

    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {

        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {

            @Override
            public void onCancel() {
                pdfDocument.close();
                pdfDocument = null;
                callback.onWriteCancelled();
            }
        });

        PdfDocument.Page page = pdfDocument.startPage(0);

        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(imageAndTextContainer.getImage(), 0, 0, new Paint());

        pdfDocument.finishPage(page);

        try {
            pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close();
            pdfDocument = null;
        }

        callback.onWriteFinished(pages);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        mainActivityCallback.onPrintFinishing();
    }

}

