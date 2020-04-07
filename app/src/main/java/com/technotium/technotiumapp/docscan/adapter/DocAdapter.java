package com.technotium.technotiumapp.docscan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.docscan.model.DocPojo;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class DocAdapter  extends RecyclerView.Adapter<DocAdapter.DocViewHolder> {

    ArrayList<DocPojo> doc_List;
    Context context;
    private static DocAdapter.ClickListener clickListener;

    public DocAdapter(ArrayList<DocPojo> doc_List, Context context) {
        this.doc_List=doc_List;
        this.context=context;
    }

    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doc_list, parent, false);
        DocAdapter.DocViewHolder holder=new DocAdapter.DocViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DocViewHolder holder, int position) {
        final DocPojo docPojo=doc_List.get(position);
        holder.doc_name.setText(docPojo.getDoc_name());
        Glide.with(context).load(WebUrl.BASE_URL+docPojo.getDoc_path()).into(holder.imageView);
        if(docPojo.getActive().equals("2")){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
        }
        else{
            ((CardView)holder.card_view).setCardBackgroundColor(Color.WHITE);
        }
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(WebUrl.BASE_URL+docPojo.getDoc_path()); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    });

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (holder.imageView.getDrawable() == null){
//
//                }else{
//                    holder.btnShare.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            shareImage(((BitmapDrawable)holder.imageView.getDrawable()).getBitmap());
//                        }
//                    });
//                }
//            }
//        }, 5000);

    }

    @Override
    public int getItemCount() {
        return doc_List.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DocAdapter.clickListener = clickListener;
    }

    private void shareImage(Bitmap bitmap){
        // save bitmap to cache directory
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();


            File imagePath = new File(context.getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType("image/png");
                context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }
    public static class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        ImageView imageView;
        TextView doc_name;
        CardView card_view;
        Button btnShare;

        public DocViewHolder(View view) {
            super(view);
            doc_name= (TextView) view.findViewById(R.id.doc_name);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            card_view= (CardView) view.findViewById(R.id.card_view);
            btnShare=(Button)view.findViewById(R.id.btnShare);
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
