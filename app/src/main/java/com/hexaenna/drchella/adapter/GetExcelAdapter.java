package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexaenna.drchella.Model.GetExcelModel;
import com.hexaenna.drchella.R;

import java.util.ArrayList;

/**
 * Created by admin on 12/13/2017.
 */

public class GetExcelAdapter extends RecyclerView.Adapter<GetExcelAdapter.ViewHolder> {

    ArrayList<GetExcelModel>excelModelArrayList;
    Context context;
    public GetExcelAdapter(ArrayList<GetExcelModel> getExcelModels, Context context)
    {
        this.excelModelArrayList = getExcelModels;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (position % 2 == 0) {
            holder.ldtMainBg.setBackgroundColor(Color.parseColor("#BCF7F0"));
        } else {
            holder.ldtMainBg.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.getExcelModel = excelModelArrayList.get(position);
        holder.mName.setText(holder.getExcelModel.getName());
        holder.mTime.setText(holder.getExcelModel.getTime());
        holder.mPhone.setText(holder.getExcelModel.getPhone());
        holder.mAge.setText(holder.getExcelModel.getAge());
        Log.e("name in excel",holder.getExcelModel.getName());

    }

    @Override
    public int getItemCount() {
        return excelModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mName,mTime,mPhone,mAge;
       GetExcelModel getExcelModel;
        LinearLayout ldtMainBg;

        public ViewHolder(View view) {
            super(view);

            mName = (TextView) view.findViewById(R.id.name);
            mTime = (TextView) view.findViewById(R.id.time);
            mPhone = (TextView) view.findViewById(R.id.phone);
            mAge = (TextView) view.findViewById(R.id.age);
            ldtMainBg = (LinearLayout) view.findViewById(R.id.ldtMainBg);


        }


      /*  @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
