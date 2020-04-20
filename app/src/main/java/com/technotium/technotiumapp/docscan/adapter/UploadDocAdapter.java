package com.technotium.technotiumapp.docscan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.docscan.model.UploadDocPojo;

import java.util.ArrayList;

public class UploadDocAdapter  extends RecyclerView.Adapter<UploadDocAdapter.UploadDocHolder> {
    ArrayList<UploadDocPojo> doc_List;
    Context context;
    private static UploadDocAdapter.ClickListener clickListener;

    public UploadDocAdapter(ArrayList<UploadDocPojo> doc_List, Context context) {
        this.doc_List=doc_List;
        this.context=context;
    }
    @NonNull
    @Override
    public UploadDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_multiple_doc_list, parent, false);
        UploadDocAdapter.UploadDocHolder holder=new UploadDocAdapter.UploadDocHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadDocAdapter.UploadDocHolder holder, int position) {
        holder.doc_name.setText(doc_List.get(position).getDoc_type());
        holder.imageView.setImageBitmap(doc_List.get(position).getBitmap());
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        UploadDocAdapter.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return doc_List.size();
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }

    public static class UploadDocHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        ImageView imageView;
        TextView doc_name;
        CardView card_view;

        public UploadDocHolder(View view) {
            super(view);
            doc_name= (TextView) view.findViewById(R.id.doc_name);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            card_view= (CardView) view.findViewById(R.id.card_view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
        @Override
        public boolean onLongClick(View v) {
            clickListener.onLongItemClick(getAdapterPosition(),v);
            return true;
        }
    }

}
