package com.example.donisaurus.ecomplaint.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.fragment.FragTimeline;
import com.example.donisaurus.ecomplaint.model.ModelPost;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperUrl;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donisaurus on 11/30/2016.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    private Button btnUpload;
    private Toolbar homeToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchText;

    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath;

    private ModelUser data_user;

    private void assignViews(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawLayout);
        homeToolbar = (Toolbar) findViewById(R.id.homeToolbar);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        searchText = (SearchView)findViewById(R.id.src_text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialization
        assignViews();

        //Get data active user
        data_user = DroidPrefs.get(this, HelperKey.USER_KEY, ModelUser.class);

        Toast.makeText(getApplicationContext(), "user : " + data_user.getNama() + " active", Toast.LENGTH_SHORT).show();

        //Check state
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FragTimeline timeline = new FragTimeline();
            fragmentTransaction.add(R.id.container, timeline);
            fragmentTransaction.commit();
        }

        /* Set DrawerLayout on Toolbar */
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>E-Complaint Timeline</small>"));

        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, homeToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        //Set Naviagtion View
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView username = (TextView)headerView.findViewById(R.id.tvUsername);
        TextView email = (TextView)headerView.findViewById(R.id.tvEmail);

        username.setText(data_user.getUsername());
        email.setText(data_user.getEmail());

        navigationView.setNavigationItemSelectedListener(this);

        //Search Function checked when text changed
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toString().toLowerCase();

                final List<ModelPost> filteredList = new ArrayList<>();

                for (int i = 0; i < FragTimeline.listPosting.size(); i++){
                    String postName = FragTimeline.listPosting.get(i).getDiskripsi().toLowerCase();

                    if (postName.contains(newText)){
                        filteredList.add(FragTimeline.listPosting.get(i));
                    }
                }

                HelperUrl.FILTERED = true;
                FragTimeline.listPosting = filteredList;
                Bundle bundle = new Bundle();
                bundle.putInt(FragTimeline.TAG_URL, HelperUrl.TAG_FILTER);
                replaceFragment(Fragment.instantiate(getApplicationContext(), FragTimeline.class.getName(), bundle));
                return false;
            }
        });

        //Set Swipe Refresh Layout
        swipeRefreshLayout.setOnRefreshListener(this);

        //button upload clicked
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] item = {"Ambil dari album", "Ambil foto"};
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Pilih pengambilan Foto")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent createPost = new Intent(getApplicationContext(), PostingActivity.class);
                                    createPost.putExtra(PostingActivity.MODE_REQUEST, PostingActivity.REQUEST_ALBUM);
                                    createPost.putExtra("user", data_user);
                                    startActivity(createPost);

                                } else if (which == 1) {
                                    Intent createReport = new Intent(getApplicationContext(), PostingActivity.class);
                                    createReport.putExtra(PostingActivity.MODE_REQUEST, PostingActivity.REQUEST_PHOTO);
                                    createReport.putExtra("user", data_user);
                                    startActivity(createReport);
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle bundle = new Bundle();

        switch(id) {
            case R.id.nav_news:
                bundle = new Bundle();
                bundle.putInt(FragTimeline.TAG_URL, HelperUrl.TAG_ALL);
                replaceFragment(Fragment.instantiate(this, FragTimeline.class.getName(), bundle));
                break;
            case R.id.nav_populer:
                bundle = new Bundle();
                bundle.putInt(FragTimeline.TAG_URL, HelperUrl.TAG_MOSTLIKE);
                replaceFragment(Fragment.instantiate(this, FragTimeline.class.getName(), bundle));
                break;
//            case R.id.nav_mix:
//                bundle = new Bundle();
//                bundle.putInt(FragTimeline.TAG_URL, HelperUrl.TAG_ACAK);
//                replaceFragment(Fragment.instantiate(this, FragTimeline.class.getName(), bundle));
//                break;
            case R.id.nav_maps:
                Intent maps = new Intent(getApplicationContext(), TLMapActivity.class);
                startActivity(maps);
                Toast.makeText(getApplicationContext(), "Maps", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                DroidPrefs.apply(getApplicationContext(), HelperKey.LOGIN, false);
                Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        Bundle bundle = new Bundle();
        bundle = new Bundle();
        bundle.putInt(FragTimeline.TAG_URL, HelperUrl.TAG_ALL);
        replaceFragment(Fragment.instantiate(this, FragTimeline.class.getName(), bundle));
    }
}
