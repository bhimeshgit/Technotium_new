package com.technotium.technotiumapp.adapter;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.model.HomeIcon;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeIconAdapter extends RecyclerView.Adapter<HomeIconAdapter.IconViewHolder>{
    ArrayList<HomeIcon> iconlist = new ArrayList<HomeIcon>();
    static Context context;
    private static ClickListener clickListener;

    public HomeIconAdapter(ArrayList<HomeIcon> iconlist,Context context) {
        this.iconlist = iconlist;
        this.context=context;
    }


    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        IconViewHolder iconViewHolder = new IconViewHolder(view);
        return iconViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        HomeIcon cour = iconlist.get(position);
        holder.courseImage.setImageResource(cour.getImage_id());
        holder.courseName.setText(cour.getName());
    }

    @Override
    public int getItemCount() {
        return iconlist.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        HomeIconAdapter.clickListener = clickListener;
    }

    public static class IconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView courseImage;
        TextView courseName;

        public IconViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            courseImage = (ImageView) view.findViewById(R.id.courImage);
            courseName = (TextView) view.findViewById(R.id.courName);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}

