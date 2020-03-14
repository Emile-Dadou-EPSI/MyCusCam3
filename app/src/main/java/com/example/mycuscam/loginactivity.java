package com.example.mycuscam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginactivity extends AppCompatActivity {

    CardView cardView;
    EditText usernameV;
    EditText passwordV;

    // Var for mail and password
    String email;
    String password;

    // For firebase auth
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);


        cardView = (CardView) findViewById(R.id.login2);
        usernameV = (EditText) findViewById(R.id.username);
        passwordV = (EditText) findViewById(R.id.passw);


// ...
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void changeActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
