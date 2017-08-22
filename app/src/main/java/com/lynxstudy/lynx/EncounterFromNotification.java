package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lynxstudy.model.Encounter;

public class EncounterFromNotification extends AppCompatActivity {

    Button yes,no;
    TextView encounter_report_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_from_notification);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        encounter_report_title = (TextView)findViewById(R.id.encounter_report_title);
        encounter_report_title.setTypeface(tf);
        yes = (Button)findViewById(R.id.yes);
        yes.setTypeface(tf);
        no = (Button)findViewById(R.id.no);
        no.setTypeface(tf);
        LynxManager.notificationActions = null;
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(EncounterFromNotification.this,LynxHome.class);
                startActivity(home);
                finish();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diary = new Intent(EncounterFromNotification.this,LynxDiary.class);
                diary.putExtra("fromNotification",true);
                startActivity(diary);
                finish();
            }
        });
    }
}
