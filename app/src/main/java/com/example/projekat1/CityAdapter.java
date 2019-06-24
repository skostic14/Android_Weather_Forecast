package com.example.projekat1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class CityAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<CityItem> mCities;

    public CityAdapter(Context context){
        mContext = context;
        mCities = new ArrayList<CityItem>();
    }

    public void addCity(CityItem city){
        mCities.add(city);
        notifyDataSetChanged();
    }

    public void removeCity(int position){
        mCities.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mCities.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        Object city = null;
        try{
            city = mCities.get(position);
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return city;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.city_row, null);
            ViewHolder holder = new ViewHolder();
            holder.select = (RadioButton) view.findViewById(R.id.cityButton);
            holder.name = (TextView) view.findViewById(R.id.cityText);
            view.setTag(holder);
        }

        CityItem city = (CityItem) getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(city.cityName);
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkCity = new Intent(mContext, DetailsActivity.class);
                Bundle context = new Bundle();
                context.putString("city", holder.name.getText().toString());
                checkCity.putExtras(context);
                holder.select.setChecked(false);
                mContext.startActivity(checkCity);
            }
        });

        return view;

    }

    private class ViewHolder{
        public RadioButton select = null;
        public TextView name = null;
    }
}
