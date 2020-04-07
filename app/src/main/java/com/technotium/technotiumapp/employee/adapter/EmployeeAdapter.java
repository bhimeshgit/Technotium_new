package com.technotium.technotiumapp.employee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.employee.AddUpdateEmpActivity;
import com.technotium.technotiumapp.employee.ManageEmployeeActivity;
import com.technotium.technotiumapp.employee.model.EmployeePojo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    ArrayList<EmployeePojo> employeeList;
    Context context;
    public EmployeeAdapter(ArrayList<EmployeePojo> employeeList, Context context) {
        this.employeeList=employeeList;
        this.context=context;
    }
    @NonNull
    @Override
    public EmployeeAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_employee_list, parent, false);
        EmployeeViewHolder holder = new EmployeeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeAdapter.EmployeeViewHolder holder, final int position) {
        EmployeePojo employeePojo=employeeList.get(position);
        holder.enameTxt.setText(employeePojo.getFname()+" "+employeePojo.getLname());
        holder.empTypeTxt.setText(employeePojo.getEmpType());
        if(employeePojo.getActive()==2){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
            holder.btnActive.setVisibility(View.VISIBLE);
            holder.btnUpdate.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
//            Log.d("iss","if");
        }
        else{
            ((CardView)holder.card_view).setCardBackgroundColor(Color.WHITE);
            holder.btnActive.setVisibility(View.GONE);
            holder.btnUpdate.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
//            Log.d("iss","else");
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageEmployeeActivity)context).deleteEmployee(employeeList.get(holder.getAdapterPosition()),holder.getAdapterPosition());
            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent=new Intent(context, AddUpdateEmpActivity.class);
                intent.putExtra("emp", gson.toJson(employeeList.get(holder.getAdapterPosition())));
                context.startActivity(intent);
            }
        });
        holder.btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageEmployeeActivity)context).activateEmployee(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }



    public static class EmployeeViewHolder extends RecyclerView.ViewHolder  {
        TextView enameTxt,empTypeTxt;
        Button btnDelete,btnUpdate,btnActive;
        CardView card_view;
        public EmployeeViewHolder(View view) {
            super(view);
            enameTxt = (TextView) view.findViewById(R.id.enameTxt);
            empTypeTxt= (TextView) view.findViewById(R.id.empTypeTxt);
            btnDelete= (Button) view.findViewById(R.id.btnDelete);
            btnUpdate= (Button) view.findViewById(R.id.btnUpdate);
            btnActive= (Button) view.findViewById(R.id.btnActive);
            card_view=(CardView)view.findViewById(R.id.card_view);
        }

    }

}
