package com.example.dashtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button backHomeBtn = (Button) findViewById(R.id.backHomeBtn);
        CheckBox formatCheck = (CheckBox) findViewById(R.id.formatCheck);
        TextView milesText = (TextView) findViewById(R.id.miles);
        TextView kilometersText = (TextView) findViewById(R.id.kilometers);

        if (formatCheck.isChecked()) {
            milesText.setTextColor(Color.parseColor("#EEEEFF"));
            kilometersText.setTextColor(Color.DKGRAY);
        } else {
            milesText.setTextColor(Color.DKGRAY);
            kilometersText.setTextColor(Color.parseColor("#EEEEFF"));
        }

        formatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    milesText.setTextColor(Color.parseColor("#EEEEFF"));
                    kilometersText.setTextColor(Color.DKGRAY);
                } else {
                    milesText.setTextColor(Color.DKGRAY);
                    kilometersText.setTextColor(Color.parseColor("#EEEEFF"));
                }
            }
        });

        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainMenuIntent);
            }
        });
    }
}