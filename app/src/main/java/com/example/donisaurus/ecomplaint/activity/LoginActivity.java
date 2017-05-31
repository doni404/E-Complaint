package com.example.donisaurus.ecomplaint.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.donisaurus.ecomplaint.model.ModelUserResponse;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.example.donisaurus.ecomplaint.util.MySnackBar;

import java.util.HashMap;
//import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin, btnRegister;
    ProgressDialog progressDialog;

    private void assignViews() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        //temp
        etLoginUsername.setText("guest1");
        etLoginPassword.setText("123");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialization
        assignViews();

        //Check session
        if (DroidPrefs.getBoolean(getApplicationContext(), HelperKey.LOGIN, false)) {
            Intent pindahMain = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(pindahMain);
            LoginActivity.this.finish();
        }
    }

    private void doLogin(final View v) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login E-Complaint");
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", etLoginUsername.getText().toString().toLowerCase());
        params.put("password", etLoginPassword.getText().toString().toLowerCase());

        //Use LoginService
//        RestClient.loginService.doLogin(params.get("username"), params.get("password")).enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                LoginResponse loginResponse = response.body();
//
//                Toast.makeText(getApplicationContext(), loginResponse.getPesan(), Toast.LENGTH_SHORT).show();
//
//                if (progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//
//                //Show Homepage if Login Success
//                if (loginResponse.getPesan().equalsIgnoreCase("sukses")){
//                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
//                    homeIntent.putExtra("user", loginResponse.getData().get(0));
//                    startActivity(homeIntent);
//                }else{
//                    Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//
//                if (progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//            }
//        });


        //Volley
        GsonRequest<ModelUserResponse> request = new GsonRequest<>(Request.Method.POST, HelperUrl.LOGIN_USER, ModelUserResponse.class, null,
                params, new Response.Listener<ModelUserResponse>() {
                    @Override
                    public void onResponse(ModelUserResponse response) {
                        if (response.getStatus() == HelperStatus.SUKSES) {
                            DroidPrefs.apply(getApplicationContext(), HelperKey.USER_KEY, response.getData().get(0));
                            DroidPrefs.apply(getApplicationContext(), HelperKey.LOGIN, true);

                            Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            homeIntent.putExtra("user", response.getData().get(0));
                            startActivity(homeIntent);

                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "User/Password Salah", Toast.LENGTH_SHORT).show();
                        }

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MySnackBar.init(v, "Jaringan bermasalah").show();
                    }
                }

        );

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                doLogin(v);
                break;
            case R.id.btnRegister:
                Intent register = new Intent(getApplicationContext(), RegisData.class);
                startActivity(register);
                break;
        }
    }
}
