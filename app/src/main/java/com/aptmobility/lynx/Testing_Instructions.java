package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.TestingInstructions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class Testing_Instructions extends Activity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_instructions);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);

        //TYpe face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        db = new DatabaseHelper(this);
        final LinearLayout testing_instruction = (LinearLayout)findViewById(R.id.testingInstructionLayout);
        testing_instruction.removeAllViews();

        List<TestingInstructions> testing_instruction_list = db.getAllTestingInstruction();
        for (final TestingInstructions Instructions : testing_instruction_list) {
            final Button instruction_title = new Button(this);
            final WebView instruction_ans_wv = new WebView(this);
            final WebView video_view = new WebView(this);

            video_view.setVisibility(View.GONE);

            instruction_title.setTypeface(roboto);

            LinearLayout.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);
            if (Instructions.getVideo_link().isEmpty()) {
                if (Instructions.getPdf_link().isEmpty()) {
                    instruction_ans_wv.loadDataWithBaseURL("",Instructions.getAnswer() , "text/html", "utf-8", "");
                } else {
                    final String filename = Instructions.getPdf_link();
                    instruction_ans_wv.loadDataWithBaseURL("",filename , "text/html", "utf-8", "");
                    instruction_ans_wv.setBackground(getResources().getDrawable(R.drawable.phastt_button));
                    instruction_ans_wv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File fileBrochure = new File(Environment.getExternalStorageDirectory().getPath()+"/"+filename);
                            if (!fileBrochure.exists()) {
                                CopyAssetsbrochure(filename);
                            }

                            /** PDF reader code */
                            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+filename);

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                getApplicationContext().startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplication(), "NO App found. Please install a pdf reader.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } else {
                    video_view.getSettings().setJavaScriptEnabled(true);
                    video_view.setWebChromeClient(new WebChromeClient() {
                    });
                    video_view.getSettings().setPluginState(WebSettings.PluginState.ON);
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    int width = (int) ((metrics.widthPixels / metrics.density) - 45);
                    int height = width * 3 / 5;
                    String link = "http://www.youtube.com/embed/";
                    String ss = Instructions.getVideo_link();
                    ss = ss.substring(ss.indexOf("v=") + 2);
                    link += ss;
                    String html = getHTML(width, height, link);
                    video_view.loadDataWithBaseURL("", html, mimeType, encoding, "");

            }

            instruction_ans_wv.setPadding(10, 10, 10, 10);
            instruction_ans_wv.setVisibility(View.GONE);

            instruction_title.setLayoutParams(params);
            instruction_title.setBackground(getResources().getDrawable(R.drawable.toggle_button));
            instruction_title.setTextColor(getResources().getColor(R.color.orange));
            instruction_title.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
            instruction_title.setCompoundDrawablePadding(20);
            instruction_title.setGravity(Gravity.CENTER_VERTICAL);
            instruction_title.setPadding(10, 10, 10, 10);
            instruction_title.setText(Instructions.getQuestion());
            instruction_title.setTextSize(16);
            instruction_title.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Button buttonView = (Button) v;

                    if (buttonView.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                        // The toggle is enabled
                        buttonView.setSelected(true);
                        instruction_title.setTextColor(Color.parseColor("#ffffff"));
                        instruction_title.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.minus), null, null, null);
                        if (Instructions.getVideo_link().isEmpty()) {
                            instruction_ans_wv.setVisibility(View.VISIBLE);
                        } else {
                            video_view.setVisibility(View.VISIBLE);
                            final String mimeType = "text/html";
                            final String encoding = "UTF-8";
                            DisplayMetrics metrics = getResources().getDisplayMetrics();
                            int width = (int) ((metrics.widthPixels / metrics.density) - 45);
                            int height = width * 3 / 5;
                            String link = "http://www.youtube.com/embed/";
                            String ss = Instructions.getVideo_link();
                            ss = ss.substring(ss.indexOf("v=") + 2);
                            link += ss;
                            String html = getHTML(width, height, link);
                            video_view.loadDataWithBaseURL("", html, mimeType, encoding, "");
                        }

                    } else {
                        // The toggle is disabled
                        buttonView.setSelected(false);
                        instruction_title.setTextColor(getResources().getColor(R.color.orange));
                        instruction_title.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
                        instruction_ans_wv.setVisibility(View.GONE);
                        if (!Instructions.getVideo_link().isEmpty()) {
                            video_view.reload();
                        }
                        video_view.setVisibility(View.GONE);
                    }
                }
            });
            instruction_title.setClickable(true);
            instruction_title.setFocusable(true);

            testing_instruction.addView(instruction_title, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            testing_instruction.addView(instruction_ans_wv, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            testing_instruction.addView(video_view, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        }
    }


    public String getHTML(int width,int height, String link) {

        String html = "<iframe class=\"youtube-player\" type=\"text/html5\" width=\""+ width + "\"height=\""+height+
                "\" src=\""+ link +"\"frameborder=\"0\"\"allowfullscreen\"></iframe>";

        return html;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reg_login, menu);
        MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setEnabled(false);
        settingsMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getVersion() {
        String versionName = "Version not found";

        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.i("Version", "Version Name: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("Version", "Exception Version Name: " + e.getLocalizedMessage());
        }
        return versionName;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause == true){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }
    //method to write the PDFs file to sd card
    private void CopyAssetsbrochure(String filename) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        }
        catch (IOException e)
        {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++)
        {
            String fStr = files[i];
            if(fStr.equalsIgnoreCase(filename))
            {
                InputStream in = null;
                OutputStream out = null;
                try
                {
                    in = assetManager.open(files[i]);
                    //out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + files[i]);
                    out = new FileOutputStream("/sdcard/" + files[i]);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                }
                catch(Exception e)
                {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }



}
