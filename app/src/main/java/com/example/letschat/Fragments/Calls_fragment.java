package com.example.letschat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
//import com.example.letschat.Activities.createroom_activity;
//import com.example.letschat.Activities.join_room_activity;
import com.example.letschat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calls_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calls_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Calls_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calls_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Calls_fragment newInstance(String param1, String param2) {
        Calls_fragment fragment = new Calls_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_calls_fragment, container, false);
        Button create_room= view.findViewById(R.id.createroom);
        Button join_room= view.findViewById(R.id.joinroom);
        /*create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createroomupdateDetail();
            }
        });
        join_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinroom();
            }
        });
*/
        return view;
    }

    /*public void createroomupdateDetail() {
        Intent intent = new Intent(getActivity(), createroom_activity.class);
        startActivity(intent);
    }
    public void joinroom(){
        Intent intent = new Intent(getActivity(), join_room_activity.class);
        startActivity(intent);
    }*/

}