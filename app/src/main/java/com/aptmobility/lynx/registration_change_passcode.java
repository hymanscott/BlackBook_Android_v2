package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aptmobility.helper.DatabaseHelper;

/**
 * Created by hariv_000 on 6/24/2015.
 */
public class registration_change_passcode extends Fragment {
    DatabaseHelper db;

    public registration_change_passcode() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_change_passcode, container, false);
        final EditText passcode = (EditText) view.findViewById(R.id.newPasscode);
        final String strPasscode = passcode.getText().toString();
        final EditText confrim_passcode = (EditText) view.findViewById(R.id.confirmNewPasscode);
        final String strConfirmPasscode = confrim_passcode.getText().toString();
        return view;
    }
}
