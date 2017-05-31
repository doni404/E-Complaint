package com.example.donisaurus.ecomplaint.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.model.ModelPost;
import com.example.donisaurus.ecomplaint.util.HelperTime;
import com.example.donisaurus.ecomplaint.util.HelperUrl;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Created by Donisaurus on 11/30/2016.
 */
public class AdapterTL extends RecyclerView.Adapter<AdapterTL.ViewHolder> {

    private List<ModelPost> mPosts = new ArrayList<>();
    private final Context mContext;
    private AdapterTLListener listener;

    public AdapterTL(Context mContext, List<ModelPost> posts) {
        this.mPosts = posts;
        this.mContext = mContext;
    }

    public void setonItemClick(AdapterTLListener listener) {
        this.listener = listener;
    }

    public void onUpdate(List<ModelPost> posts) {
        this.mPosts.addAll(posts);
        notifyDataSetChanged();
    }

    public ModelPost getItem(int position) {
        return mPosts.get(position);
    }

    public void updateItem(ModelPost post, int position) {
        this.mPosts.set(position, post);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mPosts.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ModelPost post = mPosts.get(position);
        holder.mUsername.setText(post.getNama());
        holder.mTlTime.setText(HelperTime.ConvertMilis(post.getWaktu()));
        holder.mLocText.setText(post.getNamaLokasi());
        holder.mTlDisc.setText(post.getDiskripsi());
        holder.mLikeCount.setText(String.valueOf(post.getSuka()) + " Orang");
        holder.mCommentCount.setText(String.valueOf(post.getKomentar()) + " Orang");
        if (post.getIdLike() != null) {
            holder.mLikeIc.setImageResource(R.mipmap.ic_star_black_24dp);
        } else {
            holder.mLikeIc.setImageResource(R.mipmap.ic_star_border_black_24dp);
        }

        Timber.d(HelperUrl.URL_IMAGE + post.getGambar());
        Glide.with(mContext)
                .load(HelperUrl.URL_IMAGE + post.getGambar())
                .centerCrop()
                .crossFade()
                .into(holder.mTlImage);

        holder.mLikeIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickViewAdapter(v, position);
                } else {
                    Timber.e("listener belum register");
                }
            }
        });
        holder.mCommentIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickViewAdapter(v, position);
                } else {
                    Timber.e("listener belum register");
                }
            }
        });

        holder.mTlMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickViewAdapter(v,position);
                } else {
                    Timber.e("listener belum register");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public CircleImageView mAvatar;
        public TextView mUsername;
        public TextView mTlTime;
        public ImageView mTlImage;
        public RelativeLayout mTlMap;
        public ImageView mLocId;
        public TextView mLocText;
        public TextView mTlDisc;
        public ImageView mLikeIc;
        public TextView mLikeCount;
        public ImageView mCommentIc;
        public TextView mCommentCount;
        //public ImageView mFavoriteIc;
        //public TextView mFavCount;


        public ViewHolder(View v) {
            super(v);
            //mAvatar = (CircleImageView) v.findViewById(R.id.mAvatar);
            mUsername = (TextView) v.findViewById(R.id.mUsername);
            mTlTime = (TextView) v.findViewById(R.id.mTlTime);
            mTlImage = (ImageView) v.findViewById(R.id.mTlImage);
            mTlMap = (RelativeLayout) v.findViewById(R.id.mTlMap);
            mLocId = (ImageView) v.findViewById(R.id.mLocId);
            mLocText = (TextView) v.findViewById(R.id.mLocText);
            mTlDisc = (TextView) v.findViewById(R.id.mTlDisc);
            mLikeIc = (ImageView) v.findViewById(R.id.mLikeIc);
            mLikeCount = (TextView) v.findViewById(R.id.mLikeCount);
            mCommentIc = (ImageView) v.findViewById(R.id.mCommentIc);
            mCommentCount = (TextView) v.findViewById(R.id.mCommentCount);
            //mFavoriteIc = (ImageView) v.findViewById(R.id.favorite_ic);
            //mFavCount = (TextView) v.findViewById(R.id.fav_count);
        }
    }


    public interface AdapterTLListener{
        void onClickViewAdapter(View view, int position);
    }
}
