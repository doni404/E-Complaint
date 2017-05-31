package com.example.donisaurus.ecomplaint.util;

/**
 * Created by Donisaurus on 12/24/2016.
 */

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class MySnackBar {

    public static Snackbar init(View view, String msg) {
        Snackbar make = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        View sbView = make.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        return make;
    }
}
