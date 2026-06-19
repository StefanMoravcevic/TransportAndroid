package com.programdoo.transport.data.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.programdoo.transport.R;

public class LocationTrackingService extends Service {

    private static final String CHANNEL_ID = "location_channel";
    private static final int NOTIF_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(NOTIF_ID, createNotification());
        startLocationUpdates();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationUpdates() {

        LocationRequest request =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        30000
                ).build();

        fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult result) {

            if (result == null || result.getLocations() == null) return;

            for (Location location : result.getLocations()) {

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Log.d("LOCATION", lat + "," + lng);
            }
        }
    };

    @Override
    public void onDestroy() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    private Notification createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Tracking location")
                .setContentText("Service is running...")
                .setSmallIcon(R.drawable.icon_camera_small)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }
}