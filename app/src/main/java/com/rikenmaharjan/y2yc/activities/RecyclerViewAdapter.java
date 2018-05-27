package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.Events;
import java.util.List;

/**
 * Created by bikenmaharjan on 5/25/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    Context nContext;
    List<Events> data;

    public RecyclerViewAdapter(Context context, List<Events> lstEvents) {


        this.data = lstEvents;
        this.nContext = context;
    }

    ////////// Generic class ////////////////////

    // TODO: Your code here
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v;

        v = LayoutInflater.from(nContext).inflate(R.layout.event_cell,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_location.setText(data.get(position).getLocation());
        holder.tv_time.setText(data.get(position).getTime());
        holder.tv_title.setText(data.get(position).getTitle());

    }

    @Override
    public int getItemCount() {

        return data.size();
    }


    // all the initially binding goes
    // TODO: DO binding
    public static class MyViewHolder extends RecyclerView.ViewHolder {



        private TextView tv_title;
        private TextView tv_location;
        private TextView tv_time;

        public MyViewHolder(View itemView){
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.title);
            tv_location = (TextView) itemView.findViewById(R.id.location);
            tv_time = (TextView) itemView.findViewById(R.id.time);



        }

    }

}
