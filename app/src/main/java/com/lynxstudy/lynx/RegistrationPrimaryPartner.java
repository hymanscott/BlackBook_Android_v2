package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationPrimaryPartner extends Fragment implements SeekBar.OnSeekBarChangeListener {

    //  private Spinner spinner_as_top;
    //  private Spinner spinner_as_bottom;
    private String[] number;
    private SeekBar seek_barone;
    private SeekBar seek_bartwo;
    private TextView seek_textviewtwo;
    private TextView seek_textviewtwoLabel;
    private TextView seek_textviewone;
    private TextView seek_textviewoneLabel;
    public RegistrationPrimaryPartner() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    TextView textView8,textView7,textview9,textview10,textview11,textview12,textview13,textview14,textview15,textProgress_id1,Progress_minvalue1;
    TextView Progress_maxvalue1,textview16,textview17,textview18,textProgress_id2,Progress_minvalue2,Progress_maxvalue2,textview20;
    RadioButton regPrepYes,regPrepNo,PSP_Yes,PSP_No;
    Button primary_partner_nextbtn;
    EditText negativePartners,positivePartners,unknownPartners,number_as_top,number_as_bottom;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_registration_primary_partner, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        textView8 = (TextView)view.findViewById(R.id.textView8);
        textView8.setTypeface(tf);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview9.setTypeface(tf);
        textview10 = (TextView)view.findViewById(R.id.textview10);
        textview10.setTypeface(tf);
        textView7 = (TextView)view.findViewById(R.id.textView7);
        textView7.setTypeface(tf);
        textview11 = (TextView)view.findViewById(R.id.textview11);
        textview11.setTypeface(tf);
        textview12 = (TextView)view.findViewById(R.id.textview12);
        textview12.setTypeface(tf);
        textview13 = (TextView)view.findViewById(R.id.textview13);
        textview13.setTypeface(tf);
        textview14 = (TextView)view.findViewById(R.id.textview14);
        textview14.setTypeface(tf);
        textview15 = (TextView)view.findViewById(R.id.textview15);
        textview15.setTypeface(tf);
        textProgress_id1 = (TextView)view.findViewById(R.id.textProgress_id1);
        textProgress_id1.setTypeface(tf);
        Progress_minvalue1 = (TextView)view.findViewById(R.id.Progress_minvalue1);
        Progress_minvalue1.setTypeface(tf);
        Progress_maxvalue1 = (TextView)view.findViewById(R.id.Progress_maxvalue1);
        Progress_maxvalue1.setTypeface(tf);
        textview16 = (TextView)view.findViewById(R.id.textview16);
        textview16.setTypeface(tf);
        textview17 = (TextView)view.findViewById(R.id.textview17);
        textview17.setTypeface(tf);
        textview18 = (TextView)view.findViewById(R.id.textview18);
        textview18.setTypeface(tf);
        textview20 = (TextView)view.findViewById(R.id.textview20);
        textview20.setTypeface(tf);
        textProgress_id2 = (TextView)view.findViewById(R.id.textProgress_id2);
        textProgress_id2.setTypeface(tf);
        Progress_minvalue2 = (TextView)view.findViewById(R.id.Progress_minvalue2);
        Progress_minvalue2.setTypeface(tf);
        Progress_maxvalue2 = (TextView)view.findViewById(R.id.Progress_maxvalue2);
        Progress_maxvalue2.setTypeface(tf);
        regPrepYes = (RadioButton)view.findViewById(R.id.regPrepYes);
        regPrepYes.setTypeface(tf);
        regPrepNo = (RadioButton)view.findViewById(R.id.regPrepNo);
        regPrepNo.setTypeface(tf);
        PSP_Yes = (RadioButton)view.findViewById(R.id.PSP_Yes);
        PSP_Yes.setTypeface(tf);
        PSP_No = (RadioButton)view.findViewById(R.id.PSP_No);
        PSP_No.setTypeface(tf);
        primary_partner_nextbtn = (Button)view.findViewById(R.id.primary_partner_nextbtn);
        primary_partner_nextbtn.setTypeface(tf);
        negativePartners = (EditText)view.findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf);
        positivePartners = (EditText)view.findViewById(R.id.positivePartners);
        positivePartners.setTypeface(tf);
        unknownPartners = (EditText)view.findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf);
        number_as_top = (EditText)view.findViewById(R.id.number_as_top);
        number_as_top.setTypeface(tf);
        number_as_bottom = (EditText)view.findViewById(R.id.number_as_bottom);
        number_as_bottom.setTypeface(tf);

        List<String> number_of_partners = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            number_of_partners.add(String.valueOf(i));
        }

        number = getResources().getStringArray(R.array.numbers_20);
        ArrayAdapter<String> adapterNum = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, number);


        // Seek Bar codes
        final int stepSize =10;
        seek_barone = (SeekBar) view.findViewById(R.id.seekBar_one); // make seekbar object
        seek_barone.setOnSeekBarChangeListener(this);
        seek_barone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                seek_textviewone = (TextView) view.findViewById(R.id.textProgress_id1);
                // seek_textviewoneLabel = (TextView) view.findViewById(R.id.textProgress_id1_toplabel);
                seek_textviewone.setText(progress + "%");
                //seek_textviewoneLabel.setText(progress + "%");
                //int seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress / 100));
                int seek_label_pos = seek_barone.getThumb().getBounds().left;
                seek_textviewone.setX(seek_label_pos);
            }
        });
        seek_bartwo = (SeekBar) view.findViewById(R.id.seekBar_two); // make seekbar object
        seek_bartwo.setOnSeekBarChangeListener(this);
        seek_bartwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                seek_textviewtwo = (TextView) view.findViewById(R.id.textProgress_id2);
                //seek_textviewtwoLabel = (TextView) view.findViewById(R.id.textProgress_id2_toplabel);
                seek_textviewtwo.setText(progress + "%");
                //seek_textviewtwoLabel.setText(progress + "%");
                /*int seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));*/
                int seek_label_pos = seek_bartwo.getThumb().getBounds().left;
                seek_textviewtwo.setX(seek_label_pos);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        //    inflater.inflate(R.menu.listing_menu_create, menu);
        //   MenuItem login_menu_item = menu.findItem(R.id.action_login);
        //   login_menu_item.setVisible(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //    (getActivity()).getActionBar().setTitle("Create Listing");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seek_barone) {
            seek_textviewone.setText(progress + "%");
            int seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress / 100));
            seek_textviewone.setX(seek_label_pos);
        } else if (seekBar == seek_bartwo) {
            seek_textviewtwo.setText(progress + "%");
            int seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));
            seek_textviewtwo.setX(seek_label_pos);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
}
