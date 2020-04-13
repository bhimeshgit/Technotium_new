package com.technotium.technotiumapp.after_sales.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.after_sales.model.SerialNoPojo;


import java.util.ArrayList;

public class SerialNoAdapter extends RecyclerView.Adapter<SerialNoAdapter.SerialNoViewHolder>  {
    ArrayList<SerialNoPojo> List;
    Context context;
    private ClickListener clickListener;
    String type;
    public SerialNoAdapter(ArrayList<SerialNoPojo> List, Context context,String type) {
        this.List=List;
        this.context=context;
        this.type=type;
    }
    @NonNull
    @Override
    public SerialNoAdapter.SerialNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_serial_no, parent, false);
        SerialNoViewHolder holder = new SerialNoViewHolder(view);
        return holder;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }
    @Override
    public void onBindViewHolder(@NonNull final SerialNoAdapter.SerialNoViewHolder holder, final int position) {
        SerialNoPojo serialNoPojo=List.get(position);
        holder.txtSerialNo.setText(serialNoPojo.getSerial_no());
        holder.txtdate.setText(serialNoPojo.getInserttimestamp());
        if(type.equals("portal")){
            holder.txtSerialNo.setText("Poratal ID: "+serialNoPojo.getSerial_no());
            holder.txtPass.setVisibility(View.VISIBLE);
            holder.txtPass.setText("Password: "+serialNoPojo.getPassword());
        }
        if(serialNoPojo.getActive()==2){
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

    public  class SerialNoViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {
        TextView txtSerialNo,txtdate,txtPass;
        CardView card_view;

        public SerialNoViewHolder(View view) {
            super(view);
            txtSerialNo = (TextView) view.findViewById(R.id.txtSerialNo);
            txtdate= (TextView) view.findViewById(R.id.txtdate);
            txtPass= (TextView) view.findViewById(R.id.txtPass);
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
