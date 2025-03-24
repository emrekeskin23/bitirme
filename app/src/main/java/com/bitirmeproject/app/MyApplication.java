package com.bitirmeproject.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyApplication extends Application {

    private static FirebaseFirestore db;

    public static FirebaseFirestore getFirestoreDB() {
        return db;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase first
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        // Then get Firestore instance
        db = FirebaseFirestore.getInstance();
    }

} 