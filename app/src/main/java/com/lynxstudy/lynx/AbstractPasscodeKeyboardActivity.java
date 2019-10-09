package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * Created by Hari on 2017-04-13.
 */

public abstract class AbstractPasscodeKeyboardActivity extends AppCompatActivity {

    protected EditText pinCodeField1 = null;
    protected EditText pinCodeField2 = null;
    protected EditText pinCodeField3 = null;
    protected EditText pinCodeField4 = null;
    protected InputFilter[] filters = null;
    protected TextView topMessage = null;
    TableLayout tableLayout1 ;
    TextView forgotpasscode;
    LinearLayout back;
    private View.OnClickListener defaultButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            int currentValue = -1;
            int id = arg0.getId();
            if (id == R.id.button0) {
                currentValue = 0;
            } else if (id == R.id.button1) {
                currentValue = 1;
            } else if (id == R.id.button2) {
                currentValue = 2;
            } else if (id == R.id.button3) {
                currentValue = 3;
            } else if (id == R.id.button4) {
                currentValue = 4;
            } else if (id == R.id.button5) {
                currentValue = 5;
            } else if (id == R.id.button6) {
                currentValue = 6;
            } else if (id == R.id.button7) {
                currentValue = 7;
            } else if (id == R.id.button8) {
                currentValue = 8;
            } else if (id == R.id.button9) {
                currentValue = 9;
            } else {
            }

            //set the value and move the focus
            String currentValueString = String.valueOf(currentValue);
            if (pinCodeField1.isFocused()) {
                pinCodeField1.setText(currentValueString);
                pinCodeField2.requestFocus();
                pinCodeField2.setText("");
            } else if (pinCodeField2.isFocused()) {
                pinCodeField2.setText(currentValueString);
                pinCodeField3.requestFocus();
                pinCodeField3.setText("");
            } else if (pinCodeField3.isFocused()) {
                pinCodeField3.setText(currentValueString);
                pinCodeField4.requestFocus();
                pinCodeField4.setText("");
            } else if (pinCodeField4.isFocused()) {
                pinCodeField4.setText(currentValueString);
            }

            if (pinCodeField4.getText().toString().length() > 0 &&
                    pinCodeField3.getText().toString().length() > 0 &&
                    pinCodeField2.getText().toString().length() > 0 &&
                    pinCodeField1.getText().toString().length() > 0
                    ) {
                onPinLockInserted();
            }
        }
    };
    private InputFilter onlyNumber = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.length() > 1)
                return "";

            if (source.length() == 0) //erase
                return null;

            try {
                int number = Integer.parseInt(source.toString());
                if ((number >= 0) && (number <= 9))
                    return String.valueOf(number);
                else
                    return "";
            } catch (NumberFormatException e) {
                return "";
            }
        }
    };
    private View.OnTouchListener otl = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v instanceof EditText) {
                ((EditText) v).setText("");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_passcode_keyboard);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Passcode").title("Passcode").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        back = (LinearLayout)findViewById(R.id.back);
        tableLayout1 = (TableLayout)findViewById(R.id.tableLayout1);
        forgotpasscode = (TextView)findViewById(R.id.forgotpasscode);
        forgotpasscode.setTypeface(tf);
        topMessage = (TextView) findViewById(R.id.top_message);
        topMessage.setTypeface(tf);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString("message");
            if (message != null) {
                topMessage.setText(message);
            }

        }

        filters = new InputFilter[2];
        filters[0] = new InputFilter.LengthFilter(1);
        filters[1] = onlyNumber;

        //Setup the pin fields row
        pinCodeField1 = (EditText) findViewById(R.id.pincode_1);
        setupPinItem(pinCodeField1);

        pinCodeField2 = (EditText) findViewById(R.id.pincode_2);
        setupPinItem(pinCodeField2);

        pinCodeField3 = (EditText) findViewById(R.id.pincode_3);
        setupPinItem(pinCodeField3);

        pinCodeField4 = (EditText) findViewById(R.id.pincode_4);
        setupPinItem(pinCodeField4);
        pinCodeField1.setTypeface(tf);
        pinCodeField2.setTypeface(tf);
        pinCodeField3.setTypeface(tf);
        pinCodeField4.setTypeface(tf);

        pinCodeField1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCodeField1.requestFocus();
                forgotpasscode.setVisibility(View.GONE);
                tableLayout1.setVisibility(View.VISIBLE);
            }
        });
        pinCodeField2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCodeField2.requestFocus();
                forgotpasscode.setVisibility(View.GONE);
                tableLayout1.setVisibility(View.VISIBLE);
            }
        });
        pinCodeField3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCodeField3.requestFocus();
                forgotpasscode.setVisibility(View.GONE);
                tableLayout1.setVisibility(View.VISIBLE);
            }
        });
        pinCodeField4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCodeField4.requestFocus();
                forgotpasscode.setVisibility(View.GONE);
                tableLayout1.setVisibility(View.VISIBLE);
            }
        });
        //setup the keyboard
        ((Button) findViewById(R.id.button0)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button0)).setTypeface(tf);
        ((Button) findViewById(R.id.button1)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button1)).setTypeface(tf);
        ((Button) findViewById(R.id.button2)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button2)).setTypeface(tf);
        ((Button) findViewById(R.id.button3)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button3)).setTypeface(tf);
        ((Button) findViewById(R.id.button4)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button4)).setTypeface(tf);
        ((Button) findViewById(R.id.button5)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button5)).setTypeface(tf);
        ((Button) findViewById(R.id.button6)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button6)).setTypeface(tf);
        ((Button) findViewById(R.id.button7)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button7)).setTypeface(tf);
        ((Button) findViewById(R.id.button8)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button8)).setTypeface(tf);
        ((Button) findViewById(R.id.button9)).setOnClickListener(defaultButtonListener);
        ((Button) findViewById(R.id.button9)).setTypeface(tf);
        ((Button) findViewById(R.id.button_erase)).setTypeface(tf);
        ((Button) findViewById(R.id.button_erase)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (pinCodeField1.isFocused()) {

                        } else if (pinCodeField2.isFocused()) {
                            pinCodeField1.requestFocus();
                            pinCodeField1.setText("");
                        } else if (pinCodeField3.isFocused()) {
                            pinCodeField2.requestFocus();
                            pinCodeField2.setText("");
                        } else if (pinCodeField4.isFocused()) {
                            pinCodeField3.requestFocus();
                            pinCodeField3.setText("");
                        }
                    }
                });
        // Hide Buttons //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout1.setVisibility(View.INVISIBLE);
                forgotpasscode.setVisibility(View.VISIBLE);
            }
        });
        forgotpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasscode = new Intent(AbstractPasscodeKeyboardActivity.this, PasscodeResetActivity.class);
                resetPasscode.putExtra("question",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
                resetPasscode.putExtra("answer",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
                startActivityForResult(resetPasscode, 102);
            }
        });
    }

    protected void setupPinItem(EditText item) {
        item.setInputType(InputType.TYPE_NULL);
        item.setFilters(filters);
        item.setOnTouchListener(otl);
        item.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    protected void showPasswordError() {
        Toast toast = Toast.makeText(AbstractPasscodeKeyboardActivity.this, getString(R.string.passcode_wrong_passcode), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
        toast.show();
    }

    protected abstract void onPinLockInserted();

    int onPause_count =0;
    @Override
    public void onBackPressed() {
        // do something on back.
        if (onPause_count > 0) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Are you sure, you want to exit ?");
            alertbox.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    });

            alertbox.setNeutralButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            AlertDialog dialog = alertbox.create();
            dialog.show();
            Button neg_btn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            if (neg_btn != null){
                neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonbluecornered));
                neg_btn.setTextColor(getResources().getColor(R.color.white));
            }

            Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(pos_btn != null) {
                pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonbluecornered));
                pos_btn.setTextColor(getResources().getColor(R.color.white));
            }
        }
        else{
            Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;
        return;
    }



}
