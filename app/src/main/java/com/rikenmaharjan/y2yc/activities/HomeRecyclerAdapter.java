package com.rikenmaharjan.y2yc.activities;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.Events;
import com.rikenmaharjan.y2yc.utils.StayModel;

import java.util.List;

/**
 * Created by bikenmaharjan on 5/29/18.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>  {

    // change event
    List<StayModel>data;

    Context nContext;
    String []colors;


    public HomeRecyclerAdapter(Context context, List<StayModel> lstStay) {


        this.data = lstStay;
        this.nContext = context;
        colors = new String[5];
        colors[0] = "56CEFF";
        colors[1] = "FF8C09";
        colors[2] = "F4D938";
        colors[3] = "13A89D";
        colors[4] = "FF7C3B";


    }

    // creates once
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(nContext).inflate(R.layout.yourstay_cell,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }



    // for particular cell [HomeRecyclerAdapter.MyViewHolder] <--- static
    // everytime when view gets load
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.tv_subtitle.setText(data.get(position).getSubtitle());
        holder.tv_title.setText(data.get(position).getTitle());
        
        
        //holder.cv_stay.setBackgroundColor(colors.length);
        //holder.imageView.set
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    // general binding
    public static class MyViewHolder extends RecyclerView.ViewHolder {



        private TextView tv_title;
        private TextView tv_subtitle;
        private ImageView imageView;
        private CardView cv_stay;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            cv_stay = (CardView) itemView.findViewById(R.id.cv_stay);


        }
    }



}