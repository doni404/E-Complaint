package com.example.donisaurus.ecomplaint.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.donisaurus.ecomplaint.MyApplication;
import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.adapter.KomentarAdapter;
import com.example.donisaurus.ecomplaint.model.ModelComment;
import com.example.donisaurus.ecomplaint.model.ModelCommentResponse;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.model.ResponOnly;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.example.donisaurus.ecomplaint.util.MySnackBar;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

import timber.log.Timber;

public class KomentarActivity extends AppCompatActivity {

    public static String GET_IDPOST = "id_post";
    public static String GET_DISC = "disc";

    private RecyclerView mListTimeline;
    private LinearLayout mSendMessageLayout;
    private EditText mKomentar;
    private Button mBtnSend;
    private KomentarAdapter adapter;
    private TextView mUsername;
    private TextView mDisc;
    private RequestQueue queue;
    private RelativeLayout mRelativeLayout;
    private ModelUser user;

    private void assignViews() {
        mListTimeline = (RecyclerView) findViewById(R.id.listTimeline);
        mKomentar = (EditText) findViewById(R.id.komentar);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        adapter = new KomentarAdapter();
        queue = MyApplication.getInstance().getRequestQueue();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mUsername = (TextView) findViewById(R.id.username);
        mDisc = (TextView) findViewById(R.id.disc);
        user = DroidPrefs.get(this, HelperKey.USER_KEY, ModelUser.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //Set Back button on toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Comment");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Toast.makeText(getApplicationContext(), "Cancel Comment", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        assignViews();
        initList();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postKomentar();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        getKomentar();
    }

    private void initList() {
        mListTimeline.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mListTimeline.setLayoutManager(manager);
        mListTimeline.setAdapter(adapter);

        mUsername.setText(user.getNama());

        String disc = getIntent().getStringExtra(GET_DISC);
        mDisc.setText(disc);
    }


    private void getKomentar() {
        int idPost = getIntent().getIntExtra(GET_IDPOST, 0);
        String url = HelperUrl.GET_KOMENTAR + idPost;
        queue.getCache().invalidate(url, true);
        GsonRequest<ModelCommentResponse> request = new GsonRequest<>(
                url,
                ModelCommentResponse.class,
                null,
                new Response.Listener<ModelCommentResponse>() {
                    @Override
                    public void onResponse(ModelCommentResponse response) {
                        if (response.getStatus() == HelperStatus.SUKSES) {
                            mKomentar.setText("");
                            adapter.clear();
                            adapter.addKomentar(response.getData());
                        } else {
                            MySnackBar.init(mRelativeLayout, "Tidak dapat mengambil data").show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MySnackBar.init(mRelativeLayout, "Jaringan Bermasalah").show();
                    }
                }
        );
        MyApplication.getInstance().addToRequestQueue(request);
        //queue.add(request);
    }

    private void postKomentar() {
        ModelComment komentar = new ModelComment();
        Gson gson = new Gson();
        int idPost = getIntent().getIntExtra(GET_IDPOST, 0);
        Calendar cal = Calendar.getInstance();

        komentar.setIdPost(idPost);
        komentar.setIdUserKomentar(user.getIdUser());
        komentar.setKomentar(mKomentar.getText().toString());
        komentar.setTanggalKomentar(String.valueOf(cal.getTimeInMillis()));

        HashMap<String, String> params = new HashMap<>();
        params.put("jsondata", gson.toJson(komentar));

        GsonRequest<ResponOnly> request = new GsonRequest<>(
                Request.Method.POST,
                HelperUrl.POST_KOMENTAR,
                ResponOnly.class,
                null,
                params,
                new Response.Listener<ResponOnly>() {
                    @Override
                    public void onResponse(ResponOnly response) {
                        if (response.getStatus() == HelperStatus.SUKSES) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, 0);
                            getKomentar();
                            MySnackBar.init(mRelativeLayout, "Komentar berhasil di post").show();
                        } else {
                            MySnackBar.init(mRelativeLayout, "Tidak dapat mengirim komentar").show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.e(error.getMessage());
                        MySnackBar.init(mRelativeLayout, "Jaringan Bermasalah").show();
                    }
                }
        );
        MyApplication.getInstance().addToRequestQueue(request);
        //queue.add(request);
    }
}
