package com.lynxstudy.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lynxstudy.lynx.R;

import java.util.ArrayList;

/**
 * Created by Hari on 2018-05-22.
 */

public class TrendsDataPointAdapter extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> progress_values;
    private final ArrayList<String> description_values;

    public TrendsDataPointAdapter(Context mContext, ArrayList<String> progress_values, ArrayList<String> description_values) {
        this.mContext = mContext;
        this.progress_values = progress_values;
        this.description_values = description_values;
    }


    @Override
    public int getCount() {
        return progress_values.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view ==null){
            Typeface tf_bold = Typeface.createFromAsset(mContext.getResources().getAssets(),
                    "fonts/Roboto-Bold.ttf");
            Typeface tf_italic = Typeface.createFromAsset(mContext.getResources().getAssets(),
                    "fonts/Roboto-Italic.ttf");
            View grid =  inflater.inflate(R.layout.trends_datapoints_item_single,null);
            TextView progressView = (TextView) grid.findViewById(R.id.progress);
            TextView descView = (TextView)grid.findViewById(R.id.description);
            progressView.setText(progress_values.get(i));
            progressView.setTypeface(tf_bold);
            descView.setText(description_values.get(i));
            descView.setTypeface(tf_italic);
            return grid;
        }else{
            return view;
        }
    }
}
