package com.example.donisaurus.ecomplaint.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.donisaurus.ecomplaint.MyApplication;
import com.example.donisaurus.ecomplaint.core.Point;
import com.example.donisaurus.ecomplaint.core.Polygon;
import com.example.donisaurus.ecomplaint.model.ModelUpload;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.example.donisaurus.ecomplaint.util.MyVolley;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.example.donisaurus.ecomplaint.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Donisaurus on 11/30/2016.
 */

public class PostingActivity extends AppCompatActivity {

    private static final int REQ_CODE_GPS = 1;
    public static int REQUEST_PHOTO = 123;
    public static int REQ_CAP_PHOTO = 1235;
    public static int REQUEST_ALBUM = 124;
    public static int REQUEST_LOCATION = 125;
    public static String MODE_REQUEST = "request";

    private String directoryPath;
    private String namePhoto;

    private Toolbar postToolbar;
    private Button btnPost;
    //private CircleImageView ivProfile;
    private TextView tvUsername;
    private TextView tvDatePic;
    private ImageView ivUpload;
    private TextView mLocText;
    private Spinner etCategory;
    private EditText etTitle, etLatitude, etLongitude, etDescription;
    private Button btnChangeLocation;

    private String mFilepath;
    private LatLng mDestination;
    private Location mLocExif;

    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude
    private boolean letGPSUpdate = true;
    private LocationManager locationManager;
    private GPSListener gpsListener;
    private PermissionUtil.PermissionRequestObject mPermissionGps;

    private ModelUser data_user;
    private ProgressDialog progressDialog;

    private RequestQueue queue;

    private void assignViews(){
        //ivProfile = (CircleImageView)findViewById(R.id.ivProfile);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvDatePic = (TextView)findViewById(R.id.tvDatePic);
        ivUpload = (ImageView)findViewById(R.id.ivUpload);
        etCategory = (Spinner)findViewById(R.id.etCategory);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etLatitude = (EditText)findViewById(R.id.etLatitude);
        etLongitude = (EditText)findViewById(R.id.etLongitude);
        etDescription = (EditText)findViewById(R.id.etDescription);
        btnChangeLocation = (Button)findViewById(R.id.btnChangeLocation);
        mLocText = (TextView)findViewById(R.id.mLocText);
        postToolbar = (Toolbar) findViewById(R.id.postToolbar);
        btnPost = (Button)findViewById(R.id.btnPost);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        MyVolley.init(getApplicationContext());
        //Initialization
        assignViews();

        //Get data active user
        data_user = DroidPrefs.get(this, HelperKey.USER_KEY, ModelUser.class);

        directoryPath = Environment.getExternalStorageDirectory() + "/ecomplaint";
        File folder = new File(directoryPath);
        boolean success = true;
        if (!folder.exists()){
            success = folder.mkdir();
        }

        Timber.d("Status folder ->>" + success);
        gpsListener = new GPSListener();
        mDestination = new LatLng(0, 0);
        askPermission();

        /* Set DrawerLayout on Toolbar */
        //Set Drawer button on toolbar
        setSupportActionBar(postToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Posting</small>"));

        postToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Toast.makeText(getApplicationContext(), "Cancel Posting", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        //button upload clicked
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float lat = Float.valueOf(etLatitude.getText().toString());
                float lon = Float.valueOf(etLongitude.getText().toString());

                //Create Polygon of Each Region
                Polygon.initialize();

                //Get Data File
                File fileExif = new File(mFilepath);

                //Checking Position with Polygon Geofencing
                Log.d("region", "aaa");
                boolean found = false;
                String area = "";
                for (int i = 0; i < Polygon.regionList.size(); i++){
                    Log.d("region", Polygon.regionList.get(i).getName());
                    if (isInside(Polygon.regionList.get(i).getPolygon(), new Point(lat, lon))){
                        area = Polygon.regionList.get(i).getName();
                        Toast.makeText(getApplicationContext(), "Area : " + area, Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }

                if (!found){
                    Toast.makeText(getApplicationContext(), "Lokasi belum terdaftar", Toast.LENGTH_SHORT).show();
                }

                //Upload data to server
                uploadPostingan(area);
            }
        });

        //button change location pick
        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickLocation = new Intent(getApplicationContext(), PickLocationActivity.class);
                startActivityForResult(pickLocation, REQUEST_LOCATION);
//                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askPermission() {
        mPermissionGps = PermissionUtil.with(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onAllGranted(new Func() {
                    @Override
                    protected void call() {
                        getRequest();
                    }
                })
                .onAnyDenied(new Func() {
                    @Override
                    protected void call() {
                        Toast.makeText(PostingActivity.this, "Akses GPS ditolak", Toast.LENGTH_SHORT).show();
                        letGPSUpdate = false;
                        onBackPressed();
                    }
                }).ask(REQ_CODE_GPS);
    }

    private void getRequest() {
        int codeReq = getIntent().getIntExtra(MODE_REQUEST, 0);

        if (codeReq == REQUEST_PHOTO) {
            getPhoto();
        } else if (codeReq == REQUEST_ALBUM) {
            getFromGallery();
        }
    }

    private void getPhoto() {
        Calendar cal = Calendar.getInstance();
        namePhoto = "ecomplaint" + cal.getTimeInMillis();
        Timber.d("Name photo ->" + namePhoto);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/ecomplaint", namePhoto + ".jpg");
        Uri photoPath = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
        startActivityForResult(intent, REQ_CAP_PHOTO);
    }

    private void getFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CAP_PHOTO) {
            String dataPath = directoryPath + "/" + namePhoto + ".jpg";

            //Get Location From Exif on Picture and set to form
            mLocExif = readExifImage(dataPath);
            if (mLocExif.getLatitude() != 0.0){
                etLatitude.setText(mLocExif.getLatitude()+"");
                etLongitude.setText(mLocExif.getLongitude()+"");
                mDestination = new LatLng(mLocExif.getLatitude(), mLocExif.getLongitude());
                Toast.makeText(getApplicationContext(), "Berhasil membaca lokasi foto", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Tidak terdapat Exif Location pada foto", Toast.LENGTH_SHORT).show();
            }

            Timber.d("Lokasi File ->" + dataPath);

            Bitmap bitmap = BitmapFactory.decodeFile(dataPath);
            bitmap = resizeImage(bitmap, 800);
            compresImage(bitmap, dataPath);

            this.mFilepath = dataPath;
            ivUpload.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_ALBUM) {
            Uri imageUri = data.getData();

            File src = new File(getRealPathFromURI(imageUri));
            Calendar cal = Calendar.getInstance();
            namePhoto = "ecomplaint" + cal.getTimeInMillis();
            Timber.d("Name photo ->" + namePhoto);
            File dest = new File(Environment.getExternalStorageDirectory() + "/ecomplaint", namePhoto + ".jpg");

            //Get Location From Exif on Picture and set to form
            mLocExif = readExifImage(src.getPath());
            if (mLocExif.getLatitude() != 0.0){
                etLatitude.setText(mLocExif.getLatitude()+"");
                etLongitude.setText(mLocExif.getLongitude()+"");
                mDestination = new LatLng(mLocExif.getLatitude(), mLocExif.getLongitude());
                Toast.makeText(getApplicationContext(), "Berhasil membaca lokasi foto", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Tidak terdapat Exif Location pada foto", Toast.LENGTH_SHORT).show();
            }

            try {
                copy(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
                Timber.e("gagal copy coy");
            }

            Bitmap bitmap = BitmapFactory.decodeFile(dest.getPath());
            bitmap = resizeImage(bitmap, 800);
            compresImage(bitmap, dest.getPath());

            this.mFilepath = dest.getPath();

            ivUpload.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_LOCATION) {
//            letGPSUpdate = false;
//            locationManager.removeUpdates(gpsListener);
            Bundle extras = data.getExtras();
//            String latlng = String.format("(%.6f,%.6f)", extras.getDouble("latitude"), extras.getDouble("longitude"));
            etLatitude.setText(extras.getDouble("latitude")+"");
            etLongitude.setText(extras.getDouble("longitude")+"");
//            mDestination = new LatLng(extras.getDouble("latitude"), extras.getDouble("longitude"));

            Log.d("message", "success");
        } else {
            this.finish();
        }

        //Set capturing date
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c.getTime());

        tvUsername.setText(data_user.getNama());
        tvDatePic.setText(formattedDate);
    }

    private void compresImage(Bitmap bitmap, String dataPath) {
        try {
            FileOutputStream stream = new FileOutputStream(dataPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
            stream.flush();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeImage(Bitmap bitmap, int newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if (width > height) {
            newWidth = newSize;
            newHeight = (newSize * height) / width;
        } else if (width < height) {
            newHeight = newSize;
            newWidth = (newSize * width) / height;
        } else if (width == height) {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String name = cursor.getString(column_index);
        cursor.close();
        return name;
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public Location readExifImage(String path){
        Location loc = new Location("");

        try {
            ExifInterface exif = new ExifInterface(path);
            float[] latlong = new float[2];

            if (exif.getLatLong(latlong)) {
                loc.setLatitude(latlong[0]);
                loc.setLongitude(latlong[1]);
            }
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }

        return loc;
    }

    private void uploadPostingan(String area) {
        progressDialog = new ProgressDialog(PostingActivity.this);
        progressDialog.setTitle("Upload to server");
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();

        int kategori = 0;
        if (etCategory.getSelectedItem().equals("Infrastruktur Kota")){
            kategori = 1;
        }else if (etCategory.getSelectedItem().equals("Pelayanan Publik")){
            kategori = 2;
        }else if (etCategory.getSelectedItem().equals("Kebersihan Lingkungan")){
            kategori = 3;
        }

        int areaid = 0;
        if (area.equalsIgnoreCase("lowokwaru")) areaid = 1;
        else if (area.equalsIgnoreCase("klojen")) areaid = 2;
        else if (area.equalsIgnoreCase("blimbing")) areaid = 3;
        else if (area.equalsIgnoreCase("kedungkandang")) areaid = 4;
        else if (area.equalsIgnoreCase("sukun")) areaid = 5;

        String path_image = directoryPath + "/" + namePhoto + ".jpg";

        Calendar dateNow = Calendar.getInstance();

        ModelUpload post = new ModelUpload();
        post.setIdUser(data_user.getIdUser());
        post.setKategori(kategori);
        post.setAreaid(areaid);
        post.setWaktu(String.valueOf(dateNow.getTimeInMillis()));
        post.setDiskripsi(etDescription.getText().toString());
        post.setNamaLokasi(etTitle.getText().toString());
        post.setLatitude(mDestination.getLatitude());
        post.setLongitude(mDestination.getLongitude());

        Gson gson = new Gson();

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, HelperUrl.ADD_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("status") == HelperStatus.SUKSES) {
                                Toast.makeText(PostingActivity.this, "Upload Berhasil", Toast.LENGTH_SHORT).show();
                                PostingActivity.this.finish();
                            }

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addMultipartParam("jsondata", "text/plain", gson.toJson(post));
        smr.addFile("image", path_image);
        MyApplication.getInstance().addToRequestQueue(smr);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private static boolean isInside(Polygon polygon, Point point){
        boolean contains = polygon.contains(point);
        return contains;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (letGPSUpdate) {
//            initGPS();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (letGPSUpdate) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (locationManager != null) {
//                locationManager.removeUpdates(gpsListener);
            }
        }
    }

//    private void initGPS() {
////        gpsHelper = new GPSHelper(this);
//
//        Timber.d("GPS jalan");
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //noinspection ResourceType (sudah dihandle)
//        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location != null) {
//            String latlng = String.format("(%.6f,%.6f)", location.getLatitude(), location.getLongitude());
//            mLocText.setText(latlng);
//            mDestination = new LatLng(location.getLatitude(), location.getLongitude());
//        }
//
//        //noinspection ResourceType (sudah dihandle)
//        locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                GPSHelper.MIN_TIME_BW_UPDATES,
//                GPSHelper.MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//        Timber.d("Network", "Network");
//
//        //noinspection ResourceType (sudah dihandle)
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                GPSHelper.MIN_TIME_BW_UPDATES,
//                GPSHelper.MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//        Timber.d("GPS Enabled", "GPS Enabled");
//    }
//
//
    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Timber.d(location.toString());
            String latlng = String.format("(%.6f,%.6f)", location.getLatitude(), location.getLongitude());
            mLocText.setText(latlng);
            mDestination = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Timber.d(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Timber.d(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Timber.d(provider);

        }
    }
}
