package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;

import java.util.List;

/**
 * Created by bikenmaharjan on 6/2/18.
 */

public class DialogRecycleViewAdapter extends RecyclerView.Adapter<DialogRecycleViewAdapter.MyViewHolder>{


    Context nContext;
    List<HomeRecyclerAdapter.DateAndTitle> data;


    public DialogRecycleViewAdapter(Context context, List<HomeRecyclerAdapter.DateAndTitle> data) {
        this.nContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(nContext).inflate(R.layout.warning_dialog_cell,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (data.size() > 0) {
            holder.title.setText(data.get(position).title);
            holder.date.setText(data.get(position).date);
        }
        else{

            holder.title.setText("No Warning");
            holder.date.setText("N/A");
        }

    }

    //********
    @Override
    public int getItemCount() {
        if (data.size() > 0) {
            return data.size();
        }
        else
        {
            return 1;
        }
    }

    // binding here
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView date;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_dialog_title);
            date = itemView.findViewById(R.id.txt_dialog_date);

        }
    }





}

