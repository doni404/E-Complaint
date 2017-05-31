package com.example.donisaurus.ecomplaint.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.BitmapImageCache;
import com.android.volley.cache.plus.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private MyVolley() {
        // no instances
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, BitmapImageCache.getInstance(null));
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
