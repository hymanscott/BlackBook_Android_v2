package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.UUID;

public class AddNewTest extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper db;
    String title="";
    ImageView hivTestImage,gonorrheaImage,syphilisImage,chlamydiaImage,hivAttachment,gonorrheaAttachment,syphilisAttachment,chlamydiaAttachment;
    int currentAttachmentId=0;
    String hivImageName="",gonorrheaImageName="",syphilisImageName="",chlamydiaImageName="";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_CAPTURE = 2;
    private static final int RESULT_OK = -1;
    private static final int PICK_IMAGE_REQUEST_AFTER_KITKAT = 5;
    private static final int KITKAT_API_VERSION = 19;
    private static final int READ_WRITE_PERMISSION = 100;
    TextView titleText,hivPosQn,stdTesPosQn,gonorrheaTitle,syphilisTitle,chlamydiaTitle;
    EditText addNewTestDate;
    RadioButton hivTestYes,hivTestNo,hivTestDidntTest,gonorrheaYes,gonorrheaNo,gonorrheaDidntTest,syphilisYes,syphilisNo,syphilisDidntTest;
    RadioButton chlamydiaYes,chlamydiaNo,chlamydiaDidntTest;
    Button add_new_test_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.INVISIBLE);
        // Typeface //
        Typeface ty = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        /*Typeface ty = Typeface.createFromAsset(getResources().getAssets(),
                "RobotoSlabRegular.ttf");*/
        titleText = (TextView)findViewById(R.id.titleText);
        titleText.setTypeface(ty);
        hivPosQn = (TextView)findViewById(R.id.hivPosQn);
        hivPosQn.setTypeface(ty);
        stdTesPosQn = (TextView)findViewById(R.id.stdTesPosQn);
        stdTesPosQn.setTypeface(ty);
        gonorrheaTitle = (TextView)findViewById(R.id.gonorrheaTitle);
        gonorrheaTitle.setTypeface(ty);
        syphilisTitle = (TextView)findViewById(R.id.syphilisTitle);
        syphilisTitle.setTypeface(ty);
        chlamydiaTitle = (TextView)findViewById(R.id.chlamydiaTitle);
        chlamydiaTitle.setTypeface(ty);

        addNewTestDate = (EditText)findViewById(R.id.addNewTestDate);
        addNewTestDate.setTypeface(ty);

        hivTestYes = (RadioButton)findViewById(R.id.hivTestYes);
        hivTestYes.setTypeface(ty);
        hivTestNo = (RadioButton)findViewById(R.id.hivTestNo);
        hivTestNo.setTypeface(ty);
        hivTestDidntTest = (RadioButton)findViewById(R.id.hivTestDidntTest);
        hivTestDidntTest.setTypeface(ty);
        gonorrheaYes = (RadioButton)findViewById(R.id.gonorrheaYes);
        gonorrheaYes.setTypeface(ty);
        gonorrheaNo = (RadioButton)findViewById(R.id.gonorrheaNo);
        gonorrheaNo.setTypeface(ty);
        gonorrheaDidntTest = (RadioButton)findViewById(R.id.gonorrheaDidntTest);
        gonorrheaDidntTest.setTypeface(ty);
        syphilisYes = (RadioButton)findViewById(R.id.syphilisYes);
        syphilisYes.setTypeface(ty);
        syphilisNo = (RadioButton)findViewById(R.id.syphilisNo);
        syphilisNo.setTypeface(ty);
        syphilisDidntTest = (RadioButton)findViewById(R.id.syphilisDidntTest);
        syphilisDidntTest.setTypeface(ty);
        chlamydiaYes = (RadioButton)findViewById(R.id.chlamydiaYes);
        chlamydiaYes.setTypeface(ty);
        chlamydiaNo = (RadioButton)findViewById(R.id.chlamydiaNo);
        chlamydiaNo.setTypeface(ty);
        chlamydiaDidntTest = (RadioButton)findViewById(R.id.chlamydiaDidntTest);
        chlamydiaDidntTest.setTypeface(ty);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(AddNewTest.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(AddNewTest.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewTest.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        db= new DatabaseHelper(AddNewTest.this);
        title = getIntent().getStringExtra("testname");
        TextView newTest_title = (TextView) findViewById(R.id.addNewTestTitle);
        newTest_title.setTypeface(ty);
        newTest_title.setText("New " + title);
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

        final EditText newTestDate = (EditText) findViewById(R.id.addNewTestDate);
        newTestDate.addTextChangedListener(new TextWatcher() {
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
                    newTestDate.setText(current);
                    newTestDate.setSelection(sel < current.length() ? sel : current.length());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView calenderIconNewTestDate = (ImageView)findViewById(R.id.calenderIconNewTestDate);
        calenderIconNewTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //   Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        newTestDate.setText(df.format(c.getTime()));

                    }
                };
                datePickerFragment.show(AddNewTest.this.getFragmentManager(), "datePicker");
            }
        });

        TestNameMaster testNameMaster = db.getTestingNamebyName(title);
        final int testing_id = testNameMaster.getTesting_id();

        add_new_test_ok = (Button) findViewById(R.id.addNewTestOk);
        add_new_test_ok.setTypeface(ty);
        add_new_test_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean invalid_date = LynxManager.dateValidation(newTestDate.getText().toString());
                if (newTestDate.getText().toString().isEmpty()) {
                    Toast.makeText(AddNewTest.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(invalid_date){
                    Toast.makeText(AddNewTest.this,"Invalid Date",Toast.LENGTH_SHORT).show();
                }
                else {
                    String date = LynxManager.getFormatedDate("MM/dd/yyyy",newTestDate.getText().toString(),"yyyy-MM-dd");
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
        hivTestImage = (ImageView) findViewById(R.id.hivTestImage);
        hivTestImage.setOnClickListener(AddNewTest.this);
        gonorrheaImage = (ImageView) findViewById(R.id.gonorrheaImage);
        gonorrheaImage.setOnClickListener(AddNewTest.this);
        syphilisImage = (ImageView) findViewById(R.id.syphilisImage);
        syphilisImage.setOnClickListener(AddNewTest.this);
        chlamydiaImage = (ImageView) findViewById(R.id.chlamydiaImage);
        chlamydiaImage.setOnClickListener(AddNewTest.this);

        hivAttachment = (ImageView) findViewById(R.id.hivAttachment);
        hivAttachment.setTag(0);
        gonorrheaAttachment = (ImageView) findViewById(R.id.gonorrheaAttachment);
        gonorrheaAttachment.setTag(0);
        syphilisAttachment = (ImageView) findViewById(R.id.syphilisAttachment);
        syphilisAttachment.setTag(0);
        chlamydiaAttachment = (ImageView) findViewById(R.id.chlamydiaAttachment);
        chlamydiaAttachment.setTag(0);

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
                        attachment.setVisibility(View.GONE);
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
                    attachment.setVisibility(View.GONE);
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
                    Bitmap resized = ThumbnailUtils.extractThumbnail(bitmap,300,300);
                    Drawable d =new BitmapDrawable(resized);


                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = LynxManager.getInstance().getLastImageName();
                    attachment.setTag(1);
                }else{
                    attachment.setTag(0);
                    attachment.setVisibility(View.GONE);
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
                    Drawable d = new BitmapDrawable(thumbnail);
                    Log.v("FileComplatePath",file.getAbsolutePath() + fileName);
                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = file.getAbsolutePath() + fileName;
                    attachment.setTag(1);

                }else{
                    attachment.setTag(0);
                    attachment.setVisibility(View.GONE);
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
        Log.v("ImageName",imagepath);
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

            Log.v("Temp Media", mediaFile.getAbsolutePath());
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
}

