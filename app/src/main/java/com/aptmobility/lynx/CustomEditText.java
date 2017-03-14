package com.aptmobility.lynx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by safiq on 05/08/15.
 */


public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTextView_fontAssetName:
                    try {
                        Typeface font = Typeface.createFromAsset(getResources().getAssets(), a.getString(attr));
                        if (font != null) {
                            this.setTypeface(font);
                        }
                    } catch (RuntimeException e) {

                    }
            }
        }
    }
}
