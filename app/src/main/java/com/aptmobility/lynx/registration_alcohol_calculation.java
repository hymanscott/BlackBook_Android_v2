package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hariv_000 on 6/17/2015.
 */
public class registration_alcohol_calculation extends Fragment {

    //private Spinner spinner_no_of_drinks;
    public registration_alcohol_calculation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_reg_alcohol_calculation, container, false);

       /* List<String> number_of_drinks = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            number_of_drinks.add(String.valueOf(i));
        }
        spinner_no_of_drinks = (Spinner) view.findViewById(R.id.no_of_drinks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, number_of_drinks);
        spinner_no_of_drinks.setAdapter(adapter);
        */
        return view;
    }
}
