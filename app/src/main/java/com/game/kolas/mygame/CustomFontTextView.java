package com.game.kolas.mygame;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mykola on 26.12.16.
 */

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/myFont.ttf");
        setTypeface(customFont);
    }
}