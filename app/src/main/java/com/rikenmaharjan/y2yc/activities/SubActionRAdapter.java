package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.ActionModel;
import com.rikenmaharjan.y2yc.utils.SubAction;

import java.util.ArrayList;

/**
 * Created by bikenmaharjan on 6/4/18.
 */

public class SubActionRAdapter extends RecyclerView.Adapter<SubActionRAdapter.MyViewHolder> {

    Context nContext;
    SubAction []  data;

    public SubActionRAdapter(Context nContext, SubAction []  data) {
        this.nContext = nContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.subaction_cell,parent,false);
        MyViewHolder view = new MyViewHolder(v);

        view.cb_complete.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                }
        );

        return view;
    }

    // many time
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText((CharSequence) data[position].getTitle());
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.length;
        }
        else return 0;
    }


    //
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        CheckBox cb_complete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_subaction_title);
            cb_complete = (CheckBox) itemView.findViewById(R.id.cb_subaction_comp);

        }
    }
}
