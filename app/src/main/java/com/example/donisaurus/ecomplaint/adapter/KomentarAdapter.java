package com.example.donisaurus.ecomplaint.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.donisaurus.ecomplaint.R;
import com.example.donisaurus.ecomplaint.model.ModelComment;

import java.util.ArrayList;
import java.util.List;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder>{


    private List<ModelComment> listKomentar = new ArrayList<>();

    public void clear() {
        listKomentar.clear();
        notifyDataSetChanged();
    }

    public void addKomentar(List<ModelComment> komentar) {
        listKomentar.addAll(komentar);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komentar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelComment komentar = listKomentar.get(position);
        holder.mNama.setText(komentar.getUserYgKomen());
        holder.mKomentar.setText(komentar.getKomentar());

    }

    @Override
    public int getItemCount() {
        return listKomentar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mNama;
        public TextView mKomentar;

        public ViewHolder(View itemView) {
            super(itemView);
            mNama = (TextView) itemView.findViewById(R.id.username);
            mKomentar = (TextView) itemView.findViewById(R.id.komentar);
        }
    }
}
