package com.example.donisaurus.ecomplaint.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.activity.RegisData;

/**
 * Created by Donisaurus on 11/29/2016.
 */

public class RegisNumActivity extends AppCompatActivity {

    private TextView tvPhoneNumber;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_number);

        //Initialization
        tvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);
        btnNext = (Button)findViewById(R.id.btnNext);

        //Set Back button on toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("REGISTER");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Toast.makeText(getApplicationContext(), "Cancel Register", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        //Set spanable color on textview
        Spannable wordtospan = new SpannableString("Dibutuhkan verifikasi nomor telepon untuk membuat akun TESS baru. Dengan menekan tombol NEXT berarti Anda telah menyetujui Syarat dan Ketentuan yang berlaku.");
        wordtospan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTitle)), 123, 143, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtospan.setSpan(new StyleSpan(Typeface.BOLD), 123, 143, 0);
        tvPhoneNumber.setText(wordtospan);

        //Set onclik btnNext
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), RegisData.class);
                startActivity(register);
            }
        });
    }
}
