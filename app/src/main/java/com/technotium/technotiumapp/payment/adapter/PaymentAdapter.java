package com.technotium.technotiumapp.payment.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.ImageProcessing;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.payment.model.PaymentPojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    ArrayList<PaymentPojo> pay_List;
    Context context;
    private static ClickListener clickListener;
    String image_url="";

    public PaymentAdapter(){

    }
    public PaymentAdapter(ArrayList<PaymentPojo> pay_List, Context context) {
        this.pay_List=pay_List;
        this.context=context;
    }
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_row, parent, false);
        PaymentAdapter.PaymentViewHolder holder = new PaymentAdapter.PaymentViewHolder(view);
        return holder;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        PaymentAdapter.clickListener = clickListener;
    }
    @Override
    public void onBindViewHolder(@NonNull final PaymentViewHolder holder,final int position) {
        PaymentPojo paymentPojo=pay_List.get(position);
        holder.txtPayMode.setText("Payment Mode: "+paymentPojo.getPayment_mode());
        holder.txtDate.setText("Payment Date: "+paymentPojo.getPayment_date());
        holder.txtAmount.setText("Payment Amount: "+paymentPojo.getAmount());
        holder.txtComment.setText("Comment: "+paymentPojo.getComment());
        if(paymentPojo.getActive().equals("2")){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
        }
        else{
            ((CardView)holder.card_view).setCardBackgroundColor(Color.WHITE);
        }

        if(!paymentPojo.getPay_image().equals("null")){
            Glide.with(context).load(WebUrl.BASE_URL+paymentPojo.getPay_image()).into(holder.pay_img);
            holder.pay_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PaymentHistoryActivity)context).showZoomImageDialog(WebUrl.BASE_URL+pay_List.get(position).getPay_image());
                }
            });
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (holder.pay_img.getDrawable() == null){

                }else{
                    holder.btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareImage(((BitmapDrawable)holder.pay_img.getDrawable()).getBitmap());
                        }
                    });
                }
            }
        }, 5000);
    }

    @Override
    public int getItemCount() {
        return pay_List.size();
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
    public static class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView txtPayMode,txtDate,txtAmount,txtComment;
        ImageView pay_img;
        CardView card_view;
        Button btnShare;
        public PaymentViewHolder(View view) {
            super(view);
            txtPayMode= (TextView) view.findViewById(R.id.txtPayMode);
            txtDate= (TextView) view.findViewById(R.id.txtDate);
            txtAmount= (TextView) view.findViewById(R.id.txtAmount);
            txtComment= (TextView) view.findViewById(R.id.txtComment);
            pay_img=(ImageView) view.findViewById(R.id.pay_img);
            card_view=(CardView) view.findViewById(R.id.card_view);
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


