package com.technotium.technotiumapp.expenses.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.expenses.activity.ViewAllExpenses;
import com.technotium.technotiumapp.expenses.model.Expense;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseAdapter  extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    ArrayList<Expense> expense_List;
    Context context;
    private static ClickListener clickListener;
    public ExpenseAdapter(ArrayList<Expense> pay_List, Context context) {
        this.expense_List=pay_List;
        this.context=context;
    }

    public interface ClickListener {
        void onLongItemClick(int position, View v);
    }
    public void setOnItemClickListener(ExpenseAdapter.ClickListener clickListener) {
        ExpenseAdapter.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expense, parent, false);
        ExpenseAdapter.ExpenseViewHolder holder = new ExpenseAdapter.ExpenseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseViewHolder holder, final int position) {
        Expense paymentPojo=expense_List.get(position);
        holder.txtDate.setText("Date: "+paymentPojo.getInserttimestamp());
        holder.txtAmount.setText("Amount: "+paymentPojo.getAmount());
        holder.txtComment.setText("Comment: "+paymentPojo.getComment());
        holder.txtAddedBy.setText("Added By: "+paymentPojo.getInsertuserid());
        if(paymentPojo.getActive()==2){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
        }
        else{
            ((CardView)holder.card_view).setCardBackgroundColor(Color.WHITE);
        }

        if(!paymentPojo.getExp_img().equals("null")){
            Glide.with(context).load(WebUrl.BASE_URL+paymentPojo.getExp_img()).into(holder.pay_img);
            holder.pay_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewAllExpenses)context).showZoomImageDialog(WebUrl.BASE_URL+expense_List.get(position).getExp_img());
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return expense_List.size();
    }
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtDate,txtAmount,txtComment,txtAddedBy;
        ImageView pay_img;
        CardView card_view;
        public ExpenseViewHolder(View view) {
            super(view);
            txtDate= (TextView) view.findViewById(R.id.txtDate);
            txtAmount= (TextView) view.findViewById(R.id.txtAmount);
            txtComment= (TextView) view.findViewById(R.id.txtComment);
            pay_img=(ImageView) view.findViewById(R.id.pay_img);
            card_view=(CardView) view.findViewById(R.id.card_view);
            txtAddedBy=(TextView) view.findViewById(R.id.txtAddedBy);
            view.setOnLongClickListener(this);
        }
        @Override
        public boolean onLongClick(View v) {
            clickListener.onLongItemClick(getAdapterPosition(), v);
            return true;
        }
    }
}
