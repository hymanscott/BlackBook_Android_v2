package com.lynxstudy.lynx;

import android.content.Context;
import android.util.Log;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Hari on 2017-04-13.
 */

public class calculateSexProScore {
    DatabaseHelper db;

    private int BMO; // reported month of birth
    private int BY; //reported year of birth
    private int CMO; //current month (i.e., on day of Sex Pro use) obtained from operating system
    private int CY; //current year, obtained from operating system
    private int BLACK; //1 if Black is one of the race/ethnicity options selected, 0 otherwise
    private int LATINO; //1 if Latino is one of the race/ethnicity options selected, 0 otherwise (note that both Black and Latino can be set to 1).
    private int NASP_NEG;
    private int NASP_POS;
    private int NASP_UNKNOWN; //numbers of HIV-negative, HIV-positive, and unknown serostatus male or transfemale anal sex partners in the last 3 months
    private int PP; //1 if a single HIV-negative or HIV-positive partner is a primary partner, 0 or missing otherwise
    private int PPOP; //1 if primary partner does not have other partners, 0 or missing otherwise
    private int MM6M; //1 if in mutually monogamous relationship with primary partner for at least 6 months, 0 or missing otherwise
    private int PPUVL; //1 if HIV-positive primary partner has had a undetectable viral load, 0 or missing otherwise
    private int PPUVL6M; //1 if HIV-positive partner viral load has been undetectable for at least 6 months, 0 or missing otherwise
    private int NIAS_POS_UNK; //total number of insertive anal sex episodes with HIV-positive or unknown serostatus partners
    private double PPIAS_POS_UNK; //proportion of insertive anal sex episodes with HIV-positive or unknown serostatus partners  in which a condom was used throughout and did not break, based on slider (should have range 0 to 1 for further calculations)
    private int NRAS_POS_UNK; //total number of receptive anal sex episodes with HIV-positive or unknown serostatus partners
    private double PPRAS_POS_UNK; //proportion of receptive anal sex episodes with HIV-positive or unknown serostatus partners in which a condom was used throughout and did not break, based on slider (should have range 0 to 1 for further calculations)
    private int POP; //self-report of popper use; //1=yes, 0=no
    private int COKE; //self-report of cocaine use; //1=yes, 0=no
    private int METH; //self-report of meth/speed use; //1=yes, 0=no
    private int DFREQ; //how often did you drink alcohol in last 3 months
    private int DPD; //on typical days you drank, how many drinks did you have
    private int STI; //self-report of syphilis, gonorrhea, or Chlamydia in last 3 months; //1=yes, 0=no

    private int AGE35;


    private double B1;
    private double B2;
    private double B3;
    private double B4;
    private double B5;
    private double B6;
    private double B7;
    private double B8;
    private double B9;
    private double B10;
    private double B11;
    private double B12;
    private double B13;
    private double B14;

    private double sexProScore;
    private int AGE;
    private int MM6_POS;
    private int MM6_NEG;
    private double NURAs_POS;
    private double NPRAS_POS;
    private double NUIAS_POS;
    private double NPIAS_POS;
    private double lsp1_NURAS_POS_UNK;
    private double lsp2_NURAS_POS_UNK;
    private double adjNPRAS_POS_UNK;
    private double adjNASP_NEG;
    private int DRUG;
    private int HEAVYALC;

    private double ETA;
    private double adjETA;
    private double P6m;
    private double P2y;
    private double Pt;

    private int elapsed_days;

    public calculateSexProScore(Context context) {
        db = new DatabaseHelper(context);
        initialize();

    }

    private void initialize() {

        B1  = -5.799; //Constant value included for everyone
        Users cur_user                      =   LynxManager.getActiveUser();
        User_baseline_info baselineInfo     =   LynxManager.getActiveUserBaselineInfo();
        UserPrimaryPartner primaryPartner   =   LynxManager.getActiveUserPrimaryPartner();
        List<UserDrugUse> baselineDrugUse  =   LynxManager.getActiveUserDrugUse();
        List<UserSTIDiag>  baselineSTI      =   LynxManager.getActiveUserSTIDiag();
        UserAlcoholUse userAlcoholUse   =   LynxManager.getActiveUserAlcoholUse();
        SimpleDateFormat inputDF  = new SimpleDateFormat("dd-MMM-yyyy");

        Date date1 = null;
        try {
            date1 = inputDF.parse(LynxManager.decryptString(cur_user.getDob()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        if(date1!=null){
            cal.setTime(date1);
        }

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        BMO = month;
        BY  =   year;
        SimpleDateFormat inputDF1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date1 = inputDF1.parse(baselineInfo.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(date1);

        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        CMO         =       month;
        CY          =       year;

        if(LynxManager.decryptString(cur_user.getRace()).contains("Black")){
            BLACK = 1;
        } else{BLACK = 0;}
        if(LynxManager.decryptString(cur_user.getRace()).contains("Latino")){
            LATINO = 1;
        } else{LATINO = 0;}

        if(LynxManager.decryptString(primaryPartner.getHiv_status())!=null){

            switch (LynxManager.decryptString(primaryPartner.getHiv_status())){
                case "HIV Negative":
                case "HIV Positive":
                    PP      =   1;
                    break;
                default:
                    PP      =   0;
            }
        }
        else {
            PP  =   0;
        }
        Date  enddate  =   new Date();
        Date startdate = null;
        if(LynxManager.decryptString(primaryPartner.getHiv_status())!=null) {
            PPOP = (LynxManager.decryptString(primaryPartner.getPartner_have_other_partners())).equals("Yes") ? 0 : 1;

            try {
                startdate = inputDF1.parse(primaryPartner.getCreated_at());
                //System.out.println(startdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            MM6M    =  getMonthsDifference(startdate,enddate)>6?1:0;
        }else{
            PPOP = 0;
            MM6M = 0;
        }

        String hiv_status = LynxManager.decryptString(primaryPartner.getHiv_status());

        if(LynxManager.decryptString(primaryPartner.getHiv_status())!=null) {
            if (hiv_status.equals("HIV positive & Undetectable")) {
                PPUVL = 1;
            } else {
                PPUVL = 0;
            }

            String undetectable_period = LynxManager.decryptString(primaryPartner.getUndetectable_for_sixmonth());
            if (!undetectable_period.isEmpty() || undetectable_period.equals("Yes")) {
                PPUVL6M = 1;
            } else {
                PPUVL6M = 0;
            }
        }

        POP             =   0;
        COKE            =   0;
        METH            =   0;
        STI             =   0;
        // Validating user created date
        Calendar calCurrentDate = Calendar.getInstance();
        long milliSeconds1 = cal.getTimeInMillis();
        long milliSeconds2 = calCurrentDate.getTimeInMillis();
        long periodSeconds = (milliSeconds2 - milliSeconds1) ;
        long elapsedDays = periodSeconds / (1000 * 60 * 60 * 24);
        elapsed_days = (int) elapsedDays;
        int hiv_negativePeople =0;
        int hiv_positivePeople =0;
        int hiv_unknownPeople =0;

        if(elapsedDays >=90){
            for(Partners partner : db.getAllPartners()){
                String partner_hiv_status = LynxManager.decryptString(partner.getHiv_status());
                if(partner_hiv_status.equals("HIV Negative")|| partner_hiv_status.equals("HIV Negative & on PrEP")){
                    hiv_negativePeople += 1 ;
                }
                if(partner_hiv_status.equals("HIV Positive")|| partner_hiv_status.equals("HIV Positive & Undetectable")){
                    hiv_positivePeople += 1 ;
                }
                if(partner_hiv_status.equals("I don't know")){
                    hiv_unknownPeople += 1 ;
                }
            }
            NASP_NEG = hiv_negativePeople;
            NASP_POS = hiv_positivePeople;
            NASP_UNKNOWN = hiv_unknownPeople;

            for(UserDrugUse drugUse: baselineDrugUse){
                int id = drugUse.getDrug_id();
                if(LynxManager.decryptString(drugUse.getIs_baseline()).equals("No")){
                    if(db.getDrugNamebyID(id).equals("Poppers")){
                        POP = 1;
                    }
                    else if(db.getDrugNamebyID(id).equals("Cocaine")){
                        COKE    =   1;
                    }
                    else if(db.getDrugNamebyID(id).equals("Meth / Speed")){
                        METH    =   1;
                    }
                }
            }

            for(UserSTIDiag stiDiag: baselineSTI){
                stiDiag.getCreated_at();
                if(LynxManager.decryptString(stiDiag.getIs_baseline()).equals("No")){
                    int id = stiDiag.getSti_id();

                    try {
                        startdate = inputDF1.parse(stiDiag.getCreated_at());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(getMonthsDifference(startdate,enddate)<=3){
                        STI  = 1;
                        break;
                    }
                }
            }

            for(UserAlcoholUse alcoholUse : db.getAllAlcoholUse()){
                if(LynxManager.decryptString(alcoholUse.getIs_baseline()).equals("No")){
                    LynxManager.setActiveUserAlcoholUse(alcoholUse);
                }
            }
            String no_of_DaysinWeek = LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week());
            String no_of_drinks = LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_day());
            if(no_of_DaysinWeek !=null){

                switch (no_of_DaysinWeek){
                    case "Never":
                        DFREQ   =   0;
                        break;
                    case "5-7 days a week":
                        DFREQ   =   6;
                        break;
                    case "1-4 days a week":
                        DFREQ   =   3;
                        break;
                    case "Less than once a week":
                        DFREQ   =   1;
                        break;
                }
            }
            else{ DFREQ   =   0;}

            if (no_of_drinks==null){
                DPD     =   0;
            }
            else {
                DPD = Integer.parseInt(LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_day()));

            }
            //if elapsed day ends
        }
        // Validating 90 days ends
        else{
            NASP_NEG        = Integer.parseInt(LynxManager.decryptString(baselineInfo.getHiv_negative_count()));
            NASP_POS        = Integer.parseInt(LynxManager.decryptString(baselineInfo.getHiv_positive_count()));
            NASP_UNKNOWN    = Integer.parseInt(LynxManager.decryptString(baselineInfo.getHiv_unknown_count()));
            for(UserDrugUse drugUse: baselineDrugUse){
                int id = drugUse.getDrug_id();
                if(LynxManager.decryptString(drugUse.getIs_baseline()).equals("Yes")){
                    if(db.getDrugNamebyID(id).equals("Poppers")){
                        POP = 1;
                    }
                    else if(db.getDrugNamebyID(id).equals("Cocaine")){
                        COKE    =   1;
                    }
                    else if(db.getDrugNamebyID(id).equals("Meth / Speed")){
                        METH    =   1;
                    }
                }
            }

            for(UserSTIDiag stiDiag: baselineSTI){
                stiDiag.getCreated_at();
                if(LynxManager.decryptString(stiDiag.getIs_baseline()).equals("Yes")){
                    int id = stiDiag.getSti_id();

                    try {
                        startdate = inputDF1.parse(stiDiag.getCreated_at());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(getMonthsDifference(startdate,enddate)<=3){
                        STI  = 1;
                        break;
                    }
                }
            }


            if(userAlcoholUse!=null) {

                String no_of_DaysinWeek = LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_week());
                String no_of_drinks = LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_day());
                if(no_of_DaysinWeek !=null){
                    switch (no_of_DaysinWeek){
                        case "Never":
                            DFREQ   =   0;
                            break;
                        case "5-7 days a week":
                            DFREQ   =   6;
                            break;
                        case "1-4 days a week":
                            DFREQ   =   3;
                            break;
                        case "Less than once a week":
                            DFREQ   =   1;
                            break;
                    }
                }
                else{ DFREQ   =   0;}

                if (no_of_drinks==null){
                    DPD     =   0;
                }
                else {
                    DPD = Integer.parseInt(LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_day()));
                }
            }
            else{
                DFREQ   =   0;
                DPD     =   0;
            }

        }


        String topCondomUse =   LynxManager.decryptString(baselineInfo.getTop_condom_use_percent());
        topCondomUse        =   topCondomUse.replaceAll("\\s+","");
        /*topCondomUse        =   topCondomUse.length()==3?topCondomUse.substring(0,2):(topCondomUse.substring(0,1));*/
        topCondomUse        =   topCondomUse.substring(0, topCondomUse.length() - 1);

        String botCondomUse =   LynxManager.decryptString(baselineInfo.getBottom_condom_use_percent());
        botCondomUse        =   botCondomUse.replaceAll("\\s+","");
        /*botCondomUse        =   botCondomUse.length()==3?botCondomUse.substring(0,2):(botCondomUse.substring(0,1));*/
        botCondomUse        =   botCondomUse.substring(0, topCondomUse.length() - 1);

        NIAS_POS_UNK    =   Integer.parseInt(LynxManager.decryptString(baselineInfo.getNo_of_times_top_hivposs()));
        PPIAS_POS_UNK   =   Integer.parseInt(topCondomUse) * 0.01;
        NRAS_POS_UNK    =   Integer.parseInt(LynxManager.decryptString(baselineInfo.getNo_of_times_bot_hivposs()));
        PPRAS_POS_UNK   =   Integer.parseInt(botCondomUse) * 0.01;

        if(BMO<=CMO){
            AGE     = CY - BY;
        }
        else if(BMO>CMO){
            AGE     =   CY - BY -1;
        }

        AGE35   =   AGE<=35?1:0; //AGE35 never used

        if(NASP_POS == 1 && NASP_NEG==0 && NASP_UNKNOWN==0 && PP==1 && PPOP==1 && MM6M==1 && PPUVL ==1 &&PPUVL6M==1){
            MM6_POS     =   1;
        } // STI == 0 removed

        if(NASP_NEG == 1 && NASP_POS==0 && NASP_UNKNOWN==0 && PP==1 && PPOP==1 && MM6M==1){
            MM6_NEG     =   1;
        }// NASP_NEG==0 changed to NASP_POS==0 , STI == 0 removed

        //if(MM6_POS ==1 || MM6_NEG == 1){
        if(MM6_POS ==1 || ( NASP_POS == 0 && NASP_UNKNOWN == 0 )){
            NURAs_POS   =   0;
            NPRAS_POS   =   0;
            NUIAS_POS   =   0;
        }
        else {
            NPIAS_POS   =   NIAS_POS_UNK * PPIAS_POS_UNK;
            NUIAS_POS   =   NIAS_POS_UNK - NPIAS_POS;
            NPRAS_POS   =   NRAS_POS_UNK * PPRAS_POS_UNK;
            NURAs_POS   =   NRAS_POS_UNK - NPRAS_POS;
        }

        lsp1_NURAS_POS_UNK  =   Math.min(10, NURAs_POS);
        lsp2_NURAS_POS_UNK  =   Math.max(0, NURAs_POS - 10);
        adjNPRAS_POS_UNK    =   Math.min(10, NPRAS_POS);
        adjNASP_NEG         =   Math.min(5, NASP_NEG);
        DRUG                =   Math.max(COKE,METH);
        if((DFREQ>=5 && DFREQ<=7 && DPD>=4) || (DFREQ>=1 && DFREQ<=4 && DPD>=6)){
            HEAVYALC = 1;
        }
        else {
            HEAVYALC    =   0;
        }

        if(NRAS_POS_UNK==0 && NIAS_POS_UNK==0){
            STI     =   0;
            DRUG    =   0;
            POP     =   0;
            HEAVYALC=   0;
        }

        if(NASP_POS == 0 && NASP_UNKNOWN == 0 && NASP_NEG == 0 && DRUG == 0 && POP == 0 && HEAVYALC ==0 && STI == 0){
            sexProScore     =   20;
        }
        else {
            /*B2 = AGE35>35?0:0.2703;*/
            B2  =   AGE>35?0:0.2703;
            B3  =   BLACK==0?0:0.8749;
            //B4  =   LATINO==0?0:0.4284;
            B4  =   LATINO==1 && BLACK==1?0.4284:0;
            B5  =   0.2109  *   lsp1_NURAS_POS_UNK;
            B6  =   0.0024  *   lsp2_NURAS_POS_UNK;
            B7  =   0.0322  *   adjNPRAS_POS_UNK;
            B8  =   0.0053  *   NUIAS_POS;
            B9  =   0.0985  *   adjNASP_NEG;
            B10 =   MM6_NEG==0?0:(-1.0955);
            B11 =   HEAVYALC==0?0:0.6450;
            B12 =   DRUG==0?0:0.7663;
            B13 =   POP==0?0:0.1575;
            B14 =   STI==0?0:0.3570;

            ETA     =   B1 + B2 + B3 + B4 + B5 + B6 + B7 + B8 + B9 + B10 + B11 + B12 + B13 + B14;
            if(LynxManager.decryptString(cur_user.getIs_prep()).equals("Yes")){
                adjETA  = ETA   *   0.1;
            }
            else {
                adjETA = ETA;
            }

            P6m     =   1 / (1 + Math.exp(-adjETA));
            P2y     =   (1 - Math.pow((1-P6m),4));
            //Pt      =   Math.max(0.5,Math.min(10,(100 * P2y)));
            Pt      =   Math.max(0.005,Math.min(0.1,P2y));
            //sexProScore     =   1 + 2*(10-Pt);
            if(LynxManager.decryptString(cur_user.getIs_prep()).equals("Yes")){
                sexProScore = 16+4*(1-P2y);
            }else{
                sexProScore =   1 + 2*(10-(100*Pt));
            }
        }

    }

    public int getElapsedDays(){
        return elapsed_days;
    }
    public double getUnAdjustedScore(){

        return 1 + 2*(10-(100*Pt)); // SexproScore without PrEP
    }
    public double getAdjustedScore(){

        /*Log.v("O/P", "Output");
        Log.v("BMO", String.valueOf(BMO));
        Log.v("BY", String.valueOf(BY));
        Log.v("CMO", String.valueOf(CMO));
        Log.v("CY", String.valueOf(CY));
        Log.v("BLACK", String.valueOf(BLACK));
        Log.v("LATINO", String.valueOf(LATINO));

        Log.v("NASP_NEG", String.valueOf(NASP_NEG));
        Log.v("NASP_POS", String.valueOf(NASP_POS));
        Log.v("NASP_UNKNOWN", String.valueOf(NASP_UNKNOWN));

        Log.v("NIAS_POS_UNK", String.valueOf(NIAS_POS_UNK));
        Log.v("NRAS_POS_UNK", String.valueOf(NRAS_POS_UNK));

        Log.v("PP", String.valueOf(PP));
        Log.v("PPOP", String.valueOf(PPOP));
        Log.v("MM6M", String.valueOf(MM6M));
        Log.v("PPUVL", String.valueOf(PPUVL));
        Log.v("PPUVL6M", String.valueOf(PPUVL6M));
        Log.v("PPIAS_POS_UNK", String.valueOf(PPIAS_POS_UNK));
        Log.v("PPRAS_POS_UNK", String.valueOf(PPRAS_POS_UNK));
        Log.v("POP", String.valueOf(POP));
        Log.v("COKE", String.valueOf(COKE));
        Log.v("METH", String.valueOf(METH));
        Log.v("DFREQ", String.valueOf(DFREQ));
        Log.v("DPD", String.valueOf(DPD));
        Log.v("STI", String.valueOf(STI));

        Log.v("AGE35", String.valueOf(AGE35));
        Log.v("B1", String.valueOf(B1));
        Log.v("B2", String.valueOf(B2));
        Log.v("B3", String.valueOf(B3));
        Log.v("B4", String.valueOf(B4));
        Log.v("B5", String.valueOf(B5));
        Log.v("B6", String.valueOf(B6));
        Log.v("B7", String.valueOf(B7));
        Log.v("B8", String.valueOf(B8));
        Log.v("B9", String.valueOf(B9));
        Log.v("B10", String.valueOf(B10));
        Log.v("B11", String.valueOf(B11));
        Log.v("B12", String.valueOf(B12));
        Log.v("B13", String.valueOf(B13));
        Log.v("B14", String.valueOf(B14));

        Log.v("AGE", String.valueOf(AGE));
        Log.v("MM6_POS", String.valueOf(MM6_POS));
        Log.v("MM6_NEG", String.valueOf(MM6_NEG));
        Log.v("NURAs_POS", String.valueOf(NURAs_POS));
        Log.v("NPRAS_POS", String.valueOf(NPRAS_POS));
        Log.v("NUIAS_POS", String.valueOf(NUIAS_POS));
        Log.v("NPIAS_POS", String.valueOf(NPIAS_POS));
        Log.v("lsp1_NURAS_POS_UNK", String.valueOf(lsp1_NURAS_POS_UNK));
        Log.v("lsp2_NURAS_POS_UNK", String.valueOf(lsp2_NURAS_POS_UNK));
        Log.v("adjNPRAS_POS_UNK", String.valueOf(adjNPRAS_POS_UNK));
        Log.v("adjNASP_NEG", String.valueOf(adjNASP_NEG));
        Log.v("DRUG", String.valueOf(DRUG));
        Log.v("HEAVYALC", String.valueOf(HEAVYALC));

        Log.v("ETA", String.valueOf(ETA));
        Log.v("adjETA", String.valueOf(adjETA));
        Log.v("P6m", String.valueOf(P6m));
        Log.v("P2y", String.valueOf(P2y));
        Log.v("Pt", String.valueOf(Pt));

        Log.v("sexProScore", String.valueOf(sexProScore));*/
        return  16+4*(1-P2y); // SexproScore with PrEP
    }

    public static final int getMonthsDifference(Date date1, Date date2) {
        int m1 = Calendar.getInstance().get(Calendar.YEAR) * 12 + Calendar.getInstance().get(Calendar.MONTH);
        int m2 = Calendar.getInstance().get(Calendar.YEAR) * 12 + Calendar.getInstance().get(Calendar.MONTH);
        return m2 - m1;
    }
}
