package com.technotium.technotiumapp.status.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;

import com.technotium.technotiumapp.status.OrderStatusPOJO;
import com.technotium.technotiumapp.status.activity.OrderStatusEntryActivity;
import com.technotium.technotiumapp.status.activity.OrderStatusListActivity;

import java.util.ArrayList;

public class OrderStatusListAdapter extends RecyclerView.Adapter<OrderStatusListAdapter.PaymentViewHolder> {
    ArrayList<OrderStatusPOJO> pay_List;
    Context context;
    private static OrderStatusListAdapter.ClickListener clickListener;
    String image_url="";

    public OrderStatusListAdapter(){

    }
    public OrderStatusListAdapter(ArrayList<OrderStatusPOJO> pay_List, Context context) {
        this.pay_List=pay_List;
        this.context=context;
    }
    @NonNull
    @Override
    public OrderStatusListAdapter.PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status_list_row, parent, false);
        OrderStatusListAdapter.PaymentViewHolder holder = new OrderStatusListAdapter.PaymentViewHolder(view);
        return holder;
    }
    public void setOnItemClickListener(OrderStatusListAdapter.ClickListener clickListener) {
        OrderStatusListAdapter.clickListener = clickListener;
    }
    @Override
    public void onBindViewHolder(@NonNull final OrderStatusListAdapter.PaymentViewHolder holder, final int position) {
        try {
            OrderStatusPOJO paymentPojo = pay_List.get(position);
            if (paymentPojo.getEx_name() != null) {
                holder.exName.setText("Ex. " + paymentPojo.getEx_name());
            }
            holder.txtDate.setText(paymentPojo.getStatus_date());
            holder.txtStatusType.setText(paymentPojo.status_type);
            holder.txtStatus.setText(paymentPojo.getStatus());
            holder.txtExpense.setText(paymentPojo.getExpense());
            if (paymentPojo.getComment() != null) {
                holder.txtComment.setText(paymentPojo.getComment());
            }
            Linkify.addLinks(holder.txtComment, Linkify.WEB_URLS);

            if (paymentPojo.getActive().equals("2")) {
                ((CardView) holder.card_view).setCardBackgroundColor(Color.GRAY);
            } else {
                ((CardView) holder.card_view).setCardBackgroundColor(Color.WHITE);
            }

            if (!paymentPojo.getSt_image().equals("null") && !paymentPojo.getSt_image().trim().equals("")) {
                Glide.with(context).load(WebUrl.BASE_URL + paymentPojo.getSt_image()).into(holder.pay_img);
                holder.pay_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OrderStatusListActivity) context).showZoomImageDialog(WebUrl.BASE_URL + pay_List.get(position).getSt_image());
                    }
                });
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (holder.pay_img.getDrawable() == null) {

                    } else {
                        holder.btnShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareImage(position);
                            }
                        });
                    }
                }
            }, 5000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pay_List.size();
    }
    private void shareImage(int position){
        // save bitmap to cache directory
        try {

            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Payment Image");
            share.putExtra(Intent.EXTRA_TEXT, WebUrl.BASE_URL+pay_List.get(position).getSt_image());

            context.startActivity(Intent.createChooser(share, "Share link!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }
    public static class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView exName,txtDate,txtStatusType,txtStatus,txtExpense,txtComment;
        ImageView pay_img;
        CardView card_view;
        Button btnShare;
        public PaymentViewHolder(View view) {
            super(view);
            exName= (TextView) view.findViewById(R.id.exName);
            txtDate= (TextView) view.findViewById(R.id.txtDate);
            txtStatusType= (TextView) view.findViewById(R.id.txtStatusType);
            txtComment= (TextView) view.findViewById(R.id.txtComment);
            pay_img=(ImageView) view.findViewById(R.id.pay_img);
            txtComment= (TextView) view.findViewById(R.id.txtComment);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtExpense = (TextView) view.findViewById(R.id.txtExpense);
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
