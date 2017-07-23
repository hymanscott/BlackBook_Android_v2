package com.lynxstudy.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lynxstudy.lynx.R;
import com.lynxstudy.lynx.RegistrationFragment;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * Created by Hari on 2017-06-13.
 */

public class SpinnerDropDownAdapter extends BaseAdapter {

    private ArrayList<String> mListItems;
    private LayoutInflater mInflater;
    private TextView mSelectedItems;
    private static int selectedCount = 0;
    private static String firstSelected = "";
    private ViewHolder holder;
    private static String selected = "";	//shortened selected values representation
    private boolean[] checkSelected;
    private boolean is_profile;
    public static String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        SpinnerDropDownAdapter.selected = selected;
    }
    public SpinnerDropDownAdapter(Context context, ArrayList<String> items,
                               TextView tv,boolean[] checkSelected,boolean is_profile) {
        mListItems = new ArrayList<String>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;
        this.checkSelected = checkSelected;
        this.is_profile = is_profile;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_popup_row_grey, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.SelectOption);
            holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mListItems.get(position));

        final int position1 = position;

        //whenever the checkbox is clicked the selected values textview is updated with new selected values
        holder.chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setText(position1);
            }
        });

        if(checkSelected[position])
            holder.chkbox.setChecked(true);
        else
            holder.chkbox.setChecked(false);
        return convertView;
    }


    /*
     * Function which updates the selected values display and information(checkSelected[])
     * */
    private void setText(int position1){
        if (!checkSelected[position1]) {
            checkSelected[position1] = true;
            selectedCount++;
        } else {
            checkSelected[position1] = false;
            selectedCount--;
        }

        if (selectedCount == 0) {
            mSelectedItems.setText(R.string.select_race);
            if(is_profile)
                mSelectedItems.setTextColor(Color.parseColor("#80444444"));
            else
                mSelectedItems.setTextColor(Color.parseColor("#80FFFFFF"));
        } else if (selectedCount == 1) {
            for (int i = 0; i < checkSelected.length; i++) {
                if (checkSelected[i] == true) {
                    firstSelected = mListItems.get(i);
                    break;
                }
            }
            mSelectedItems.setText(firstSelected);
            if(is_profile)
                mSelectedItems.setTextColor(Color.parseColor("#FF444444"));
            else
                mSelectedItems.setTextColor(Color.parseColor("#FFFFFF"));

            setSelected(firstSelected);
        } else if (selectedCount > 1) {
            for (int i = 0; i < checkSelected.length; i++) {
                if (checkSelected[i] == true) {
                    firstSelected = mListItems.get(i);
                    //firstSelected += mListItems.get(i)+",";
                    break;
                }
            }
            String text = "";
            for(int j=0;j<checkSelected.length;j++){
                if (checkSelected[j]) {
                    if(j!=checkSelected.length-1)
                        text += mListItems.get(j)+",";
                    else
                        text += mListItems.get(j);
                }
            }
			/*mSelectedItems.setText(firstSelected + " & "+ (selectedCount - 1) + " more");
			setSelected(firstSelected + " & "+ (selectedCount - 1) + " more");*/
            if (text.endsWith(",")) {
                text = text.substring(0, text.length()-1);
            }
            mSelectedItems.setText(text);
            if(is_profile)
                mSelectedItems.setTextColor(Color.parseColor("#FF444444"));
            else
                mSelectedItems.setTextColor(Color.parseColor("#FFFFFF"));
            setSelected(text);
        }
    }

    private class ViewHolder {
        TextView tv;
        CheckBox chkbox;
    }
}
