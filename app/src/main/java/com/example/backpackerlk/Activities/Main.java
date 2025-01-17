package com.example.backpackerlk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.backpackerlk.R;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ConstraintLayout mainBtn=findViewById(R.id.mainBtn);
        mainBtn.setOnClickListener(view -> startActivity(new Intent(Main.this, Home.class)));

    }
}