package com.example.dashtracker;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.content.pm.PackageManager;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
public class Tracker extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private Location previousLocation;
    private float totalDistance;
    public static final String FINAL_DATE = "final_date";
    //public static final String SP = "sp";
    private String date;
    private float kilometers;
    private double miles;
    SQLClass sql = new SQLClass(this);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarbuttons, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem thing) {
        switch (thing.getItemId()) {
            case R.id.homeBtn:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.trackerBtn:
                return true;
            case R.id.entriesBtn:
                startActivity(new Intent(this, Entries.class));
                return true;
        }
        return super.onOptionsItemSelected(thing);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        Button stBtn = (Button) findViewById(R.id.stBtn);
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        TextView dateView = (TextView) findViewById(R.id.dateView);
        //EditText editText = (EditText) findViewById(R.id.editTextNumber);
        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        stopBtn.setEnabled(false);
        saveBtn.setEnabled(false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        previousLocation = null;
        totalDistance = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Tracker.this, "My Notification")
                .setSmallIcon(R.drawable.tracker)
                .setContentTitle("DashTracker")
                .setContentText("Distance is currently being recorded!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Open in background."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Tracker.this);

        stBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                float getTotalDistance = sp.getFloat("totalDistance", totalDistance);
                stBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                boolean bool = sp.getBoolean("checkState", false);

                managerCompat.notify(1, builder.build());
                if (ActivityCompat.checkSelfPermission(Tracker.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Tracker.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //ActivityCompat.requestPermissions();
                    return;
                }
                fusedLocationClient.requestLocationUpdates(
                        getLocationRequest(),
                        locationCallback,
                        Looper.getMainLooper()
                );
                //distanceTextView.setText(String.format("%.2f meters", totalDistance));

                if(bool == false) {
                    TextView distanceTextView = findViewById(R.id.maindistanceView);
                    kilometers = getTotalDistance/1000;
                    distanceTextView.setText(String.format("%.2f km", kilometers));
                } else {
                    TextView distanceTextView = findViewById(R.id.maindistanceView);
                    miles = getTotalDistance*0.000621371192;
                    distanceTextView.setText(String.format("%.2f m", miles));
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopBtn.setEnabled(false);
                saveBtn.setEnabled(true);

                managerCompat.cancelAll();
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Tracker.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int moY, int doM) {
                                dateView.setText((moY + 1) + "-" + doM + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();

                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bool = sp.getBoolean("checkState", false);
                saveBtn.setEnabled(false);
                stBtn.setEnabled(true);

                date = dateView.getText().toString();
                edit.putString(FINAL_DATE, date);
                edit.apply();

                if(bool == false) {
                    sql.addToDB(date, String.format("%.2f km", kilometers));
                } else {
                    sql.addToDB(date, String.format("%.2f m", miles));
                }
                Toast.makeText(getApplicationContext(), "Saved entry!", Toast.LENGTH_SHORT).show();

                miles = 0;
                kilometers = 0;
                edit.putFloat("totalDistance", 0);
                edit.commit();
            }
        });
    }

    private LocationRequest getLocationRequest() {
        return LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location currentLocation = locationResult.getLastLocation();
            if (previousLocation != null) {
                float distance = previousLocation.distanceTo(currentLocation);
                totalDistance += distance;
            }
            previousLocation = currentLocation;

            SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();

            edit.putFloat("totalDistance", totalDistance);
            edit.commit();
            //TextView distanceTextView = findViewById(R.id.maindistanceView);
            //distanceTextView.setText(String.format("%.2f meters", totalDistance));
        }
    };
}