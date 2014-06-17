package com.example.chicken_or_the_egg.manager;

import android.app.Application;

/**
 * Created by Konstantin on 16.06.2014.
 */
public class App extends Application {
    public static final ZxingManager zxingManager = new ZxingManager();

    @Override
    public void onCreate() {
        super.onCreate();
        initManagers();
    }

    private void initManagers() {
        zxingManager.init(this);
    }
}
