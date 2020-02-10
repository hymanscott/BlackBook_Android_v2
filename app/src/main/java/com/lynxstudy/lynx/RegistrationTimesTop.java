package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * Created by Hari on 2017-06-20.
 */

public class RegistrationTimesTop extends Fragment implements SeekBar.OnSeekBarChangeListener{
    public RegistrationTimesTop() {
    }

    private SeekBar seek_barone;
    private TextView seek_textviewone;
    EditText editText;
    Button regAuthNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reg_times_top, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        seek_textviewone = (TextView)view.findViewById(R.id.textProgress_id1);
        TextView progress_minvalue2 = (TextView) view.findViewById(R.id.Progress_minvalue2);
        TextView progress_maxvalue2 = (TextView) view.findViewById(R.id.Progress_maxvalue2);
        editText = (EditText) view.findViewById(R.id.editText);
        regAuthNext = (Button) view.findViewById(R.id.regAuthNext);

        seek_textviewone.setTypeface(tf);
        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_medium);
        ((TextView) view.findViewById(R.id.textview9)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.textview10)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.textview11)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.textview12)).setTypeface(tf);
        progress_minvalue2.setTypeface(tf);
        progress_maxvalue2.setTypeface(tf);
        editText.setTypeface(tf);
        regAuthNext.setTypeface(tf_bold);
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
                //seekBar.setProgress(progress);

                setSeekBarText(progress);
            }


        });
        editText.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_top_hivposs()));


        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          String topPercent = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getTop_condom_use_percent());
                                          seek_textviewone.setText(topPercent);
                                          topPercent = topPercent.replaceAll("\\s+","");
                                          topPercent = topPercent.substring(0, topPercent.length() - 1);
                                          setSeekBarText(Integer.parseInt(topPercent));

                                      }
                                  },
                500);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Timestop").title("Baseline/Timestop").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

    private void setSeekBarText(int progress) {
        seek_barone.setProgress(progress);
        seek_textviewone.setText(progress + " %");
        int seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress / 100));
        if(progress>=90){
            seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) 85 / 100));
        }else if(progress>10){
            progress = progress - (progress/15);
            seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress  / 100));
        }
        seek_textviewone.setX(seek_label_pos);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
