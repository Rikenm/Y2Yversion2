package com.rikenmaharjan.y2yc.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    int[] myImageList = new int[]{R.drawable.bed, R.drawable.calendar,R.drawable.locker,R.drawable.warning,R.drawable.nit};

    Drawable []image;

    Dialog warningDialog;


    public HomeRecyclerAdapter(Context context, List<StayModel> lstStay) {


        this.data = lstStay;
        this.nContext = context;
        colors = new String[5];
        colors[0] = "#56CEFF";
        colors[1] = "#FF8C09";
        colors[2] = "#F4D938";
        colors[3] = "#13A89D";
        colors[4] = "#FF7C3B";




    }

    // creates once
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(nContext).inflate(R.layout.yourstay_cell,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);


        warningDialog = new Dialog(nContext);
        warningDialog.setContentView(R.layout.dialog_warning);



        vHolder.ll_yourstay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Button btn_warning_cancel = (Button) warningDialog.findViewById(R.id.btn_warning_cancel);
                        // set data here


                        int position = vHolder.getAdapterPosition();
                        if (position == 3){


                                warningDialog.show();
                            btn_warning_cancel.setOnClickListener(

                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            warningDialog.dismiss();
                                        }
                                    }

                            );

                        }

                    }
                }
        );






        return vHolder;
    }



    // for particular cell [HomeRecyclerAdapter.MyViewHolder] <--- static
    // everytime when view gets load
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.tv_subtitle.setText(data.get(position).getSubtitle());
        holder.tv_title.setText(data.get(position).getTitle());
        holder.cv_stay.setCardBackgroundColor(Color.parseColor(colors[position]));



        holder.imageView.setImageResource(myImageList[position]);
        Log.i("color",""+colors[position]);


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
        private LinearLayout ll_yourstay;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            cv_stay = (CardView) itemView.findViewById(R.id.cv_stay);
            ll_yourstay = (LinearLayout) itemView.findViewById(R.id.ll_yourstay_cell);


        }
    }



}
