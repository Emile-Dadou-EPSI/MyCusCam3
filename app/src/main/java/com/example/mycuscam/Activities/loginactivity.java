package com.example.mycuscam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mycuscam.R;
import com.example.mycuscam.Visu.loadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginactivity extends AppCompatActivity {

    CardView cardView;
    EditText usernameV;
    EditText passwordV;

    // For firebase auth
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        mAuth = FirebaseAuth.getInstance();

        cardView = (CardView) findViewById(R.id.login2);
        usernameV = (EditText) findViewById(R.id.username);
        passwordV = (EditText) findViewById(R.id.passw);



        View.OnClickListener log1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final loadingDialog loadingDialog = new loadingDialog(loginactivity.this, true);
                loadingDialog.startLoadingDialog();
                    mAuth.signInWithEmailAndPassword(usernameV.getText().toString().trim(), passwordV.getText().toString().trim()).addOnCompleteListener(loginactivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismissDialog();
                                changeActivity();
                            }
                            else {
                                loadingDialog.dismissDialog();
                                task.getResult();
                                Toast.makeText(loginactivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                }
            };
        cardView.setOnClickListener(log1);
        }

    private void changeActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
