package com.example.donisaurus.ecomplaint.util;

/**
 * Created by Donisaurus on 12/19/2016.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import timber.log.Timber;

public class GPSHelper extends Service {

    private final Context mContext;

    // flag untuk status gps
    boolean isGPSEnabled = false;

    // flag jika g dapet lokasi
    boolean canGetLocation = false;



    // Jarak minimum untuk update GPS
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 5 meter

    // Waktu untuk update Realtime GPS
    public static final long MIN_TIME_BW_UPDATES = 0; // 1 menite


    protected LocationManager locationManager;

    public GPSHelper(Context context) {
        this.mContext = context;
    }

    public LocationManager getLocationManager() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // cek gps status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                this.canGetLocation = true;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locationManager;
    }

    /**
     * Fungsi untuk cek GPS aktif/tidak
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Alert untuk menampilkan jika gps gak aktif
     * Jika setting maka aku masuk ke menbu settng
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS tidak aktif");
        alertDialog.setMessage("GPS belum dinyalakan, Apakah anda ingin pergi ke pengaturan?");
        alertDialog.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
