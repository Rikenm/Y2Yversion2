package com.rikenmaharjan.y2yc.fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikenmaharjan.y2yc.R;


public class ResetFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public ResetFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ResetFragment newInstance() {
        ResetFragment fragment = new ResetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset, container, false);



    }


    // Replace Fragment
    public void replaceFragment(android.app.Fragment someFragment) {

        //FragmentManager fragmentManager = getFragmentManager(); // static variable
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_base_fragmentContainer, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
