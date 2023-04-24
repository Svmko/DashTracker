package com.example.dashtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    public void Confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Made by Sam Rouillard\nCIS 433\n4/23/2023");
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button trackerMenuBtn = (Button) findViewById(R.id.startTrackingBtn);
        Button entriesMenuBtn = (Button) findViewById(R.id.entriesBtn);
        Button optionsMenuBtn = (Button) findViewById(R.id.optionsBtn);
        ImageButton infoBtn = (ImageButton) findViewById(R.id.infoMenuBtn);

        trackerMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trackerMenuIntent = new Intent(getApplicationContext(), Tracker.class);
                startActivity(trackerMenuIntent);
            }
        });

        entriesMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entriesMenuIntent = new Intent(getApplicationContext(), Entries.class);
                startActivity(entriesMenuIntent);
            }
        });

        optionsMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionsMenuIntent = new Intent(getApplicationContext(), Options.class);
                startActivity(optionsMenuIntent);
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Confirm(); }
        });
    }
}