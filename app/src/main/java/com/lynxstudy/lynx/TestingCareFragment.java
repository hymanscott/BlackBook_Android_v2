package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingCareFragment extends Fragment {

    public TestingCareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_care, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        TextView positiveHIVtest = (TextView)view.findViewById(R.id.positiveHIVtest);
        positiveHIVtest.setTypeface(tf);
        positiveHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =0;
                Intent i = new Intent(getActivity(),TestingCareAnswer.class);
                i.putExtra("id", id);
                startActivityForResult(i, 100);
            }
        });

        TextView negativeHIVtest = (TextView)view.findViewById(R.id.negativeHIVtest);
        negativeHIVtest.setTypeface(tf);
        negativeHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                Intent i = new Intent(getActivity(),TestingCareAnswer.class);
                i.putExtra("id", id);
                startActivityForResult(i, 100);
            }
        });

        return view;
    }
    public void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reloadFragment();
    }
}
