package com.example.mycuscam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimateScreen1Activity extends AppCompatActivity {

    // Hooks
    ImageView gosecu;
    CardView login;

    // ANimations
    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.animate_screen1);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Hooks
        gosecu = findViewById(R.id.img1);
        login = findViewById(R.id.login);

        // set animations
        gosecu.setAnimation(topAnimation);
        login.setAnimation(bottomAnimation);

        View.OnClickListener btnLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        };
        login.setOnClickListener(btnLogin);

    }

    private void changeActivity() {
        Intent i = new Intent(this, loginactivity.class);
        startActivity(i);
    }

    public void loginAction(View view) {
        changeActivity();
    }
}
