package com.technotium.technotiumapp.workorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderPdfReportActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import java.util.ArrayList;

public class SmsSendAdapter  extends RecyclerView.Adapter<SmsSendAdapter.SendSmsViewHolder> {
    ArrayList<WorkOrderPojo> orderList;
    Context context;
    private static SmsSendAdapter.ClickListener clickListener;

    public SmsSendAdapter(ArrayList<WorkOrderPojo> orderList, Context context) {
        this.orderList=orderList;
        this.context=context;
    }
    @NonNull
    @Override
    public SendSmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_send_sms, parent, false);
        SmsSendAdapter.SendSmsViewHolder holder = new SmsSendAdapter.SendSmsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SendSmsViewHolder holder, final int position) {
        final WorkOrderPojo workOrderPojo=orderList.get(position);

        holder.customerNameTxt.setText(workOrderPojo.getFname()+" "+workOrderPojo.getLname());
        holder.mobileTxt.setText(workOrderPojo.getMobile());
        holder.addedByTxt.setText("Ex.: "+workOrderPojo.getAdded_by());
        holder.orderDateTxt.setText(workOrderPojo.getOrder_date());
        holder.wo_no_txt.setText(workOrderPojo.getPkid());
        holder.chkBox.setChecked(orderList.get(position).isSelected());
        holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clickListener.onItemClick(position, isChecked);
            }
        });

    }

    public interface ClickListener {
        void onItemClick(int position, boolean b);
        void onLongItemClick(int position, View v);
    }

    public void setOnItemClickListener(SmsSendAdapter.ClickListener clickListener) {
        SmsSendAdapter.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class SendSmsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{
        TextView customerNameTxt,mobileTxt,addedByTxt,orderDateTxt,wo_no_txt;
        CardView card_view;
        CheckBox chkBox;
        public SendSmsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            customerNameTxt = (TextView) view.findViewById(R.id.customerNameTxt);
            mobileTxt= (TextView) view.findViewById(R.id.mobileTxt);
            addedByTxt= (TextView) view.findViewById(R.id.addedByTxt);
            orderDateTxt= (TextView) view.findViewById(R.id.orderDateTxt);
            card_view=(CardView) view.findViewById(R.id.card_view);
            wo_no_txt= (TextView) view.findViewById(R.id.wo_no_txt);
            chkBox=(CheckBox) view.findViewById(R.id.chkBox);
        }
        @Override
        public void onClick(View v) {
            //clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onLongItemClick(getAdapterPosition(),v);
            return true;
        }
    }
}
