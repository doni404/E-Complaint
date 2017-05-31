package com.example.donisaurus.ecomplaint.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.example.donisaurus.ecomplaint.R;

/**
 * Created by Donisaurus on 11/30/2016.
 */

public class RegisCodeActivity extends AppCompatActivity {

    private TextView tvVerifCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_code);

        //Initialization
        tvVerifCode = (TextView)findViewById(R.id.tvVerifCode);

        //Set spanable color on textview
        Spannable wordtospan = new SpannableString("Masukkan kode verifikasi yang dikirim melalui SMS ke nomor telepon yang Anda masukan sebelumnya. Apabila Kode Verifikasi tidak terkirim silahkan tekan\n Kirim ulang kode");
        wordtospan.setSpan(new ForegroundColorSpan(Color.BLACK), 152, 168, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtospan.setSpan(new StyleSpan(Typeface.BOLD), 152, 168, 0);
        tvVerifCode.setText(wordtospan);
    }

}
