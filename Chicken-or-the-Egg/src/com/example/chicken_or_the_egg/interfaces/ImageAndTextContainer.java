package com.example.chicken_or_the_egg.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Konstantin on 17.06.2014.
 */
public interface ImageAndTextContainer {
    /** Called by PrintShopPrintDocumentAdapter when need to get bitmap content for printing */
    public Bitmap getImage();
}
