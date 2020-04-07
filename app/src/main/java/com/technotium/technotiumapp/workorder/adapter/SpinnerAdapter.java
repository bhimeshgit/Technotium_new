package com.technotium.technotiumapp.workorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.technotium.technotiumapp.R;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
	
	 	Context con;
	    ArrayList<String> arrData = new ArrayList<String>();
	    LayoutInflater mInflater;
	    
	    public SpinnerAdapter(Context con, ArrayList<String> arrData) {
	        this.con = con;
	        this.arrData = arrData;
	        mInflater = LayoutInflater.from(con);
	    }

	    public class ViewHolder {
	        TextView txtItem;
	    }
	    
	    @SuppressWarnings("unused")
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
	    	
	    	final ViewHolder holder;
	        view = null;
	        if (view == null) {
	            holder = new ViewHolder();
	            view = mInflater.inflate(R.layout.row_spinner, null);

	            holder.txtItem = (TextView) view.findViewById(R.id.txtItem);
	           
	            view.setTag(holder);
	        }
	        else{
	        	holder = (ViewHolder) view.getTag();
	        }

	        holder.txtItem.setText(arrData.get(position));
	       
			return view;
		}
	    
	    

	    @Override
	    public int getCount() {
	        return arrData.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return arrData.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

}
