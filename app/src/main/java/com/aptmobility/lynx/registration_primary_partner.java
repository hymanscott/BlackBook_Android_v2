package com.aptmobility.lynx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hariv_000 on 6/16/2015.
 */
public class registration_primary_partner extends Fragment implements SeekBar.OnSeekBarChangeListener {

//    private Spinner spinner_as_top;
  //  private Spinner spinner_as_bottom;
    private String[] number;
    private SeekBar seek_barone;
    private SeekBar seek_bartwo;
    private TextView seek_textviewtwo;
    private TextView seek_textviewtwoLabel;
    private TextView seek_textviewone;
    private TextView seek_textviewoneLabel;
    public registration_primary_partner() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_reg_primary_partner, container, false);

        List<String> number_of_partners = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            number_of_partners.add(String.valueOf(i));
        }

        //Textwatcher Demo
        /*final EditText datetextwatcher = (EditText)view.findViewById(R.id.datetextwatcher);
        datetextwatcher.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "MMDDYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 6){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,6));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        *//*SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
                        String date = df.format(cal.getTime());
                        int current_year = Integer.parseInt(date.substring(date.length()-2,date.length()));
                        if(year > current_year) year = current_year;*//*
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 6));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    datetextwatcher.setText(current);
                    datetextwatcher.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        number = getResources().getStringArray(R.array.numbers_20);
        ArrayAdapter<String> adapterNum = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, number);

        /*//As top spinner
        spinner_as_top = (Spinner) view.findViewById(R.id.number_as_top);
        spinner_as_top.setAdapter(adapterNum);

        //As bottom spinner
        spinner_as_bottom = (Spinner) view.findViewById(R.id.number_as_bottom);
        spinner_as_bottom.setAdapter(adapterNum);
        */

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
