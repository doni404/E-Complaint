package com.example.donisaurus.ecomplaint.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donisaurus.ecomplaint.R;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Created by Donisaurus on 3/3/2017.
 */

public class PickLocationActivity extends AppCompatActivity implements MapView.OnMyLocationChangeListener {
    private MapView mMapview;
    private CardView mCardView;
    private RelativeLayout relativeLayout, mTlLokasi;
    private TextView mLocText;

    private int REQ_CODE_GPS = 1;
    private PermissionUtil.PermissionRequestObject mPermissionGps;

    private void assignViews() {
        mMapview = (MapView) findViewById(R.id.mapview);
        mCardView = (CardView) findViewById(R.id.card_view);
//        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//        mTlLokasi = (RelativeLayout) findViewById(R.id.tl_lokasi);
        mLocText = (TextView) findViewById(R.id.loc_text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pick_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Tentukan Lokasi</small>"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Toast.makeText(PickLocationActivity.this, "Batal Pilih Lokasi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        assignViews();
        initMap(savedInstanceState);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "pencet");

                LatLng latLng = mMapview.getCenterCoordinate();
                Bundle conData = new Bundle();
                conData.putDouble("latitude", latLng.getLatitude());
                conData.putDouble("longitude", latLng.getLongitude());
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        mMapview.setStyleUrl(Style.MAPBOX_STREETS);
        mMapview.setAccessToken(getString(R.string.mapbox_token));
        mMapview.onCreate(savedInstanceState);

        mMapview.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                mLocText.setText(point.getLatitude() + ", " + point.getLongitude());

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(point.getLatitude(), point.getLongitude())) // Sets the new camera position
                        .zoom(5) // Sets the zoom
                        .build(); // Creates a CameraPosition from the builder

                mMapview.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000, null);
            }
        });
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {

    }
}
