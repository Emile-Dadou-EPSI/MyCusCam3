package com.example.mycuscam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycuscam.R;

public class ResultActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textView = findViewById(R.id.textView2);
        Intent intent =  getIntent();
        String  text  = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        String[] parts = text.split(":");
        System.out.println(text);
        //textView.setText(text);
        /*for (int i = 0; i < parts.length; i++)  {
            if (parts[i].contains(""))
        }*/
        textView.setText(parts.length);
    }
}
