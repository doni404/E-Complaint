package com.example.donisaurus.ecomplaint.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.donisaurus.ecomplaint.MyApplication;
import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.model.ResponOnly;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.google.gson.Gson;

import java.util.HashMap;

import timber.log.Timber;

/**
 * Created by Donisaurus on 11/30/2016.
 */

public class RegisData extends AppCompatActivity{

    private EditText etUsername, etFullname, etDate, etGender, etAddress, etEmail, etNoHP, etPassword;
    private Button btnRegister;
    ModelUser regis;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_data);

        //Initialization
        etUsername = (EditText) findViewById(R.id.etUsername);
        etFullname = (EditText) findViewById(R.id.etFullname);
        etDate = (EditText) findViewById(R.id.etDate);
        etGender = (EditText) findViewById(R.id.etGender);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNoHP = (EditText) findViewById(R.id.etNoHP);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        //Set Back button on toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register Data");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Toast.makeText(getApplicationContext(), "Cancel Register", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegisData.this);
                progressDialog.setTitle("Registration");
                progressDialog.setMessage("Register your account ...");
                progressDialog.show();

                regis = new ModelUser();
                regis.setUsername(etUsername.getText().toString());
                regis.setNama(etFullname.getText().toString());
                regis.setTanggalLahir(etDate.getText().toString());
                regis.setJeniskelamin(etGender.getText().toString());
                regis.setAlamat(etAddress.getText().toString());
                regis.setEmail(etEmail.getText().toString());
                regis.setNoHp(etNoHP.getText().toString());
                regis.setPasword(etPassword.getText().toString());

                Gson gson = new Gson();

                HashMap<String, String> params = new HashMap<>();
                params.put("jsondata", gson.toJson(regis));

                GsonRequest<ResponOnly> request = new GsonRequest<>(
                        Request.Method.POST,
                        HelperUrl.REG_USER,
                        ResponOnly.class,
                        null,
                        params,
                        new Response.Listener<ResponOnly>() {
                            @Override
                            public void onResponse(ResponOnly response) {
                                if (response.getStatus() == HelperStatus.SUKSES) {
                                    Toast.makeText(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                                }

                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Timber.e(error.getMessage());
                                Toast.makeText(getApplicationContext(), "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                MyApplication.getInstance().addToRequestQueue(request);
            }
        });
    }
}
