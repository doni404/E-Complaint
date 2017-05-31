package com.example.donisaurus.ecomplaint.util;

/**
 * Created by Donisaurus on 12/14/2016.
 */

public class HelperUrl {

    public static int TAG_ALL = 1;
    public static int TAG_MOSTLIKE = 2;
    public static int TAG_ACAK = 3;
    public static int TAG_FILTER = 4;
    public static boolean FILTERED = false;

    public static String urlserver = "http://nbserver-labmobileptiik.rhcloud.com/";
    public static String url = urlserver + "api/";
    public static String GET_UPDATE_POST = url + "post/show/update";
    public static String GET_LESS_POST = url + "post/show/all";
    public static String GET_All = url + "post/show/all";
    public static String GET_MOSTLIKE = url + "post/show/mostliked";
    public static String GET_ACAK = url + "post/show/random";
    public static String ADD_POST = url + "post/add";
    public static String URL_IMAGE = urlserver + "uploads/images/";
    public static String LOGIN_USER = url + "user/login";
    public static String REG_USER = url + "user/register";
    public static String GET_KOMENTAR = url + "komentar/idpost/";
    public static String POST_KOMENTAR = url + "komentar/add";
    public static String POST_LIKE = url + "like/add";
    public static String DEL_LIKE = url + "like/delete";
}
