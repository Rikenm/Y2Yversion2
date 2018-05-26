package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.RecyclerViewAdapter;
import com.rikenmaharjan.y2yc.utils.Events;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpComingEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpComingEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpComingEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // custom variable
    View v;
    private RecyclerView newRecycleView;
    private List<Events> lstEvents;




    private OnFragmentInteractionListener mListener;

    public UpComingEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpComingEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpComingEventFragment newInstance(String param1, String param2) {
        UpComingEventFragment fragment = new UpComingEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // data here
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // dummy data

        lstEvents = new ArrayList<>();
        lstEvents.add(new Events("Y2Y demo","Boston","12:00-3:00pm"));
        lstEvents.add(new Events("Y2Y demo","Boston","12:00-3:00pm"));


    }


    // TODO: Update here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_up_coming_event, container, false);
        /*YOUR CODE HERE*/

        newRecycleView = (RecyclerView) v.findViewById(R.id.events_recycleView);

        RecyclerViewAdapter recyclerViewAdapter= new RecyclerViewAdapter(getContext(),lstEvents);
        newRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newRecycleView.setAdapter(recyclerViewAdapter);
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
