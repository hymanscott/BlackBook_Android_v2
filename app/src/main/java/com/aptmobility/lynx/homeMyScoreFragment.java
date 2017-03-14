package com.aptmobility.lynx;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.EncounterSexType;
import com.aptmobility.model.Partners;
import com.aptmobility.model.UserDrugUse;

/**
 * Created by Safiq Ahamed on 6/17/2015.
 */
public class homeMyScoreFragment extends Fragment {
    DatabaseHelper db;
    Point p;
    public homeMyScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private SeekBar seek_barone;
    private TextView seek_textviewtwoLabel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_home_myscore_screen, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "RobotoSlabRegular.ttf");

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);

        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)view.findViewById(R.id.myscore_framelayout);
        dial_layout.setLayoutParams(params);


        final ImageView dial_imgview = (ImageView)view.findViewById(R.id.imageView);
        TextView current_score_tv = (TextView) view.findViewById(R.id.textProgress_id1_toplabel);
        TextView current_score_text = (TextView) view.findViewById(R.id.current_score_text);
        calculateSexProScore getscore = new calculateSexProScore(getActivity());
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        Log.v("AdjScore", String.valueOf(adjustedScore));
        Log.v("unAdjScore", String.valueOf(unAdjustedScore));
        Log.v("Is_prep?", LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score_tv.setText(String.valueOf(adjustedScore));
            float angle = (int) ((adjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP raised your score from " +  String.valueOf(unAdjustedScore) +
                    " & added an extra layer of protection.");
        }else{
            current_score_tv.setText(String.valueOf(unAdjustedScore));
            float angle = (int) ((unAdjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP can raise your score to " +  String.valueOf(adjustedScore) +
                    " & add an extra layer of protection.");
        }

        CustomButtonView myscorePrep = (CustomButtonView) view.findViewById(R.id.myscore_prEP);

        myscorePrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent myscore_prep = new Intent(getActivity(), Myscore_Activity.class);
                startActivity(myscore_prep);
            }
        });

         /*
        * Raise Sex Pro score Layout
        */
        LinearLayout raiseSexProScoreBy = (LinearLayout)view.findViewById(R.id.raiseSexProScoreBy);
        TextView sexualPartner = new TextView(getActivity());
        sexualPartner.setTextColor(getResources().getColor(R.color.text_color));
        sexualPartner.setText(Html.fromHtml("&#8226; Decreasing your number of sexual partners."));
        sexualPartner.setTypeface(roboto);
        sexualPartner.setTextSize(20);
        sexualPartner.setPadding(30, 20, 0, 0);

        TextView condomuseAsTop = new TextView(getActivity());
        condomuseAsTop.setTextColor(getResources().getColor(R.color.text_color));
        condomuseAsTop.setText(Html.fromHtml("&#8226; Increasing your condom use as a TOP."));
        condomuseAsTop.setTextSize(20);
        condomuseAsTop.setTypeface(roboto);
        condomuseAsTop.setPadding(30, 20, 0, 0);

        TextView condomuseAsBottom = new TextView(getActivity());
        condomuseAsBottom.setTextColor(getResources().getColor(R.color.text_color));
        condomuseAsBottom.setText(Html.fromHtml("&#8226; Increasing your condom use as a BOTTOM."));
        condomuseAsBottom.setTextSize(20);
        condomuseAsBottom.setTypeface(roboto);
        condomuseAsBottom.setPadding(30, 20, 0, 0);

        TextView methSpeed = new TextView(getActivity());
        methSpeed.setTextColor(getResources().getColor(R.color.text_color));
        methSpeed.setText(Html.fromHtml("&#8226; Reduce your use of meth/speed."));
        methSpeed.setTextSize(20);
        methSpeed.setTypeface(roboto);
        methSpeed.setPadding(30, 20, 0, 0);

        TextView cocaine = new TextView(getActivity());
        cocaine.setTextColor(getResources().getColor(R.color.text_color));
        cocaine.setText(Html.fromHtml("&#8226; Reduce your use of cocaine/crack."));
        cocaine.setTextSize(20);
        cocaine.setTypeface(roboto);
        cocaine.setPadding(30, 20, 0, 0);

        TextView poppers = new TextView(getActivity());
        poppers.setTextColor(getResources().getColor(R.color.text_color));
        poppers.setText(Html.fromHtml("&#8226; Reduce your use of poppers."));
        poppers.setTextSize(20);
        poppers.setTypeface(roboto);
        poppers.setPadding(30, 20, 0, 0);

        TextView preventsti = new TextView(getActivity());
        preventsti.setTextColor(getResources().getColor(R.color.text_color));
        preventsti.setText(Html.fromHtml("&#8226; Using condoms to  prevent STDs"));
        preventsti.setTextSize(20);
        preventsti.setTypeface(roboto);
        preventsti.setPadding(30, 20, 0,0);

        TextView heavyAlc = new TextView(getActivity());
        heavyAlc.setTextColor(getResources().getColor(R.color.text_color));
        heavyAlc.setText(Html.fromHtml("&#8226; Reducing the amount of alcohol you drink."));
        heavyAlc.setTextSize(20);
        heavyAlc.setTypeface(roboto);
        heavyAlc.setPadding(30,20,0,0);

        /*Your Sex pro score Summary*/
        TextView noOfPeople_bottomed = (TextView) view.findViewById(R.id.myscore_noOfPeopleWhenBottomed);
        TextView noOfPeople_topped = (TextView) view.findViewById(R.id.myscore_noOfPeopleWhenTopped);
        TextView hivNegative = (TextView) view.findViewById(R.id.myscore_hivNeg);
        TextView hadPoppers = (TextView) view.findViewById(R.id.myscore_hadPoppers);
        TextView hadMethSpeed = (TextView) view.findViewById(R.id.myscore_hadMethSpeed);
        TextView hadCocaine = (TextView) view.findViewById(R.id.myscore_hadCocaine);
        TextView drinkingHeavily = (TextView) view.findViewById(R.id.myscore_drinkingHeavily);
        TextView myscore_hadSTD = (TextView) view.findViewById(R.id.myscore_hadSTD);

        int topped =0;
        int bottomed =0;
        String sexType;
        String condomUse;
        db = new DatabaseHelper(getActivity());

        /*// Validating user created date
        Calendar calCreatedAt = Calendar.getInstance();
        Date createdAt = null;
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            createdAt = dateFormat.parse(LynxManager.getActiveUser().getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calCreatedAt.setTime(createdAt);
        Calendar calCurrentDate = Calendar.getInstance();
        long milliSeconds1 = calCreatedAt.getTimeInMillis();
        long milliSeconds2 = calCurrentDate.getTimeInMillis();
        long periodSeconds = (milliSeconds2 - milliSeconds1) ;
        long elapsedDays = periodSeconds / (1000 * 60 * 60 * 24);
        Log.v("elapsedDays", String.valueOf(elapsedDays));
        //System.out.println(String.format("%d months", elapsedDays / 30));*/

        for(EncounterSexType encSexType: db.getAllEncounterSexTypes()){
            sexType = LynxManager.decryptString(encSexType.getSex_type());
            Log.v("sexType",sexType);
            condomUse = encSexType.getCondom_use();
            Log.v("condomUse",condomUse);
            if(sexType.equals("I topped") && condomUse.equals("Condom not used")){
                topped ++;
            }
            if(sexType.equals("I bottomed") && condomUse.equals("Condom not used")){
                bottomed ++;
            }
        }
        topped += Integer.parseInt(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_top_hivposs()));
        bottomed += Integer.parseInt(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_bot_hivposs()));
        Log.v("topped", String.valueOf(topped));
        Log.v("bottomed", String.valueOf(bottomed));

        noOfPeople_bottomed.setText(String.valueOf(bottomed));
        noOfPeople_topped.setText(String.valueOf(topped));
        if(topped>0){
            raiseSexProScoreBy.addView(condomuseAsTop);
        }
        if(bottomed>0){
            raiseSexProScoreBy.addView(condomuseAsBottom);
        }
        /*Number of HIV Negative and HIV Negative & on PrEP people count*/
        int hiv_negativePeople =0;
        String hiv_status;
        for(Partners partner : db.getAllPartners()){
            hiv_status = LynxManager.decryptString(partner.getHiv_status());
            if(hiv_status.equals("HIV Negative")|| hiv_status.equals("HIV Negative & on PrEP")){
                hiv_negativePeople++;
            }
        }
        hiv_negativePeople += Integer.parseInt(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count()));
        Log.v("hiv_negativePeople", String.valueOf(hiv_negativePeople));
        hivNegative.setText(String.valueOf(hiv_negativePeople));
        if(hiv_negativePeople>0){
            raiseSexProScoreBy.addView(sexualPartner);
        }

        int had_drug = 0;
        boolean had_poppers = false;
        boolean had_alcohol = false;
        boolean had_cocaine = false;
        boolean had_methspeed = false;
        for (UserDrugUse druginfo : db.getAllDrugUser()) {
           int drug_id = druginfo.getDrug_id();
            Log.v("MyscoreUsedDruglist", String.valueOf(drug_id));
            switch (String.valueOf(drug_id)){
                case "1":
                    hadPoppers.setText("Yes");
                    had_drug++;
                    /*raiseSexProScoreBy.removeView(poppers);
                    raiseSexProScoreBy.addView(poppers);*/
                    had_poppers = true;
                    break;
                case "2":
                    drinkingHeavily.setText("Yes");
                    /*raiseSexProScoreBy.removeView(heavyAlc);
                    raiseSexProScoreBy.addView(heavyAlc);*/
                    had_alcohol =true;
                    had_drug++;
                    break;
                case "3":
                    hadCocaine.setText("Yes");
                    /*raiseSexProScoreBy.removeView(cocaine);
                    raiseSexProScoreBy.addView(cocaine);*/
                    had_cocaine = true;
                    had_drug++;
                    break;
                case "4":
                    hadMethSpeed.setText("Yes");
                    /*raiseSexProScoreBy.removeView(methSpeed);
                    raiseSexProScoreBy.addView(methSpeed);*/
                    had_methspeed = true;
                    had_drug++;
                    break;
                default:
                    break;
            }
        }

        if(had_poppers)
            raiseSexProScoreBy.addView(poppers);
        if(had_alcohol)
            raiseSexProScoreBy.addView(heavyAlc);
        if (had_cocaine)
            raiseSexProScoreBy.addView(cocaine);
        if (had_methspeed)
            raiseSexProScoreBy.addView(methSpeed);

        if(db.getSTIDiagCount()>0){
            myscore_hadSTD.setText("Yes");
            raiseSexProScoreBy.addView(preventsti);
        }
        if(db.getSTIDiagCount()>0 || hiv_negativePeople>0 ||bottomed >0 || topped >0 || had_drug >0){
            raiseSexProScoreBy.setVisibility(View.VISIBLE);
        }
        final ImageView btn = (ImageView) view.findViewById(R.id.imgBtn_sexpro_info);
        final int[] click = {0};
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                ImageView btn = (ImageView) getActivity().findViewById(R.id.imgBtn_sexpro_info);

                // Get the x, y location and store it in the location[] array
                // location[0] = x, location[1] = y.
                btn.getLocationOnScreen(location);

                //Initialize the Point with x, and y positions
                p = new Point();
                p.x = location[0];
                p.y = location[1];

                //Open popup window

                if (p != null)
                    showPopup(v);
            }
        });

        return view;
    }



    public void showPopup(View anchorView) {


        View popupView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.fragment_sexpro_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);

        popupWindow.setHeight(500);
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        //anchorView.getLocationOnScreen(location);
        ImageView btn = (ImageView) getActivity().findViewById(R.id.imgBtn_sexpro_info);
        btn.getLocationOnScreen(location);
        p = new Point();
        p.x = location[0];
        p.y = location[1];
        int OFFSET_X = 4;
        int OFFSET_Y = 30;
        // Using location, the PopupWindow will be displayed right under anchorView //Gravity.NO_GRAVITY
        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, p.x - (popupView.getWidth() + OFFSET_X), p.y + OFFSET_Y);

    }


}
