package com.yzrilyzr.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yzrilyzr.ui.uidata;

public class myLinearLayoutRipple extends myLinearLayout
{
    public myLinearLayoutRipple(Context c,AttributeSet a)
    {
        super(c,a);
		if(a!=null)
		{
			radius=a.getAttributeFloatValue(null,"radius",uidata.UI_RADIUS);
			useRound=a.getAttributeBooleanValue(null,"round",false);
			color2Back=a.getAttributeBooleanValue(null,"color2Back",false);
		}
		init();
    }
    private boolean useRound=false,color2Back=false;
    private float radius;
    private myRippleDrawable mrd;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // TODO: Implement this method
        super.onSizeChanged(w, h, oldw, oldh);
        if(useRound)radius=h/2f;
        init();
    }
    public myLinearLayoutRipple(Context c)
    {
        this(c,null);
    }
    public void init()
    {
        mrd=new myRippleDrawable(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK,uidata.UI_COLOR_MAINHL,radius);
        setBackgroundDrawable(mrd);
        mrd.setLayer(this);
        setOnTouchListener(new myTouchListener(){
                @Override public boolean onLongClick(View v,MotionEvent m)
                {
                    mrd.longRipple(m.getX(),m.getY());
                    return false;
                };
                @Override public void onDown(View v,MotionEvent m)
                {
                    mrd.shortRipple(m.getX(),m.getY());
                }});

    }
}

