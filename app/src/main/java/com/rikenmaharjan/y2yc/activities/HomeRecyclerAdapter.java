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
import com.rikenmaharjan.y2yc.utils.WarningModel;

import java.util.List;

/**
 * Created by bikenmaharjan on 5/29/18.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>  {

    // change event
    List<StayModel>data;
    List<WarningModel>warningData;

    Context nContext;
    String []colors;

    int[] myImageList = new int[]{R.drawable.bed,R.drawable.bed, R.drawable.calendar,R.drawable.locker,R.drawable.nit};

    Drawable []image;

    Dialog warningDialog;


    public HomeRecyclerAdapter(Context context, List<StayModel> lstStay, List<WarningModel>warningData) {


        this.data = lstStay;
        this.nContext = context;

        this.warningData = warningData;

        colors = new String[5];
        colors[2] = "#56CEFF";
        colors[1] = "#FF8C09";
        colors[3] = "#F4D938";
        colors[0] = "#13A89D";
        colors[4] = "#FF7C3B";
    }

    // returns postion for onCreateViewHolder
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // creates once
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        ///////
        if (viewType !=0) {
            v = LayoutInflater.from(nContext).inflate(R.layout.yourstay_cell, parent, false);
            final MyViewHolder vHolder = new MyViewHolder(v);


            warningDialog = new Dialog(nContext);
            warningDialog.setContentView(R.layout.dialog_warning);


            // ADAPTER HERE
//            vHolder.ll_yourstay.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            Button btn_warning_cancel = (Button) warningDialog.findViewById(R.id.btn_warning_cancel);
//                            // set data here
//
//
//                            int position = vHolder.getAdapterPosition();
//                            if (position == 3) {
//
//
//                                warningDialog.show();
//                                btn_warning_cancel.setOnClickListener(
//
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                warningDialog.dismiss();
//                                            }
//                                        }
//
//                                );
//
//                            }
//
//                        }
//                    }
//            );
            return vHolder;
        }
        else {
            v = LayoutInflater.from(nContext).inflate(R.layout.warning_cell, parent, false);
            final MyViewHolder vHolder = new MyViewHolder(v);

            warningDialog = new Dialog(nContext);
            warningDialog.setContentView(R.layout.dialog_warning);


            vHolder.btn_minor.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.i("homeAdapter","minor");

                        }
                    }
            );

            vHolder.btn_major.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Log.i("homeAdapter","major");
                        }
                    }
            );

            vHolder.btn_susp.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.i("homeAdapter","susp");

                        }
                    }
            );




            return vHolder;

        }
    }



    // for particular cell [HomeRecyclerAdapter.MyViewHolder] <--- static
    // everytime when view gets load
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        if (position !=0) {
            holder.tv_subtitle.setText(data.get(position).getSubtitle());
            holder.tv_title.setText(data.get(position).getTitle());
            holder.cv_stay.setCardBackgroundColor(Color.parseColor(colors[position]));
            holder.imageView.setImageResource(myImageList[position]);
            Log.i("color", "" + colors[position]);
        }
        else{

            String minorNum = conversion((warningData.get(position).getMinorWarning()));
            String majorNum = conversion((warningData.get(position).getMajorWarning()));
            String suspNum = conversion((warningData.get(position).getSuspensionWarning()));

            holder.tv_warning_minor.setText(minorNum);
            holder.tv_warning_major.setText(majorNum);
            holder.tv_warning_susp.setText(suspNum);

        }


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


        private TextView tv_warning_minor;
        private TextView tv_warning_major;
        private TextView tv_warning_susp;
        private LinearLayout ll_warning;
        private Button btn_minor;
        private Button btn_major;
        private Button btn_susp;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            cv_stay = (CardView) itemView.findViewById(R.id.cv_stay);
            ll_yourstay = (LinearLayout) itemView.findViewById(R.id.ll_yourstay_cell);


            // for warning cell

            tv_warning_minor = itemView.findViewById(R.id.txt_warning_minor);
            tv_warning_major = itemView.findViewById(R.id.txt_warning_major);
            tv_warning_susp = itemView.findViewById(R.id.txt_warning_susp);
            ll_warning = itemView.findViewById(R.id.ll_warning_cell);
            btn_minor = itemView.findViewById(R.id.btn_warning_minor);
            btn_major = itemView.findViewById(R.id.btn_warning_major);
            btn_susp = itemView.findViewById(R.id.btn_warning_susp);


        }
    }

    public String conversion(int original){

        if (original < 10 ){
            return "0"+original;
        }
        else{
            return ""+original;
        }

    }

}
