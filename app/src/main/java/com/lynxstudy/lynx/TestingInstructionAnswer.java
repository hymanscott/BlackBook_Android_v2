package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestingInstructions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TestingInstructionAnswer extends AppCompatActivity {


    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_instruction_answer);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.INVISIBLE);
        db = new DatabaseHelper(TestingInstructionAnswer.this);

        TextView qn = (TextView)findViewById(R.id.question);
        qn.setTypeface(tf);
        WebView webView = (WebView)findViewById(R.id.webview);
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        int id = getIntent().getIntExtra("testingInsID",1);

        TestingInstructions instruction = db.getTestingInstruction(id);
        qn.setText(instruction.getQuestion());


        final WebView instruction_ans_wv = new WebView(TestingInstructionAnswer.this);
        final WebView video_view = new WebView(TestingInstructionAnswer.this);
        LinearLayout.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);

        if (instruction.getVideo_link().isEmpty()) {
            if (instruction.getPdf_link().isEmpty()) {
                instruction_ans_wv.loadDataWithBaseURL("",instruction.getAnswer() , "text/html", "utf-8", "");
            } else {
                final String filename = instruction.getPdf_link();
                instruction_ans_wv.loadDataWithBaseURL("",filename , "text/html", "utf-8", "");
                instruction_ans_wv.setBackground(getResources().getDrawable(R.drawable.lynx_button));
                instruction_ans_wv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File fileBrochure = new File(Environment.getExternalStorageDirectory().getPath()+"/"+filename);
                        if (!fileBrochure.exists()) {
                            CopyAssetsbrochure(filename);
                        }

                            /* PDF reader code*/
                        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+filename);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(TestingInstructionAnswer.this, "NO App found. Please install a pdf reader.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            instruction_ans_wv.setPadding(10, 20, 10, 20);
            parentLayout.addView(instruction_ans_wv, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {
            });
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) ((metrics.widthPixels / metrics.density) - 45);
            int height = width * 3 / 5;
            String link = "http://www.youtube.com/embed/";
            String ss = instruction.getVideo_link();
            ss = ss.substring(ss.indexOf("v=") + 2);
            link += ss;
            String html = getHTML(width, height, link);
            webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
            webView.setVisibility(View.VISIBLE);
            /*parentLayout.addView(video_view, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));*/
        }
    }
    public String getHTML(int width,int height, String link) {

        String html = "<iframe class=\"youtube-player\" type=\"text/html5\" width=\""+ width + "\"height=\""+height+
                "\" src=\""+ link +"\"frameborder=\"0\"\"allowfullscreen\"></iframe>";

        return html;
    }

    //method to write the PDFs file to sd card
    private void CopyAssetsbrochure(String filename) {
        AssetManager assetManager = TestingInstructionAnswer.this.getAssets();
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
