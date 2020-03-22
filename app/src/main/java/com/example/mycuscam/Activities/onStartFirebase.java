package com.example.mycuscam.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycuscam.R;
import com.google.firebase.FirebaseApp;

public class onStartFirebase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_start_firebase);
        Context sContext = getApplicationContext();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(sContext);
        startActivity(new Intent(this,AnimateScreen1Activity.class));
    }
}
