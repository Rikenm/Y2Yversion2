package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.ActionModel;
import com.rikenmaharjan.y2yc.utils.HelperIdModel;
import com.rikenmaharjan.y2yc.utils.SubAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikenmaharjan on 6/4/18.
 */

public class SubActionRAdapter extends RecyclerView.Adapter<SubActionRAdapter.MyViewHolder> {

    Context nContext;
    SubAction []  data;
    ArrayList<String> checkedID;
    ArrayList<Integer> idices;
    int num = 0;

    //
    public SubActionRAdapter(Context nContext, SubAction []  data) {
        this.nContext = nContext;
        this.data = data;
        checkedID = new ArrayList<>();
        idices = new ArrayList<>();
    }


    public List<String> getId1(){
        ArrayList<String> checkedID =  new ArrayList<>();
        checkedID = this.checkedID;
        return checkedID;
    }

    public List<Integer> getIndice(){
        ArrayList<Integer> indices = new ArrayList<>();
        indices = this.idices;
        return indices;
    }


    public void setId(ArrayList<String> checkedID){
        this.checkedID = checkedID;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.subaction_cell,parent,false);
        MyViewHolder view = new MyViewHolder(v);

        view.cb_complete.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                            if (b) {
                                data[viewType].setComplete(true);
                                checkedID.add(data[viewType].getId());
                                data[viewType].setIndex(viewType);
                                idices.add( data[viewType].getIndex());
                                Log.i("Length",idices.size()+"");

                            } else {
                                data[viewType].setComplete(false);
                                checkedID.remove(data[viewType].getId());
                                idices.remove(data[viewType].getIndex());
                                Log.i("Length",idices.size()+"");
                            }
                    }
                }
        );

        return view;
    }

    // many time
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (data[position].getComplete()){
            //holder.cb_complete.setChecked(data[position].getComplete());
            holder.cb_complete.setEnabled(false);
            holder.tv_comp.setText("Completed");
            holder.cb_complete.setVisibility(View.GONE);
            holder.tv_title.setText((CharSequence) data[position].getTitle());
        }
        else {
            holder.cb_complete.setEnabled(true);
            holder.cb_complete.setVisibility(View.VISIBLE);
            holder.tv_comp.setText("Complete");
            holder.tv_title.setText((CharSequence) data[position].getTitle());

        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.length;
        }
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        CheckBox cb_complete;
        TextView tv_comp;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_subaction_title);
            cb_complete = (CheckBox) itemView.findViewById(R.id.cb_subaction_comp);
            tv_comp = (TextView) itemView.findViewById(R.id.tv_comp);

        }
    }
}
