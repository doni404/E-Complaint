package com.example.donisaurus.ecomplaint.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import timber.log.Timber;

public class DroidPrefs {

    public static boolean contains(Context context, String key) {
        return getDefaultInstance(context).contains(key);
    }

    public static <T> T get(Context context, String key, Class<T> cls) {
        return getDefaultInstance(context).get(key, cls);
    }
    /**
     * Digunakan untuk mengambil value yang bertipe object
     * */
    public static <T> T get(Context context, String key, Type type) {
        return getDefaultInstance(context).get(key, type);
    }

    /**
     * Digunakan untuk mengambil value yang bertipe String
     * */
    public static String getString(Context context, String key, String defaultValue) {
        return getDefaultInstance(context).get(key, defaultValue);
    }

    /**
     * Digunakan untuk mengambil value yang bertipe boolean
     * */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getDefaultInstance(context).get(key, defaultValue);
    }

    /**
     * Digunakan untuk mengambil value yang bertipe int
     * */
    public static int getInt(Context context, String key, int defaultValue) {
        return getDefaultInstance(context).get(key, defaultValue);
    }

    /**
     * Digunakan untuk menyimpan value yang bertipe object
     * */
    public static void apply(Context context, String key, Object value) {
        getDefaultInstance(context).apply(key, value);
    }
    /**
     * Digunakan untuk menyimpan value yang bertipe boolean
     * */
    public static void apply(Context context, String key, boolean value) {
        getDefaultInstance(context).apply(key, value);
    }
    /**
     * Digunakan untuk menyimpan value yang bertipe String
     * */
    public static void apply(Context context, String key, String value) {
        getDefaultInstance(context).apply(key, value);
    }

    /**
     * Digunakan untuk menyimpan value yang bertipe int
     * */
    public static void apply(Context context, String key, int value) {
        getDefaultInstance(context).apply(key, value);
    }

    /**
     * Digunakan untuk menghapus semua simpanan
     * */
    public static void clearAll(Context context) {
        getDefaultInstance(context).clearAll();
    }

    private static DroidPrefs getDefaultInstance(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return new DroidPrefs(sp);
    }

    private Gson mGson;

    private SharedPreferences mSharedPrefs;

    public DroidPrefs(SharedPreferences sharedPrefs) {
        mGson = new Gson();
        mSharedPrefs = sharedPrefs;
    }

    public boolean contains(String key) {
        return mSharedPrefs.contains(key);
    }

    public <T> T get(String key, Class<T> cls) {
        if (contains(key)) {
            return mGson.fromJson(mSharedPrefs.getString(key, null), cls);
        }

//        try {
//            return cls.newInstance();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("class must have an empty constructor");
//        }
        Timber.e("Item not found");
        return null;
    }

    public <T> T get(String key, Type type) {
        if (contains(key)) {
            Timber.d(key + " Load data ->> " + mSharedPrefs.getString(key, null));
            return mGson.fromJson(mSharedPrefs.getString(key, null), type);
        }

        return null;
    }

    public String get(String key, String defaultValue) {

        Timber.d(key + " Load data ->> " + mSharedPrefs.getString(key, defaultValue));
        return mSharedPrefs.getString(key, defaultValue);

    }

    public int get(String key, int defaultValue) {

        Timber.d(key + " Load data ->> " + mSharedPrefs.getInt(key, defaultValue));
        return mSharedPrefs.getInt(key, defaultValue);

    }

    public boolean get(String key, boolean defaultValue) {

        Timber.d(key + " Load data ->> " + mSharedPrefs.getBoolean(key, defaultValue));
        return mSharedPrefs.getBoolean(key, defaultValue);

    }

    public void apply(String key, Object value) {
        put(key, value).apply();
    }

    public void apply(String key, String value) {
        putString(key, value).apply();
    }

    public void apply(String key, int value) {
        putInt(key, value).apply();
    }

    public void apply(String key, Boolean value) {
        putBoolean(key, value).apply();
    }

    public void commit(String key, Object value) {
        put(key, value).commit();
    }

    private void clearAll() {
        Editor e = mSharedPrefs.edit();
        e.clear();
        e.apply();
    }

    @SuppressLint("CommitPrefEdits")
    private Editor put(String key, Object value) {
        Editor e = mSharedPrefs.edit();
        Timber.d("JSON data -> " + mGson.toJson(value));
        e.putString(key, mGson.toJson(value));
        return e;
    }


    private Editor putString(String key, String value) {
        Editor e = mSharedPrefs.edit();
        Timber.d("Value Masuk -> " + value);
        e.putString(key, value);
        return e;
    }

    private Editor putInt(String key, int value) {
        Editor e = mSharedPrefs.edit();
        Timber.d("Value Masuk -> " + value);
        e.putInt(key, value);
        return e;
    }

    private Editor putBoolean(String key, boolean value) {
        Editor e = mSharedPrefs.edit();
        Timber.d("Value Masuk -> " + value);
        e.putBoolean(key, value);
        return e;
    }
}
