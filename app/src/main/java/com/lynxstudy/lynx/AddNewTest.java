package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestNameMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AddNewTest extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper db;
    String title="";
    ImageView hivAttachment,gonorrheaAttachment,syphilisAttachment,chlamydiaAttachment;
    FrameLayout  hivTestImage,gonorrheaImage,syphilisImage,chlamydiaImage;
    int currentAttachmentId=0;
    String hivImageName="",gonorrheaImageName="",syphilisImageName="",chlamydiaImageName="";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_CAPTURE = 2;
    private static final int RESULT_OK = -1;
    private static final int PICK_IMAGE_REQUEST_AFTER_KITKAT = 5;
    private static final int KITKAT_API_VERSION = 19;
    private static final int READ_WRITE_PERMISSION = 100;
    TextView titleText;
    EditText addNewTestDate;
    RadioButton chlamydiaYes,chlamydiaNo,chlamydiaDidntTest;
    Button add_new_test_ok;
    LinearLayout bot_nav;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);
        // Typeface //
        Typeface ty = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface ty_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-BoldItalic.ttf");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tab1 = new TextView(AddNewTest.this);
        tab1.setText("HISTORY");
        tab1.setTextColor(getResources().getColor(R.color.text_color));
        tab1.setTypeface(ty);
        tab1.setTextSize(16);
        TextView tab2 = new TextView(AddNewTest.this);
        tab2.setText("TEST KIT");
        tab2.setTextColor(getResources().getColor(R.color.text_color));
        tab2.setTypeface(ty);
        tab2.setTextSize(16);
        TextView tab3 = new TextView(AddNewTest.this);
        tab3.setText("LOCATIONS");
        tab3.setTextColor(getResources().getColor(R.color.text_color));
        tab3.setTypeface(ty);
        tab3.setTextSize(16);
        TextView tab4 = new TextView(AddNewTest.this);
        tab4.setText("INSTRUCTIONS");
        tab4.setTextColor(getResources().getColor(R.color.text_color));
        tab4.setTypeface(ty);
        tab4.setTextSize(16);
        TextView tab5 = new TextView(AddNewTest.this);
        tab5.setText("CARE");
        tab5.setTextColor(getResources().getColor(R.color.text_color));
        tab5.setTypeface(ty);
        tab5.setTextSize(16);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.getTabAt(3).setCustomView(tab4);
        tabLayout.getTabAt(4).setCustomView(tab5);

        titleText = (TextView)findViewById(R.id.titleText);
        titleText.setTypeface(ty);
        ((TextView)findViewById(R.id.hivPosQn)).setTypeface(ty);
        ((TextView)findViewById(R.id.stdTesPosQn)).setTypeface(ty);
        ((TextView)findViewById(R.id.gonorrheaTitle)).setTypeface(ty_bold_italic);
        ((TextView)findViewById(R.id.syphilisTitle)).setTypeface(ty_bold_italic);
        ((TextView)findViewById(R.id.chlamydiaTitle)).setTypeface(ty_bold_italic);

        addNewTestDate = (EditText)findViewById(R.id.addNewTestDate);
        addNewTestDate.setTypeface(ty);
        ((RadioButton)findViewById(R.id.hivTestYes)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.hivTestNo)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.hivTestDidntTest)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.gonorrheaYes)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.gonorrheaNo)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.gonorrheaDidntTest)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.syphilisYes)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.syphilisNo)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.syphilisDidntTest)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.chlamydiaYes)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.chlamydiaNo)).setTypeface(ty);
        ((RadioButton)findViewById(R.id.chlamydiaDidntTest)).setTypeface(ty);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(AddNewTest.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(AddNewTest.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewTest.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        db= new DatabaseHelper(AddNewTest.this);
        title = getIntent().getStringExtra("testname");
        TextView newTest_title = (TextView) findViewById(R.id.addNewTestTitle);
        newTest_title.setTypeface(ty);
        newTest_title.setText("Add New " + title);
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("When was your most recent HIV test?");
        LinearLayout std_layout = (LinearLayout)findViewById(R.id.std_layout);
        RadioGroup hivTestStatus = (RadioGroup)findViewById(R.id.hivTestStatus);
        LinearLayout hivTestStatusTitle = (LinearLayout)findViewById(R.id.hivTestStatusTitle);
        RelativeLayout hivTestStatusRadio = (RelativeLayout)findViewById(R.id.hivTestStatusRadio);
        if(title.equals("STD Test")){
            std_layout.setVisibility(View.VISIBLE);
            hivTestStatus.setVisibility(View.GONE);
            hivTestStatusTitle.setVisibility(View.GONE);
            hivTestStatusRadio.setVisibility(View.GONE);
            titleText.setText("When was your most recent STD test?");
        }
        addNewTestDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1800) ? 1800 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    addNewTestDate.setText(current);
                    addNewTestDate.setSelection(sel < current.length() ? sel : current.length());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                addNewTestDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        addNewTestDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddNewTest.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TestNameMaster testNameMaster = db.getTestingNamebyName(title);
        final int testing_id = testNameMaster.getTesting_id();

        add_new_test_ok = (Button) findViewById(R.id.addNewTestOk);
        add_new_test_ok.setTypeface(ty);
        add_new_test_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean invalid_date = LynxManager.regDateValidation(addNewTestDate.getText().toString());
                if (addNewTestDate.getText().toString().isEmpty()) {
                    Toast.makeText(AddNewTest.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(invalid_date){
                    Toast.makeText(AddNewTest.this,"Invalid Date",Toast.LENGTH_SHORT).show();
                }
                else {
                    String date = LynxManager.getFormatedDate("MM/dd/yyyy",addNewTestDate.getText().toString(),"yyyy-MM-dd");
                    TestingHistory history = new TestingHistory(testing_id, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(date), String.valueOf(R.string.statusUpdateNo), true);
                    int testingHistoryid = db.createTestingHistory(history);
                    RadioButton chlamydia = (RadioButton)findViewById(((RadioGroup) findViewById(R.id.chlamydia)).getCheckedRadioButtonId());
                    RadioButton gonorrhea = (RadioButton)findViewById(((RadioGroup)findViewById(R.id.gonorrhea)).getCheckedRadioButtonId());
                    RadioButton syphilis = (RadioButton)findViewById(((RadioGroup)findViewById(R.id.syphilis)).getCheckedRadioButtonId());
                    RadioButton hivTestStatus = (RadioButton)findViewById(((RadioGroup)findViewById(R.id.hivTestStatus)).getCheckedRadioButtonId());
                    // Adding testing history info
                    if (title.equals("STD Test")){
                        for(int sti_count =1 ; sti_count<=3;sti_count++){
                            String test_status;
                            String full_path="";
                            String name="";
                            switch (sti_count){
                                case 1:
                                    test_status = LynxManager.encryptString(gonorrhea.getText().toString());
                                    name = gonorrheaImageName.substring(gonorrheaImageName.lastIndexOf("/") + 1);
                                    full_path = gonorrheaImageName;
                                    break;
                                case 2:
                                    test_status = LynxManager.encryptString(syphilis.getText().toString());
                                    name = syphilisImageName.substring(syphilisImageName.lastIndexOf("/") + 1);
                                    full_path = syphilisImageName;
                                    break;
                                case 3:
                                    test_status = LynxManager.encryptString(chlamydia.getText().toString());
                                    name = chlamydiaImageName.substring(chlamydiaImageName.lastIndexOf("/") + 1);
                                    full_path = chlamydiaImageName;
                                    break;
                                default:
                                    test_status = "";
                                    name="";
                                    full_path="";
                            }

                            TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid , LynxManager.getActiveUser().getUser_id(),sti_count,test_status,LynxManager.encryptString(name),String.valueOf(R.string.statusUpdateNo),true);
                            int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                            uploadMultipart(full_path,name); // fullpath,imagename
                        }
                    }else{
                        String path = hivImageName.substring(hivImageName.lastIndexOf("/") + 1);
                        String test_status = LynxManager.encryptString(hivTestStatus.getText().toString());
                        TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid , LynxManager.getActiveUser().getUser_id(),0,test_status,LynxManager.encryptString(path),String.valueOf(R.string.statusUpdateNo),true);
                        int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                        uploadMultipart(hivImageName,path); // fullpath,imagename
                    }
                    Toast.makeText(AddNewTest.this, "New "+ title +" Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        // Attachment //
        hivTestImage = (FrameLayout) findViewById(R.id.hivTestImage);
        hivTestImage.setOnClickListener(AddNewTest.this);
        gonorrheaImage = (FrameLayout) findViewById(R.id.gonorrheaImage);
        gonorrheaImage.setOnClickListener(AddNewTest.this);
        syphilisImage = (FrameLayout) findViewById(R.id.syphilisImage);
        syphilisImage.setOnClickListener(AddNewTest.this);
        chlamydiaImage = (FrameLayout) findViewById(R.id.chlamydiaImage);
        chlamydiaImage.setOnClickListener(AddNewTest.this);

        hivAttachment = (ImageView) findViewById(R.id.hivAttachment);
        hivAttachment.setTag(0);
        gonorrheaAttachment = (ImageView) findViewById(R.id.gonorrheaAttachment);
        gonorrheaAttachment.setTag(0);
        syphilisAttachment = (ImageView) findViewById(R.id.syphilisAttachment);
        syphilisAttachment.setTag(0);
        chlamydiaAttachment = (ImageView) findViewById(R.id.chlamydiaAttachment);
        chlamydiaAttachment.setTag(0);
        bot_nav = (LinearLayout)findViewById(R.id.bot_nav);
        bot_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddNewTest.this,"Press back to exit from Test Screen",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hivTestImage:
                currentAttachmentId = R.id.hivAttachment;
                startImageInent(hivAttachment);
                break;
            case R.id.gonorrheaImage:
                currentAttachmentId = R.id.gonorrheaAttachment;
                startImageInent(gonorrheaAttachment);
                break;
            case R.id.syphilisImage:
                currentAttachmentId = R.id.syphilisAttachment;
                startImageInent(syphilisAttachment);
                break;
            case R.id.chlamydiaImage:
                currentAttachmentId = R.id.chlamydiaAttachment;
                startImageInent(chlamydiaAttachment);
                break;
        }
    }
    public void startImageInent(final ImageView attachment){
        int tag = (int) attachment.getTag();
        if (tag == 0) {

            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewTest.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, PICK_IMAGE_CAPTURE);
                        attachment.setTag(1);
                    } else if (items[item].equals("Choose from Library")) {
                        if (Build.VERSION.SDK_INT < KITKAT_API_VERSION) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST);

                        } else {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            Uri uriImagePath = getTempUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImagePath);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST_AFTER_KITKAT);
                        }
                        attachment.setTag(1);

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else if (tag == 1) {
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Remove Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewTest.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, PICK_IMAGE_CAPTURE);
                        attachment.setTag(1);
                    } else if (items[item].equals("Choose from Library")) {
                        if (Build.VERSION.SDK_INT < KITKAT_API_VERSION) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST);

                        } else {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            Uri uriImagePath = getTempUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST_AFTER_KITKAT);
                        }
                        attachment.setTag(1);
                    } else if (items[item].equals("Remove Image")) {
                        attachment.setTag(0);
                        //attachment.setVisibility(View.GONE);
                        attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                        switch (currentAttachmentId){
                            case R.id.hivAttachment:
                                hivImageName = "";
                                break;
                            case R.id.gonorrheaAttachment:
                                gonorrheaImageName= "";
                                break;
                            case R.id.syphilisAttachment:
                                syphilisImageName= "";
                                break;
                            case R.id.chlamydiaAttachment:
                                chlamydiaImageName= "";
                                break;
                        }
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddNewTest.this, "Read/Write Access Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(AddNewTest.this, "Read/Write Access Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ImageView attachment = (ImageView)findViewById(R.id.hivAttachment);
        String imagepath="";
        switch (currentAttachmentId){
            case R.id.hivAttachment:
                attachment =  (ImageView)findViewById(R.id.hivAttachment);
                break;
            case R.id.gonorrheaAttachment:
                attachment =  (ImageView)findViewById(R.id.gonorrheaAttachment);
                break;
            case R.id.syphilisAttachment:
                attachment =  (ImageView)findViewById(R.id.syphilisAttachment);
                break;
            case R.id.chlamydiaAttachment:
                attachment =  (ImageView)findViewById(R.id.chlamydiaAttachment);
                break;
        }

        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    Drawable d = null;
                    try {
                        bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        d =new BitmapDrawable(bitmap);

                    } catch(IOException ie) {

                        // messageText.setText("Error");
                    }
                    String realPath = getRealPathFromURI(selectedImage);
                    File photoFile = new File(realPath);
                    try {
                        FileOutputStream out = new FileOutputStream(photoFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    attachment.setTag(1);
                    imagepath = realPath;
                }else{
                    attachment.setTag(0);
                   // attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }
                break;
            case PICK_IMAGE_REQUEST_AFTER_KITKAT:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream input = null;
                    OutputStream output = null;
                    File photoFile = null;
                    try {
                        //converting the input stream into file to crop the
                        //selected image from sd-card.
                        input = getContentResolver().openInputStream(selectedImage);
                        try {
                            photoFile = new File(LynxManager.getInstance().getLastImageName());
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                        output = new FileOutputStream(photoFile);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = input.read(bytes)) != -1) {
                            try {
                                output.write(bytes, 0, read);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(LynxManager.getInstance().getLastImageName());
                    try {
                        FileOutputStream out = new FileOutputStream(photoFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap resized = ThumbnailUtils.extractThumbnail(bitmap,400,400);
                    Drawable d =new BitmapDrawable(resized);


                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = LynxManager.getInstance().getLastImageName();
                    attachment.setTag(1);
                }else{
                    attachment.setTag(0);
                    //attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }

                break;
            case PICK_IMAGE_CAPTURE:

                if(resultCode == RESULT_OK) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    String outputFile = null;
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    String fileName = "/"+LynxManager.getInstance().getActiveUser().getUser_id()+"_" + LynxManager.getTimeStamp() + ".jpg";
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                    //File destination = new File(getActivity().getFilesDir().getAbsolutePath()+"/LYNX/Media/Images/",
                    //  System.currentTimeMillis() + ".jpg");
                    File file = new File(String.valueOf(outputFile));
                    System.out.println(file.getAbsolutePath());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(file + fileName);
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap resized = ThumbnailUtils.extractThumbnail(thumbnail,400,400);
                    Drawable d =new BitmapDrawable(resized);
                    //Drawable d = new BitmapDrawable(thumbnail);
                    //Log.v("FileComplatePath",file.getAbsolutePath() + fileName);
                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = file.getAbsolutePath() + fileName;
                    attachment.setTag(1);

                }else{
                    attachment.setTag(0);
                    //attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }
                break;
        }
        switch (currentAttachmentId){
            case R.id.hivAttachment:
                hivImageName = imagepath;
                break;
            case R.id.gonorrheaAttachment:
                gonorrheaImageName = imagepath;
                break;
            case R.id.syphilisAttachment:
                syphilisImageName = imagepath;
                break;
            case R.id.chlamydiaAttachment:
                chlamydiaImageName = imagepath;
                break;
        }
        //Log.v("ImageName",imagepath);
    }
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }
    private File getTempFile() {


        String fileName = LynxManager.getActiveUser().getUser_id()+"_" + LynxManager.getTimeStamp() +".jpg";
        // Check that the SDCard is mounted


        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
        File mediaFile = new File(outputFile);
        if(!mediaFile.exists()){

            //Log.v("Temp Media", mediaFile.getAbsolutePath());
            mediaFile.mkdirs();
        }

        // For unique image file name appending current timeStamp with file name
        mediaFile = new File(outputFile + fileName);

        LynxManager.getInstance().setLastImageName(outputFile + fileName);
        return mediaFile;
    }
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.MediaColumns.DATA};

        android.database.Cursor cursor = managedQuery(contentUri,

                proj,     // Which columns to return

                null,     // WHERE clause; which rows to return (all rows)
                null,     // WHERE clause selection arguments (none)
                null);     // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart(String path,String name) {
        // URL //
        String url = LynxManager.getBaseURL()+"upload.php";
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, url)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            // To Enable Notification bar for upload //
            //.setNotificationConfig(new UploadNotificationConfig())

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*Log.v("page_id ", String.valueOf(position));
            return PlaceholderFragment.newInstance(position + 1);*/
            switch (position) {
                case 0:
                    //return new TestingHomeFragment();
                    return new BlankFragment();
                case 1:
                    return new BlankFragment();
                    //return new TestingTestKitFragment();
                case 2:
                    return new BlankFragment();
                    //return new TestingLocationFragment();
                case 3:
                    return new BlankFragment();
                    //return new TestingInstructionFragment();
                case 4:
                    return new BlankFragment();
                    //return new TestingCareFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HISTORY";
                case 1:
                    return "TEST KIT";
                case 2:
                    return "LOCATIONS";
                case 3:
                    return "INSTRUCTIONS";
                case 4:
                    return "CARE";
            }
            return null;
        }
    }
}

