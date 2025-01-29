package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;

public class Main extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //variables
    Animation topAnim ,bottomAnim;
    ImageView image;
    TextView logo, slogen;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //animationss
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.loadingImg);
        logo = findViewById(R.id.logoTxt);
        slogen = findViewById(R.id.sloganTxt);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogen.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main.this, WhoAreYou.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}