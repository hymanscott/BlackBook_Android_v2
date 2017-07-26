package com.lynxstudy.lynx;


import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.lynx.R;
import com.lynxstudy.lynx.TestingInstructionAnswer;
import com.lynxstudy.model.TestingInstructions;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_instruction, container, false);
        db = new DatabaseHelper(getActivity());

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        List<TestingInstructions> testing_instruction_list = db.getAllTestingInstruction();

        testingInsTable = (TableLayout) view.findViewById(R.id.testingInsTable);
        testingInsTable.removeAllViews();
        int j = 0;
        for (final TestingInstructions testingInstructions : testing_instruction_list) {
            TableRow testingInsRow = new TableRow(getActivity());
            testingInsRow.setPadding(0, 30, 10, 30);

            TextView testingInsName = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            testingInsName.setText(testingInstructions.getQuestion());
            testingInsName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            testingInsName.setLayoutParams(params);
            testingInsName.setTypeface(roboto);
            testingInsName.setTextColor(getResources().getColor(R.color.faq_blue));
            testingInsName.setPadding(40, 10, 10, 10);
            testingInsName.setTextSize(16);

            testingInsRow.addView(testingInsName);
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

                            row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                            ((TextView) ((TableRow) testingInsTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.profile_text_color));
                            Intent selectedPartnerSumm = new Intent(getActivity(), TestingInstructionAnswer.class);
                            int testingInsID = row.getId();
                            selectedPartnerSumm.putExtra("testingInsID", testingInsID);
                            startActivityForResult(selectedPartnerSumm, 100);

                        } else {
                            ((TextView) ((TableRow) testingInsTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.faq_blue));
                            row.setBackground(getResources().getDrawable(R.drawable.bottom_border_faq));
                        }
                    }
                }
            });
            testingInsTable.addView(testingInsRow);
            j++;

        }
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
}

