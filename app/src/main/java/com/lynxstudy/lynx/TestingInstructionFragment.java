package com.lynxstudy.lynx;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.lynx.R;
import com.lynxstudy.model.TestingInstructions;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingInstructionFragment extends Fragment {

    DatabaseHelper db;
    TableLayout testingInsTable;
    public TestingInstructionFragment() {
        // Required empty public constructor
    }
    View view;
    Typeface barlow;
    LinearLayout questionLayout,answerLayout;
    private boolean isAnswerShown = false;
    int back_press_count;
    private  Tracker tracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testing_instruction, container, false);
        db = new DatabaseHelper(getActivity());

        //Type face
        barlow = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        List<TestingInstructions> testing_instruction_list = db.getAllTestingInstruction();
        questionLayout = (LinearLayout) view.findViewById(R.id.questionLayout);
        answerLayout = (LinearLayout) view.findViewById(R.id.answerLayout);
        testingInsTable = (TableLayout) view.findViewById(R.id.testingInsTable);
        testingInsTable.removeAllViews();
        int j = 0;
        for (final TestingInstructions testingInstructions : testing_instruction_list) {
            TableRow testingInsRow = new TableRow(getActivity());
            testingInsRow.setPadding(0, 0, 10, 0);
            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View question_view = chInflater.inflate(R.layout.testing_instruction_row,testingInsRow,false);
            TextView textview = (TextView)question_view.findViewById(R.id.textview);
            textview.setTypeface(barlow);
            textview.setText(testingInstructions.getQuestion());
            testingInsRow.addView(question_view);
            testingInsRow.setBackground(getResources().getDrawable(R.drawable.bottom_border_faq));

            testingInsRow.setClickable(true);
            testingInsRow.setFocusable(true);
            testingInsRow.setId(testingInstructions.getTesting_instruction_id());
            testingInsRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < testingInsTable.getChildCount(); i++) {
                        View row = testingInsTable.getChildAt(i);
                        if (row == v) {

                            /*row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                            ((TextView) ((TableRow) testingInsTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.profile_text_color));
                            Intent selectedPartnerSumm = new Intent(getActivity(), TestingInstructionAnswer.class);
                            int testingInsID = row.getId();
                            selectedPartnerSumm.putExtra("testingInsID", testingInsID);
                            startActivityForResult(selectedPartnerSumm, 100);*/
                            showAnswerLayout(row.getId());
                        }
                    }
                }
            });
            testingInsTable.addView(testingInsRow);
            j++;

        }
        back_press_count = 0;
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isAnswerShown){
                        answerLayout.setVisibility(View.GONE);
                        questionLayout.setVisibility(View.VISIBLE);
                        isAnswerShown = false;
                        back_press_count = 0;
                    }else{
                        if(back_press_count>1){
                            LynxManager.goToIntent(getActivity(),"home",getActivity().getClass().getSimpleName());
                            getActivity().overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            getActivity().finish();
                        }else{
                            back_press_count++;
                        }
                    }
                    return true;
                }
                return false;
            }
        } );
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxtesting/Instructions").title("Lynxtesting/Instructions").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

    public String getHTML(int width,int height, String link) {

        String html = "<iframe class=\"youtube-player\" type=\"text/html5\" width=\""+ width + "\"height=\""+height+
                "\" src=\""+ link +"\"frameborder=\"0\"\"allowfullscreen\"></iframe>";

        return html;
    }

    //method to write the PDFs file to sd card
    private void CopyAssetsbrochure(String filename) {
        AssetManager assetManager = getActivity().getAssets();
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

    public void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reloadFragment();
    }

    public void showAnswerLayout(int id){
        answerLayout.setVisibility(View.VISIBLE);
        questionLayout.setVisibility(View.GONE);
        isAnswerShown = true;
        TextView qn = (TextView)view.findViewById(R.id.question);
        qn.setTypeface(barlow);
        LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.parentLayout);
        TestingInstructions instruction = db.getTestingInstruction(id);
        qn.setText(instruction.getQuestion());
        TrackHelper.track().event("Testing Instruction","View").name(instruction.getQuestion()).with(tracker);

        final WebView instruction_ans_wv = new WebView(getActivity());
        final WebView video_view = new WebView(getActivity());
        LinearLayout.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);

        if (instruction.getVideo_link().isEmpty()) {
            if (instruction.getPdf_link().isEmpty()) {
                instruction_ans_wv.loadDataWithBaseURL("",instruction.getAnswer() , "text/html", "utf-8", "");
                parentLayout.setVisibility(View.VISIBLE);
                parentLayout.removeAllViews();
                parentLayout.addView(instruction_ans_wv, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            } else {
                final String filename = instruction.getPdf_link();
                File fileBrochure = new File(Environment.getExternalStorageDirectory().getPath()+"/"+filename);
                if (!fileBrochure.exists()) {
                    CopyAssetsbrochure(filename);
                    Log.v("PDF File name",filename + " !Exist");
                }else{
                    Log.v("PDF File name",filename + " Exist");
                }
                parentLayout.setVisibility(View.GONE);
                PDFView pdfView = (PDFView) view.findViewById(R.id.pdfView);
                pdfView.setVisibility(View.VISIBLE);
                File file_pdf = new File(Environment.getExternalStorageDirectory().getPath() +"/"+filename);
                pdfView.fromFile(file_pdf)
                        //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        // spacing between pages in dp. To define spacing color, set view background
                        .spacing(0)
                        .load();
            }
        } else {
            video_view.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= 19) {
                video_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }else {
                video_view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            video_view.setWebChromeClient(new WebChromeClient() {
            });
            video_view.getSettings().setPluginState(WebSettings.PluginState.ON);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) ((metrics.widthPixels / metrics.density) - 16);
            int height = width * 3 / 5;
            String link = "http://www.youtube.com/embed/";
            String ss = instruction.getVideo_link();
            ss = ss.substring(ss.indexOf("v=") + 2);
            link += ss;
            String html = getHTML(width, height, link);
            video_view.loadDataWithBaseURL("", html, mimeType, encoding, "");
            parentLayout.setVisibility(View.VISIBLE);
            parentLayout.removeAllViews();
            parentLayout.addView(video_view, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        }
    }
}

