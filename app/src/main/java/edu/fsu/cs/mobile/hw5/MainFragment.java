package edu.fsu.cs.mobile.hw5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getCanonicalName();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_main, container, false);


        Button buttonLogin = (Button) v.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).OnFragmentChanged(MainActivity.VIEW_LOGIN);
            }
        });

        Button buttonRegister = (Button) v.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).OnFragmentChanged(MainActivity.VIEW_REGISTER);
            }
        });
        return v;
    }

}
