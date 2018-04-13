package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * Created by Hari on 2017-06-20.
 */

public class RegistrationTimesBottom extends Fragment implements SeekBar.OnSeekBarChangeListener {
    public RegistrationTimesBottom() {
    }
    private SeekBar seek_bartwo;
    private TextView seek_textviewone;
    EditText editText;
    Button regAuthNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reg_times_bottom, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        seek_textviewone = (TextView)view.findViewById(R.id.textProgress_id2);
        TextView frag_title = (TextView) view.findViewById(R.id.frag_title);
        TextView textview9 = (TextView)view.findViewById(R.id.textview9);
        TextView textview10 = (TextView)view.findViewById(R.id.textview10);
        TextView textview11 = (TextView)view.findViewById(R.id.textview11);
        TextView textview12 = (TextView)view.findViewById(R.id.textview12);
        TextView Progress_minvalue2 = (TextView)view.findViewById(R.id.Progress_minvalue2);
        TextView Progress_maxvalue2 = (TextView)view.findViewById(R.id.Progress_maxvalue2);
        editText = (EditText) view.findViewById(R.id.editText);
        regAuthNext = (Button) view.findViewById(R.id.regAuthNext);

        seek_textviewone.setTypeface(tf);
        frag_title.setTypeface(tf_bold);
        textview9.setTypeface(tf);
        textview10.setTypeface(tf);
        textview11.setTypeface(tf);
        textview12.setTypeface(tf);
        Progress_minvalue2.setTypeface(tf);
        Progress_maxvalue2.setTypeface(tf);
        editText.setTypeface(tf);
        regAuthNext.setTypeface(tf_bold);

        // Seek Bar codes
        final int stepSize =10;
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
                //seekBar.setProgress(progress);

                setSeekBarText(progress);
                /*// seek_textviewoneLabel = (TextView) view.findViewById(R.id.textProgress_id1_toplabel);
                seek_textviewone.setText(progress + "%");
                //seek_textviewoneLabel.setText(progress + "%");
                //int seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));
                //int seek_label_pos = seek_bartwo.getThumb().getBounds().left;
                int seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));
                if(progress==80){
                    seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) 75 / 100));
                }else if(progress>=90){
                    seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) 85 / 100));
                }else{
                    seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));
                }
                Log.v("seek_label_pos", String.valueOf(seek_label_pos));
                seek_textviewone.setX(seek_label_pos);*/
            }
        });
        editText.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_bot_hivposs()));

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          String botPercent = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getBottom_condom_use_percent());
                                          seek_textviewone.setText(botPercent);
                                          botPercent = botPercent.replaceAll("\\s+","");
                                          botPercent = botPercent.substring(0, botPercent.length() - 1);
                                          setSeekBarText(Integer.parseInt(botPercent));

                                      }
                                  },
                500);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Timesbottom").title("Baseline/Timesbottom").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

    private void setSeekBarText(int progress) {
        seek_bartwo.setProgress(progress);
        seek_textviewone.setText(progress + " %");
        int seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress / 100));
        if(progress>=90){
            seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) 85 / 100));
        }else if(progress>10){
            progress = progress - (progress/12);
            seek_label_pos = (int) ((float) (seek_bartwo.getMeasuredWidth()) * ((float) progress  / 100));
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
