package com.mcindoe.workoutwhiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FractionTranslateLinearLayout extends LinearLayout {

    public FractionTranslateLinearLayout(Context context) {
		super(context);
	}
	
	public FractionTranslateLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FractionTranslateLinearLayout(Context context, AttributeSet attrs, int defStyles) {
		super(context, attrs, defStyles);
	}

	private int screenWidth;
    private float fractionX;

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH){

        // Assign the actual screen width to our class variable.
        screenWidth = w;            

        super.onSizeChanged(w, h, oldW, oldH);
    }
    
    public int getScreenWidth() {
    	return screenWidth;
    }

    public float getFractionX(){

        return fractionX;
    }

    public void setXFraction(float xFraction){

        this.fractionX = xFraction;

        // When we modify the xFraction, we want to adjust the x translation
        // accordingly.  Here, the scale is that if xFraction is -1, then
        // the layout is off screen to the left, if xFraction is 0, then the 
        // layout is exactly on the screen, and if xFraction is 1, then the 
        // layout is completely offscreen to the right.
        setX((screenWidth > 0) ? (xFraction * screenWidth) : 0);
    }
}