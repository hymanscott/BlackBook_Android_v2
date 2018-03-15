package com.lynxstudy.lynx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.Users;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Collections;
import java.util.List;

import static android.R.attr.fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeEncounterFragment extends Fragment {

    DatabaseHelper db;
    private  int width,height;
    TextView partner,sexRating,hivStatus,typeSex,condomUsed;
    private boolean isSummaryShown = false;
    public HomeEncounterFragment() {
        // Required empty public constructor
    }
    LinearLayout encounterListContent;
    RelativeLayout encounterSummaryContent;
    View view;
    Typeface tf,tf_bold;
    int back_press_count;
    private Tracker tracker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_home_encounter, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Lynxdiary/History").title("Lynxdiary/History").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        //Type face
       tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        //Log.v("priPartId", String.valueOf(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id()));
        encounterListContent = (LinearLayout) view.findViewById(R.id.encounterListContent);
        encounterSummaryContent = (RelativeLayout) view.findViewById(R.id.encounterSummaryContent);
        Button addNewEncounter = (Button) view.findViewById(R.id.addNewEncounter);
        addNewEncounter.setTypeface(tf_bold);
        addNewEncounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TrackHelper.track().event("Navigation","Click").name("Add New Encounter").with(tracker);
                Intent home = new Intent(getActivity(), EncounterStartActivity.class);
                home.putExtra("fromNotification",false);
                startActivityForResult(home, 100);
            }
        });
        boolean isfromNotification = getActivity().getIntent().getBooleanExtra("fromNotification",false);
        if(isfromNotification){
            Intent home = new Intent(getActivity(), EncounterStartActivity.class);
            home.putExtra("fromNotification",true);
            startActivityForResult(home, 101);
            getActivity().getIntent().removeExtra("fromNotification");
        }
        final TableLayout encounterTable = (TableLayout) view.findViewById(R.id.encounterTable);
        encounterTable.removeAllViews();

        db = new DatabaseHelper(getActivity());
        LynxManager.selectedPartnerID = 0;

        Gson gson = new Gson();
        Users DecryptUser = LynxManager.getActiveUser();
        DecryptUser.setStatus_encrypt(true);
        DecryptUser.decryptUser();
        String json = gson.toJson(DecryptUser);
        //Log.v("Encounter List", json);
        String hashcode = LynxManager.stringToHashcode(json);
        //Log.v("Encounter hashcode List",hashcode);

        List<Encounter> allEncounters = db.getAllEncounters();
        int j = 0;

        if(allEncounters.isEmpty()){
            TableRow encounterRow = new TableRow(getActivity());
            encounterRow.setPadding(0, 0, 10, 0);
            encounterRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            TextView Info = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
            Info.setText("No Encounters on list");
            Info.setTypeface(tf);
            Info.setTextColor(getResources().getColor(R.color.text_color));
            encounterRow.addView(Info);
            encounterTable.addView(encounterRow);
        }
        else {
            Collections.sort(allEncounters, new Encounter.CompDate(true));
            for (Encounter encounter : allEncounters) {
                int enc_partner_id = encounter.getEncounter_partner_id();
                Partners partner = db.getPartnerbyID(enc_partner_id);

                TableRow encounterRow = new TableRow(getActivity());
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.table_encounter_row, encounterRow, false);
                if (width == 480 && height == 800)
                {
                    v = LayoutInflater.from(getActivity()).inflate(R.layout.table_encounter_row_alt, encounterRow, false);
                }
                TextView date = (TextView)v.findViewById(R.id.date);
                TextView name = (TextView)v.findViewById(R.id.name);
                RatingBar ratingBar = (RatingBar)v.findViewById(R.id.rating);
                date.setTypeface(tf);
                name.setTypeface(tf);
                String format = "MM/dd/yy";
                String current_format = "yyyy-MM-dd HH:mm:ss";
                date.setText(LynxManager.getFormatedDate(current_format, LynxManager.decryptString(encounter.getDatetime()), format));
                name.setText(LynxManager.decryptString(partner.getNickname()));
                ratingBar.setRating(Float.parseFloat(LynxManager.decryptString(encounter.getRate_the_sex())));
                ratingBar.setNumStars(5);
                ratingBar.setRight(10);
                float rating_bar_scale = (float) 0.9;
                ratingBar.setScaleX(rating_bar_scale);
                ratingBar.setScaleY(rating_bar_scale);
                LayerDrawable stars4 = (LayerDrawable) ratingBar.getProgressDrawable();
                stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
                stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);// Off State color
                stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)
                int encounterId = encounter.getEncounter_id();
                v.setClickable(true);
                v.setFocusable(true);
                v.setId(encounterId);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Highlight selected row
                        for (int i = 0; i < encounterTable.getChildCount(); i++) {
                            View row = encounterTable.getChildAt(i);
                            if (row == view) {
                                //row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                LynxManager.selectedEncounterID = row.getId();
                                LynxManager.activePartnerSexType.clear();

                                //Set Active Encounter
                                Encounter selectedEncounter = db.getEncounter(row.getId());
                                LynxManager.setActiveEncounter(selectedEncounter);
                                //Log.v("sextypeID", String.valueOf(selectedEncounter.getEncounter_id()));
                                //set Active partner and Contact
                                LynxManager.setActivePartner(db.getPartnerbyID(selectedEncounter.getEncounter_partner_id()));
                                LynxManager.setActivePartnerContact(db.getPartnerContactbyPartnerID(selectedEncounter.getEncounter_partner_id()));

                                List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(row.getId());

                                for (EncounterSexType setSextype : selectedSEXtypes) {
                                    EncounterSexType encounterSexType = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.decryptString(setSextype.getSex_type()), "", "", String.valueOf(R.string.statusUpdateNo),false);
                                    //Log.v("sextypes", String.valueOf(encounterSexType));
                                    LynxManager.activePartnerSexType.add(encounterSexType);
                                }
                                //Intent selectedEncounterSumm = new Intent(getActivity(), SelectedEncounterSummary.class);
                                //Log.v("EncounterID", String.valueOf(row.getId()));
                                /*Intent selectedEncounterSumm = new Intent(getActivity(), SelectedEncounterSummary.class);
                                selectedEncounterSumm.putExtra("EncounterID",row.getId());
                                startActivity(selectedEncounterSumm);*/
                                setEncounterSummary(row.getId());
                            } else {
                                //Change this to your normal background color.
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                            }
                        }
                    }
                });
                v.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                encounterTable.addView(v);
            }
        }
        back_press_count=0;
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isSummaryShown){
                        encounterSummaryContent.setVisibility(View.GONE);
                        encounterListContent.setVisibility(View.VISIBLE);
                        isSummaryShown = false;
                        back_press_count = 0;
                    }else{
                        if(back_press_count>1){
                            /*Intent home = new Intent(getActivity(),LynxSexPro.class);
                            startActivity(home);*/
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
        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        //   reloadFragment();
    }

    public void reloadFragment() {
        //Log.v("Fragment Reload", "Reloaded");

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 || requestCode == 101) {
            reloadFragment();
            LynxManager.isRefreshRequired = true;
            startActivity(getActivity().getIntent());
            getActivity().finish();
        }
    }

    public void pushFragments(String tag, Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

        ft.replace(R.id.container, fragment);
        if (addToStack == true)
            ft.addToBackStack(null);
        ft.commit();


    }

    private void setEncounterSummary(int encounter_id){
        encounterSummaryContent.setVisibility(View.VISIBLE);
        encounterListContent.setVisibility(View.GONE);
        isSummaryShown = true;
        partner = (TextView)view.findViewById(R.id.partner);
        partner.setTypeface(tf);
        sexRating = (TextView)view.findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        hivStatus = (TextView)view.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        typeSex = (TextView)view.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        condomUsed = (TextView)view.findViewById(R.id.condomUsed);
        condomUsed.setTypeface(tf);
        db = new DatabaseHelper(getActivity());
        TextView nickname = (TextView) view.findViewById(R.id.encList_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);
        TextView partnerNotes = (TextView) view.findViewById(R.id.encListSumm_partnerNotes);
        partnerNotes.setText(LynxManager.decryptString(db.getEncounter(encounter_id).getEncounter_notes()));
        partnerNotes.setTypeface(tf);

        RatingBar sexRating = (RatingBar) view.findViewById(R.id.encListSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.decryptString(String.valueOf(LynxManager.getActiveEncounter().getRate_the_sex()))));


        LayerDrawable stars1 = (LayerDrawable) sexRating.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars


        TextView hivStatus = (TextView) view.findViewById(R.id.encListSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)view.findViewById(R.id.sexTypeLayout);
        String gender = LynxManager.decryptString(LynxManager.getActivePartner().getGender());
        //Log.v("Gender",gender);
        View sextypeView;
        switch (gender){
            case "Woman":
                sextypeView  = getActivity().getLayoutInflater().inflate(R.layout.encounter_sextype_woman,null);
                break;
            case "Trans woman":
                sextypeView  = getActivity().getLayoutInflater().inflate(R.layout.encounter_sextype_transwoman, null);
                break;
            case "Trans man":
                sextypeView  = getActivity().getLayoutInflater().inflate(R.layout.encounter_sextype_transman, null);
                break;
            default:
                sextypeView  = getActivity().getLayoutInflater().inflate(R.layout.encounter_sextype_man, null);
                break;
        }
        sexTypeLayout.removeAllViews();
        sexTypeLayout.addView(sextypeView);
        //int encounter_id = getActivity().getIntent().getIntExtra("EncounterID",0);
        final ToggleButton btn_sexType_kissing = (ToggleButton) view.findViewById(R.id.sexType_kissing);
        btn_sexType_kissing.setTypeface(tf);
        final ToggleButton btn_sexType_iSucked = (ToggleButton) view.findViewById(R.id.sexType_iSucked);
        btn_sexType_iSucked.setTypeface(tf);
        final ToggleButton btn_sexType_heSucked = (ToggleButton) view.findViewById(R.id.sexType_heSucked);
        btn_sexType_heSucked.setTypeface(tf);
        final ToggleButton btn_sexType_iTopped = (ToggleButton) view.findViewById(R.id.sexType_iTopped);
        btn_sexType_iTopped.setTypeface(tf);
        final ToggleButton btn_sexType_iBottomed = (ToggleButton) view.findViewById(R.id.sexType_iBottomed);
        btn_sexType_iBottomed .setTypeface(tf);
        final ToggleButton btn_sexType_iJerked = (ToggleButton) view.findViewById(R.id.sexType_iJerked);
        btn_sexType_iJerked.setTypeface(tf);
        final ToggleButton btn_sexType_heJerked = (ToggleButton) view.findViewById(R.id.sexType_heJerked);
        btn_sexType_heJerked.setTypeface(tf);
        final ToggleButton btn_sexType_iRimmed = (ToggleButton) view.findViewById(R.id.sexType_iRimmed);
        btn_sexType_iRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_heRimmed = (ToggleButton) view.findViewById(R.id.sexType_heRimmed);
        btn_sexType_heRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_iWentDown = (ToggleButton) view.findViewById(R.id.sexType_iWentDown);
        btn_sexType_iWentDown.setTypeface(tf);
        final ToggleButton btn_sexType_iFucked = (ToggleButton) view.findViewById(R.id.sexType_iFucked);
        btn_sexType_iFucked.setTypeface(tf);
        final ToggleButton btn_sexType_iFingered = (ToggleButton) view.findViewById(R.id.sexType_iFingered);
        btn_sexType_iFingered.setTypeface(tf);
        final ToggleButton btn_sexType_heFingered = (ToggleButton) view.findViewById(R.id.sexType_heFingered);
        btn_sexType_heFingered.setTypeface(tf);

        LynxManager.activeEncCondomUsed.clear();
        if(encounter_id!=0){
            List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(encounter_id);

            for (EncounterSexType encSexType : selectedSEXtypes) {
                switch (LynxManager.decryptString(encSexType.getSex_type())) {
                    case "We kissed/made out":
                        ToggleButton sexType_kissing = (ToggleButton) view.findViewById(R.id.sexType_kissing);
                        sexType_kissing.setSelected(true);
                        sexType_kissing.setClickable(false);
                        sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                        sexType_kissing.setTypeface(tf);
                        break;
                    case "I sucked him":
                    case "I sucked her":
                        ToggleButton sexType_iSucked = (ToggleButton)view.findViewById(R.id.sexType_iSucked);
                        sexType_iSucked.setSelected(true);
                        sexType_iSucked.setClickable(false);
                        sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iSucked.setTypeface(tf);
                        //Log.v("CondomStatus","iSucked "+LynxManager.decryptString(encSexType.getCondom_use()));
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used")&& !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        break;
                    case "He sucked me":
                    case "She sucked me":
                        ToggleButton sexType_heSucked = (ToggleButton)view.findViewById(R.id.sexType_heSucked);
                        sexType_heSucked.setSelected(true);
                        sexType_heSucked.setClickable(false);
                        sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heSucked.setTypeface(tf);
                        break;
                    case "I bottomed":
                        ToggleButton sexType_iBottomed = (ToggleButton)view.findViewById(R.id.sexType_iBottomed);
                        sexType_iBottomed.setSelected(true);
                        sexType_iBottomed.setClickable(false);
                        sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iBottomed.setTypeface(tf);
                        //Log.v("CondomStatus","iBot "+LynxManager.decryptString(encSexType.getCondom_use()));
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        break;
                    case "I topped":
                        ToggleButton sexType_iTopped = (ToggleButton)view.findViewById(R.id.sexType_iTopped);
                        sexType_iTopped.setSelected(true);
                        sexType_iTopped.setClickable(false);
                        sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iTopped.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        //Log.v("CondomStatus","iTop "+LynxManager.decryptString(encSexType.getCondom_use()));
                        break;
                    case "I jerked him":
                    case "I jerked her":
                        ToggleButton sexType_iJerked = (ToggleButton)view.findViewById(R.id.sexType_iJerked);
                        sexType_iJerked.setSelected(true);
                        sexType_iJerked.setClickable(false);
                        sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iJerked.setTypeface(tf);
                        break;
                    case "He jerked me":
                    case "She jerked me":
                        ToggleButton sexType_heJerked = (ToggleButton)view.findViewById(R.id.sexType_heJerked);
                        sexType_heJerked.setSelected(true);
                        sexType_heJerked.setClickable(false);
                        sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heJerked.setTypeface(tf);
                        break;
                    case "I rimmed him":
                    case "I rimmed her":
                        ToggleButton sexType_iRimmed = (ToggleButton)view.findViewById(R.id.sexType_iRimmed);
                        sexType_iRimmed.setSelected(true);
                        sexType_iRimmed.setClickable(false);
                        sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iRimmed.setTypeface(tf);
                        break;
                    case "He rimmed me":
                    case "She rimmed me":
                        ToggleButton sexType_heRimmed = (ToggleButton)view.findViewById(R.id.sexType_heRimmed);
                        sexType_heRimmed.setSelected(true);
                        sexType_heRimmed.setClickable(false);
                        sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heRimmed.setTypeface(tf);
                        break;
                    case "I fucked her":
                    case "We fucked":
                        ToggleButton sexType_iFucked = (ToggleButton)view.findViewById(R.id.sexType_iFucked);
                        sexType_iFucked.setSelected(true);
                        sexType_iFucked.setClickable(false);
                        sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iFucked.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        //Log.v("CondomStatus","iFucked "+LynxManager.decryptString(encSexType.getCondom_use()));
                        break;
                    case "I fingered her":
                    case "I fingered him":
                        ToggleButton sexType_iFingered = (ToggleButton)view.findViewById(R.id.sexType_iFingered);
                        sexType_iFingered.setSelected(true);
                        sexType_iFingered.setClickable(false);
                        sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iFingered.setTypeface(tf);
                        break;
                    case "He fingered me":
                        ToggleButton sexType_heFingered = (ToggleButton)view.findViewById(R.id.sexType_heFingered);
                        sexType_heFingered.setSelected(true);
                        sexType_heFingered.setClickable(false);
                        sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heFingered.setTypeface(tf);
                        break;
                    case "I went down on her":
                    case "I went down on him":
                        ToggleButton sexType_iWentDown = (ToggleButton)view.findViewById(R.id.sexType_iWentDown);
                        sexType_iWentDown.setSelected(true);
                        sexType_iWentDown.setClickable(false);
                        sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iWentDown.setTypeface(tf);
                        break;

                }
            }
        }
        LinearLayout condomUsedContent = (LinearLayout)view.findViewById(R.id.condomUsedContent);
        condomUsedContent.removeAllViews();
        //Log.v("activeCondomUsedSize", String.valueOf(LynxManager.activeEncCondomUsed.size()));
        if(LynxManager.activeEncCondomUsed.size()>0){
            LinearLayout condomUsedLayout = (LinearLayout)view.findViewById(R.id.condomUsedLayout);
            condomUsedLayout.setVisibility(View.VISIBLE);
            for (String str : LynxManager.activeEncCondomUsed){
                TextView tv = new TextView(getActivity());
                tv.setTypeface(tf);
                tv.setText("When "+str);
                tv.setPadding(0,0,0,16);
                tv.setTextColor(getResources().getColor(R.color.text_color));
                condomUsedContent.addView(tv);
                //Log.v("CondomUsedWhen",str);
            }
        }else{
            LinearLayout condomUsedLayout = (LinearLayout)view.findViewById(R.id.condomUsedLayout);
            condomUsedLayout.setVisibility(View.GONE);
        }
    }
}
