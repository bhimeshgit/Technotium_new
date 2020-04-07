package com.technotium.technotiumapp.material.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.material.model.MaterialPojo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    ArrayList<MaterialPojo> material_List;
    Context context;
    private static ClickListener clickListener;
    public MaterialAdapter(ArrayList<MaterialPojo> material_List, Context context) {
        this.material_List=material_List;
        this.context=context;
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_material, parent, false);
        MaterialAdapter.MaterialViewHolder holder = new MaterialAdapter.MaterialViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        MaterialPojo materialPojo=material_List.get(position);
        holder.txtMaterialName.setText("Material: "+materialPojo.getMaterial());
        holder.quantityTxt.setText("Quantity: "+materialPojo.getQuantity());
        holder.txtdate.setText(materialPojo.getInserttimestamp());
        holder.insertByTxt.setText("Added By: "+materialPojo.getInsertBy());
        if(materialPojo.getActive()==2){
            ((CardView)holder.card_view).setCardBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return material_List.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MaterialAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemLongClick(int position, View v);
    }
    public static class MaterialViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtMaterialName,quantityTxt,txtdate,insertByTxt;
        CardView card_view;
        public MaterialViewHolder(View view) {
            super(view);
            txtMaterialName=view.findViewById(R.id.txtMaterialName);
            quantityTxt=view.findViewById(R.id.quantityTxt);
            card_view=view.findViewById(R.id.card_view);
            txtdate=view.findViewById(R.id.txtdate);
            insertByTxt=view.findViewById(R.id.insertByTxt);
            view.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(),v);
            return false;
        }
    }
}