package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LYNXPrep extends Activity implements View.OnClickListener {

    LinearLayout btn_sexpro,btn_diary,btn_testing,btn_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynxprep);

        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar);

        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_testing = (LinearLayout) findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_sexpro.setOnClickListener(this);
        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                LynxManager.goToIntent(LYNXPrep.this,"sexpro");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LYNXPrep.this,"testing");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LYNXPrep.this,"diary");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LYNXPrep.this,"chat");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            default:
                break;
        }
    }
}
