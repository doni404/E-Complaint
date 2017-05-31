package com.example.donisaurus.ecomplaint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.example.donisaurus.ecomplaint.MyApplication;
import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.activity.KomentarActivity;
import com.example.donisaurus.ecomplaint.adapter.AdapterTL;
import com.example.donisaurus.ecomplaint.model.ModelLike;
import com.example.donisaurus.ecomplaint.model.ModelPost;
import com.example.donisaurus.ecomplaint.model.ModelPostDB;
import com.example.donisaurus.ecomplaint.model.ModelPostResponse;
import com.example.donisaurus.ecomplaint.model.ModelUser;
import com.example.donisaurus.ecomplaint.model.ResponOnly;
import com.example.donisaurus.ecomplaint.util.DroidPrefs;
import com.example.donisaurus.ecomplaint.util.HelperKey;
import com.example.donisaurus.ecomplaint.util.HelperStatus;
import com.example.donisaurus.ecomplaint.util.HelperUrl;
import com.example.donisaurus.ecomplaint.util.MySnackBar;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ArikAchmad on 11/29/2015.
 */
public class FragTimeline extends Fragment implements AdapterTL.AdapterTLListener {

    public static String TAG_URL = "tag_url";
    public static String mUrl = null;
    private RecyclerView mListTimeline;
    private RequestQueue queue;
    private View relativeLayout;
    private AdapterTL mAdapter;
    private static String TAG = FragTimeline.class.getSimpleName();
    private ModelUser mUser;
    public static List<ModelPost> listPosting = new ArrayList<>();

    private void assignViews() {
        mListTimeline = (RecyclerView) findViewById(R.id.listTimeline);
        relativeLayout = getActivity().findViewById(R.id.relativeLayout);
        queue = MyApplication.getInstance().getRequestQueue();
        mAdapter = new AdapterTL(getActivity(), new ArrayList<ModelPost>());
        mAdapter.setonItemClick(this);
    }

    @SuppressWarnings("ConstantConditions")
    private View findViewById(int id) {
        return getView().findViewById(id);
    }

    private void onSetView() {
        mListTimeline.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mListTimeline.setLayoutManager(manager);

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.getInt(TAG_URL) == HelperUrl.TAG_ALL) {
                mUrl = HelperUrl.GET_All;
            } else if (bundle.getInt(TAG_URL) == HelperUrl.TAG_MOSTLIKE) {
                mUrl = HelperUrl.GET_MOSTLIKE;
            } else if (bundle.getInt(TAG_URL) == HelperUrl.TAG_ACAK) {
                mUrl = HelperUrl.GET_ACAK;
            } else if (bundle.getInt(TAG_URL) == HelperUrl.TAG_FILTER) {
                mUrl = HelperUrl.GET_All;
            } else {
                mUrl = HelperUrl.GET_All;
            }
        } else {
            mUrl = HelperUrl.GET_All;
        }

    }

    private void get4Local() {
        List<ModelPostDB> listPostDB = ModelPostDB.getALl();
        List<ModelPost> listpost = new ArrayList<>();

        for (ModelPostDB postDB : listPostDB) {
            ModelPost post = new ModelPost();
            post.setIdPost(postDB.getId_post());
            post.setIdUser(postDB.getId_user());
            post.setGambar(postDB.getGambar());
            post.setWaktu(postDB.getWaktu());
            post.setDiskripsi(postDB.getDiskripsi());
            post.setLatitude(postDB.getLatitude());
            post.setLongitude(postDB.getLongitude());
            post.setNamaLokasi(postDB.getNama_lokasi());
            post.setSuka(postDB.getSuka());
            post.setNama(postDB.getUser());
            post.setKomentar(postDB.getKomentar());
            listpost.add(post);
        }
        mAdapter.onUpdate(listpost);
        mListTimeline.setAdapter(mAdapter);
    }

    private void getDataPost() {
        mAdapter.clear();
        mUser = DroidPrefs.get(getActivity(), HelperKey.USER_KEY, ModelUser.class);
        String mUrlLokal = mUrl + "/" + mUser.getIdUser();
        queue.getCache().invalidate(mUrlLokal, true);

        Log.d("url", mUrlLokal);

        HashMap<String, String> params = new HashMap<>();

        if (mAdapter.getItemCount() > 0) {
            params.put("waktu", mAdapter.getItem(0).getWaktu());
        } else {
            params.put("waktu", String.valueOf(0));
        }

        Timber.d(mUrlLokal);

        GsonRequest<ModelPostResponse> request = new GsonRequest<>(mUrlLokal, ModelPostResponse.class, null,
                new PostResponse(),
                new PostResponse()
        );
        request.setTag(FragTimeline.class.getSimpleName());
        MyApplication.getInstance().addToRequestQueue(request);
        //queue.add(request);

        Log.d("error", request.toString());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();
        onSetView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(FragTimeline.class.getSimpleName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataPost();
    }

    @Override
    public void onClickViewAdapter(View view, int position) {
        switch (view.getId()) {
            case R.id.mLikeIc:
                mAdapter.setonItemClick(null);
                sendLike(position);
                Toast.makeText(getActivity(), "like " + position, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mCommentIc:
                Intent pindahKomentar = new Intent(getActivity(), KomentarActivity.class);
                pindahKomentar.putExtra(KomentarActivity.GET_IDPOST, mAdapter.getItem(position).getIdPost());
                pindahKomentar.putExtra(KomentarActivity.GET_DISC, mAdapter.getItem(position).getDiskripsi());
                startActivity(pindahKomentar);
                break;

//            case R.id.tl_map:
//                Intent pindahMap = new Intent(getActivity(), ShowMapActivity.class);
//                pindahMap.putExtra(ShowMapActivity.TAG_TITLE, mAdapter.getItem(position).getNamaLokasi());
//                pindahMap.putExtra(ShowMapActivity.TAG_LAT, mAdapter.getItem(position).getLatitude());
//                pindahMap.putExtra(ShowMapActivity.TAG_LNG, mAdapter.getItem(position).getLongitude());
//                startActivity(pindahMap);
//                break;
        }
    }


    private void sendLike(int position) {

        String mUrlLokal;

        ModelPost post = mAdapter.getItem(position);
        ModelLike like = new ModelLike();
        if (post.getIdLike() == null) {
            mUrlLokal = HelperUrl.POST_LIKE;
        } else {
            int tambahLike = post.getSuka() - 1;
            BigDecimal idLike = new BigDecimal(post.getIdLike().toString());
            Timber.d("idlike -->>" + idLike.intValue());
            like.setIdLike(idLike.intValue());
            post.setSuka(tambahLike);
            post.setIdLike(null);
            mAdapter.updateItem(post, position);
            mUrlLokal = HelperUrl.DEL_LIKE;
        }


        like.setIdPost(post.getIdPost());
        like.setIdUserLiked(mUser.getIdUser());
        Timber.d(new Gson().toJson(like));

        HashMap<String, String> params = new HashMap<>();
        params.put("jsondata", new Gson().toJson(like));

        GsonRequest<ResponOnly> request = new GsonRequest<>(
                Request.Method.POST,
                mUrlLokal,
                ResponOnly.class,
                null,
                params,
                new LiketResponse(post, position),
                new LiketResponse(post, position)
        );

        queue.add(request);
    }

    private class LiketResponse implements Response.ErrorListener, Response.Listener<ResponOnly> {

        private final ModelPost mPost;
        private final int mPosition;

        public LiketResponse(ModelPost post, int position) {
            this.mPost = post;
            this.mPosition = position;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Timber.e(error.getMessage());
            MySnackBar.init(relativeLayout, "Koneksi bermasalah").show();
            mAdapter.setonItemClick(FragTimeline.this);
        }

        @Override
        public void onResponse(ResponOnly response) {
            mAdapter.setonItemClick(FragTimeline.this);
            if (response.getStatus() != HelperStatus.SUKSES) {

                MySnackBar.init(relativeLayout, "Server Bermasalah").show();
            } else {
                Timber.d("insert id ->" + response.getData().getInsertId());
                if (response.getData().getInsertId() != 0) {
                    int tambahLike = mPost.getSuka() + 1;
                    mPost.setSuka(tambahLike);
                    mPost.setIdLike(response.getData().getInsertId());
                    mAdapter.updateItem(mPost, mPosition);
                }
            }
        }
    }

    private class PostResponse implements Response.ErrorListener, Response.Listener<ModelPostResponse> {
        @Override
        public void onErrorResponse(VolleyError error) {
            Timber.d("error", error.getMessage());
            Log.d("error", error.getMessage());
            MySnackBar.init(relativeLayout, "Koneksi bermasalah").show();
        }

        @Override
        public void onResponse(ModelPostResponse response) {
            if (response.getStatus() == HelperStatus.SUKSES){
                if (!HelperUrl.FILTERED){
                    listPosting = response.getData();
                }

                if (HelperUrl.FILTERED){
                    mAdapter.onUpdate(listPosting);
                    HelperUrl.FILTERED = false;
                }else {
                    mAdapter.onUpdate(response.getData());
                }

                mListTimeline.setAdapter(mAdapter);
                MySnackBar.init(relativeLayout, "Koneksi berhasil").show();

//                mAdapter.onUpdate(response.getData());
//                mListTimeline.setAdapter(mAdapter);
//
//                MySnackBar.init(relativeLayout, "Koneksi berhasil").show();
            }else {
                MySnackBar.init(relativeLayout, "Server bermasalah").show();
            }
        }
    }
}
