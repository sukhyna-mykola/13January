package com.game.kolas.mygame.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mykola on 26.12.16.
 */

public class  CustomFontButton extends Button {

    public CustomFontButton(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/myFont.ttf");
        setTypeface(customFont);
    }
}
