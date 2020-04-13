package com.technotium.technotiumapp.after_sales.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.after_sales.model.MeterReadingPojo;
import com.technotium.technotiumapp.config.WebUrl;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MeterReadingAdapter extends RecyclerView.Adapter<MeterReadingAdapter.MeterReadingViewHolder>  {
    ArrayList<MeterReadingPojo> List;
    Context context;
    private  ClickListener clickListener;
    String type;
    public MeterReadingAdapter(ArrayList<MeterReadingPojo> List, Context context,String type) {
        this.List=List;
        this.context=context;
        this.type=type;
    }
    @NonNull
    @Override
    public MeterReadingAdapter.MeterReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_meter_reading, parent, false);
        MeterReadingViewHolder holder = new MeterReadingViewHolder(view);
        return holder;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }
    @Override
    public void onBindViewHolder(@NonNull final MeterReadingAdapter.MeterReadingViewHolder holder, final int position) {
        MeterReadingPojo meterReadingPojo=List.get(position);
        holder.txtdate.setText(meterReadingPojo.getInserttimestamp());
        try{
            Glide.with(context).load(WebUrl.BASE_URL+meterReadingPojo.getReading_img()).into(holder.imageView);
        }
        catch (Exception e){
        }
        if(meterReadingPojo.getActive()==2){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
        }
        else{
            ((CardView)holder.card_view).setCardBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return List.size();
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

public  class MeterReadingViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener ,View.OnLongClickListener {
    TextView txtdate;
    ImageView imageView;
    CardView card_view;
    public MeterReadingViewHolder(View view) {
        super(view);
        imageView= (ImageView) view.findViewById(R.id.imageView);
        txtdate= (TextView) view.findViewById(R.id.txtDate);
        card_view=(CardView) view.findViewById(R.id.card_view);
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
