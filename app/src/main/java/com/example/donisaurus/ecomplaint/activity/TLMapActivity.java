package com.example.donisaurus.ecomplaint.activity;

/**
 * Created by Donisaurus on 12/13/2016.
 */

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.bumptech.glide.Glide;
import com.example.donisaurus.ecomplaint.MyApplication;
import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.model.ModelPost;
import com.example.donisaurus.ecomplaint.model.ModelPostResponse;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.example.donisaurus.ecomplaint.util.MySnackBar;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.concurrent.ExecutionException;

import timber.log.Timber;

public class TLMapActivity extends AppCompatActivity implements MapView.OnMyLocationChangeListener {


    private MapView mMapview;
    //private RequestQueue mQeueu;
    private RelativeLayout relativeLayout;

    private int REQ_CODE_GPS = 1;
    private PermissionUtil.PermissionRequestObject mPermissionGps;

    private void assignViews() {
        mMapview = (MapView) findViewById(R.id.mapview);
        //mQeueu = MyVolley.getRequestQueue();
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tl_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignViews();
        initMap(savedInstanceState);
        getAllMarker();
    }

    private void initMap(Bundle savedInstanceState) {
        mMapview.setAccessToken(getString(R.string.mapbox_token));
        mMapview.setStyleUrl(Style.MAPBOX_STREETS);
        mMapview.onCreate(savedInstanceState);
        askPermission();
    }


    private void getAllMarker() {
        ModelUser user = DroidPrefs.get(this, HelperKey.USER_KEY, ModelUser.class);
        Drawable mIconDrawable = ContextCompat.getDrawable(this, R.drawable.map_marker);
        GsonRequest<ModelPostResponse> request = new GsonRequest<>(
                HelperUrl.GET_All + "/" + user.getIdUser(),
                ModelPostResponse.class,
                null,
                new Response.Listener<ModelPostResponse>() {
                    @Override
                    public void onResponse(ModelPostResponse response) {
                        if (response.getStatus() == HelperStatus.SUKSES) {
                            mMapview.removeAllAnnotations();
                            final IconFactory mIconFactory = IconFactory.getInstance(TLMapActivity.this);
                            for (final ModelPost post : response.getData()) {
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();

                                        Bitmap theBitmap = null;
                                        try {
                                            theBitmap = Glide.
                                                    with(TLMapActivity.this).
                                                    load(HelperUrl.URL_IMAGE + post.getGambar()).
                                                    asBitmap().
                                                    into(80, 80). // Width and height
                                                    get();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                        Icon icon = null;
                                        if (theBitmap == null) {
                                            icon = mIconFactory.defaultMarker();
                                        } else {
                                            icon = mIconFactory.fromBitmap(customBitmap(theBitmap, post.getKategori()));
                                        }
                                        mMapview.addMarker(new MarkerOptions()
                                                .position(new LatLng(post.getLatitude(), post.getLongitude()))
                                                .title(post.getNamaLokasi())
                                                .icon(icon)
                                                .snippet(post.getDiskripsi()));
                                    }
                                };

                                new Thread(runnable).start();

                            }

                        } else {
                            MySnackBar.init(relativeLayout, "Server bermasalah").show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.e(error.getMessage());
                        MySnackBar.init(relativeLayout, "Jaringan Bermasalah").show();
                    }
                }
        );
        MyApplication.getInstance().addToRequestQueue(request);
//        mQeueu.add(request);
    }

    // Gambar ke circle
    private Bitmap customBitmap(Bitmap bitmap, String category) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 4, w / 4);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);

        //Set stroke each category
        if (category.equalsIgnoreCase("Infrastruktur Kota")){
            p.setColor(Color.parseColor("#f1c40f"));
        }else if (category.equalsIgnoreCase("Pelayanan Publlik")){
            p.setColor(Color.parseColor("#3498db"));
        }else if (category.equalsIgnoreCase("Kebersihan Lingkungan")){
            p.setColor(Color.parseColor("#2ecc71"));
        }else {
            p.setColor(Color.WHITE);
        }

        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }

    private void askPermission() {
        mPermissionGps = PermissionUtil.with(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onAllGranted(new Func() {
                    @SuppressWarnings("ResourceType")
                    @Override
                    protected void call() {
                        mMapview.setMyLocationEnabled(true);
                        mMapview.setOnMyLocationChangeListener(TLMapActivity.this);
                        Location myLocation = mMapview.getMyLocation();
                        if (myLocation != null) {
                            mMapview.setLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                        }
                        mMapview.setZoom(11);
                    }
                })
                .onAnyDenied(new Func() {
                    @Override
                    protected void call() {
                        Toast.makeText(TLMapActivity.this, "Akses GPS ditolak", Toast.LENGTH_SHORT).show();
                    }
                }).ask(REQ_CODE_GPS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionGps.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapview.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapview.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapview.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapview.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean firsTimeMoveCam = true;

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null && firsTimeMoveCam) {
            mMapview.setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            mMapview.setZoom(11);
            firsTimeMoveCam = false;
        }
    }
}
