package com.example.dashtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Entries extends AppCompatActivity {
    Activity activity = this;
    Context context = this;

    private ArrayList entryID, entryDate, entryDistance;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarbuttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem thing) {
        switch(thing.getItemId()) {
            case R.id.homeBtn:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.trackerBtn:
                startActivity(new Intent(this, Tracker.class));
                return true;
            case R.id.entriesBtn:
                return true;
        }
        return super.onOptionsItemSelected(thing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryview);
        RecyclerView rv = findViewById(R.id.rvEntires);

        entryID = new ArrayList<>();
        entryDate = new ArrayList<>();
        entryDistance = new ArrayList<>();

        SQLClass sqlClass = new SQLClass(Entries.this);
        Cursor c = sqlClass.pullFromDB();

        if (c.getCount() == 0) {
            //make no data notification
        } else {
            while (c.moveToNext()) {
                entryID.add(c.getString(0));
                entryDate.add(c.getString(1));
                entryDistance.add(c.getString(2));
                System.out.println(c.getString(0));
                System.out.println(c.getString(1));
                System.out.println(c.getString(2));
            }
        }

        RecyclerAdapter ra = new RecyclerAdapter(entryID, entryDate, entryDistance, context, activity);
        rv.setAdapter(ra);
        rv.setLayoutManager(new LinearLayoutManager(context));
    }
}

