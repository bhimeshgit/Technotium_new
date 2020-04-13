package com.technotium.technotiumapp.workorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderPdfReportActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderViewHolder>{

    ArrayList<WorkOrderPojo>  orderList;
    Context context;
    private static ClickListener clickListener;
    Button btnDelete;


    public WorkOrderAdapter(){

    }
    public WorkOrderAdapter(ArrayList<WorkOrderPojo> orderList, Context context) {
        this.orderList=orderList;
        this.context=context;
    }

    @NonNull
    @Override
    public WorkOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_order_view_row, parent, false);
        WorkOrderViewHolder holder = new WorkOrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderViewHolder holder, final int position) {
        final WorkOrderPojo workOrderPojo=orderList.get(position);

        holder.customerNameTxt.setText(workOrderPojo.getFname()+" "+workOrderPojo.getLname());
        holder.mobileTxt.setText(workOrderPojo.getMobile());
        holder.addedByTxt.setText("Ex.: "+workOrderPojo.getAdded_by());
        holder.orderDateTxt.setText(workOrderPojo.getOrder_date());

        if(workOrderPojo.getWo_activity()==0){
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnReport.setVisibility(View.GONE);
        }
        if(!SessionManager.getMyInstance(context).getEmpType().equalsIgnoreCase("admin")){
            holder.btnDelete.setVisibility(View.GONE);
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchOrderActivity)context).showDeleteAlertDialog(Integer.parseInt(workOrderPojo.getPkid()),position);
            }
        });
        holder.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchOrderActivity)context).startActivity(new Intent((SearchOrderActivity)context, WorkOrderPdfReportActivity.class));
                ((SearchOrderActivity)context).finish();
            }
        });

        if(workOrderPojo.getActive()==2){
            holder.card_view.setBackgroundColor(Color.GRAY);
            holder.btnDelete.setVisibility(View.GONE);
        }
        else{
            holder.card_view.setBackgroundColor(Color.WHITE);
        }

    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WorkOrderAdapter.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class WorkOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{
        TextView customerNameTxt,mobileTxt,addedByTxt,orderDateTxt;
        CardView card_view;Button btnDelete,btnReport;
        public WorkOrderViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            btnDelete=(Button) view.findViewById(R.id.btnDelete);
            btnReport=(Button) view.findViewById(R.id.btnReport);
            customerNameTxt = (TextView) view.findViewById(R.id.customerNameTxt);
            mobileTxt= (TextView) view.findViewById(R.id.mobileTxt);
            addedByTxt= (TextView) view.findViewById(R.id.addedByTxt);
            orderDateTxt= (TextView) view.findViewById(R.id.orderDateTxt);
            card_view=(CardView) view.findViewById(R.id.card_view);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("iss","Long click");
            clickListener.onLongItemClick(getAdapterPosition(),v);
            return true;
        }
    }
}
