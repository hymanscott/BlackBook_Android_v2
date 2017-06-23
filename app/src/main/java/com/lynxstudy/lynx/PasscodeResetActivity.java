package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasscodeResetActivity extends AppCompatActivity {

    Button reset_passcode;
    TextView frag_title,sec_qn;
    EditText sec_ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_reset);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        reset_passcode = (Button)findViewById(R.id.reset_passcode);
        reset_passcode.setTypeface(tf);
        frag_title = (TextView)findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        sec_qn = (TextView)findViewById(R.id.sec_qn);
        sec_qn.setTypeface(tf);
        sec_ans = (EditText)findViewById(R.id.sec_ans);
        sec_ans.setTypeface(tf);

        String question = getIntent().getStringExtra("question");
        final String answer = getIntent().getStringExtra("answer");

        sec_qn.setText(question);
        reset_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sec_ans.getText().toString().equals(answer)){
                    Intent changePasscode = new Intent(PasscodeResetActivity.this, PasscodeChangePasscode.class);
                    startActivity(changePasscode);
                    finish();
                }else if(sec_ans.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter security answer", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 30);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Securtiy answer is in-valid", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 30);
                    toast.show();
                }
            }
        });
    }
}
