package com.aptmobility.lynx;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.UserRatingFields;

import java.util.List;

/**
 * Created by hariv_000 on 6/22/2015.
 */
public class settings_partner_rating extends Fragment {
    DatabaseHelper db;
    //The "x" and "y" position of the "Show Button" on screen.
    Point p;

    public settings_partner_rating() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_partner_ratings, container, false);


        final ImageView btn = (ImageView) view.findViewById(R.id.imgBtn_partner_ratings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                ImageView btn = (ImageView) getActivity().findViewById(R.id.imgBtn_partner_ratings);

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


        // edit text

        db = new DatabaseHelper(getActivity().getBaseContext());
        int user_id = LynxManager.getActiveUser().getUser_id();
        List<UserRatingFields> field = db.getAllUserRatingFields(user_id);
        int field_size = field.size();
        EditText[] editText = new EditText[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String edittext_id = "rating_ctg" + (i + 1);
            int et_id = getResources().getIdentifier(edittext_id, "id", getActivity().getPackageName());
            editText[i] = (EditText) view.findViewById(et_id);
            int id_of_field = field_loc.getUser_ratingfield_id();
            String hinttext = LynxManager.decryptString(field_loc.getName());
            editText[i].setTag(id_of_field);
            editText[i].setText(hinttext);
        }


        return view;
    }
    public void showPopup(View anchorView) {


        View popupView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.fragment_partner_ratings_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setHeight(500);
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        //anchorView.getLocationOnScreen(location);
        ImageView btn = (ImageView) getActivity().findViewById(R.id.imgBtn_partner_ratings);
        btn.getLocationOnScreen(location);
        p = new Point();
        p.x = location[0];
        p.y = location[1];
        int OFFSET_X = 10;
        int OFFSET_Y = 20;
        // Using location, the PopupWindow will be displayed right under anchorView //Gravity.NO_GRAVITY
        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, p.x - (popupView.getWidth() + OFFSET_X), p.y + OFFSET_Y);

    }


    @Override
    public void onResume() {
        super.onResume();
    }



}
