package com.lynxstudy.lynx;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

public class URLNoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
