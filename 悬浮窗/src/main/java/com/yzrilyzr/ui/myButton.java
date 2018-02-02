package com.yzrilyzr.ui;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.yzrilyzr.floatingwindow.R;

public class myButton extends Button// implements Runnable
{
    public myButton(Context c,AttributeSet a)
    {
        super(c,a);
		if(a!=null){
        radius=a.getAttributeFloatValue(null,"radius",uidata.UI_RADIUS);
        useRound=a.getAttributeBooleanValue(null,"round",false);
        color2Back=a.getAttributeBooleanValue(null,"color2Back",false);
        }
    }
    private boolean useRound=false,color2Back=false;
    private float radius;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // TODO: Implement this method
        super.onSizeChanged(w, h, oldw, oldh);
        if(useRound)radius=h/2f;
        init();
    }
	@Override
	public void setEnabled(boolean enabled)
	{
		// TODO: Implement this method
		super.setEnabled(enabled);
		mrd=new myRippleDrawable(enabled?(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK):0xffaaaaaa,uidata.UI_COLOR_MAINHL,radius);
        mrd.setLayer(this);
        setBackgroundDrawable(mrd);
	}
    public myButton(Context c)
    {
        this(c,null);
    }
    private myRippleDrawable mrd;
    private void init()
    {
        setTextColor(color2Back?uidata.UI_TEXTCOLOR_BACK:uidata.UI_TEXTCOLOR_MAIN);
        if(uidata.UI_USETYPEFACE)setTypeface(uidata.UI_TYPEFACE);
        setTextSize(uidata.UI_TEXTSIZE_DEFAULT);
        mrd=new myRippleDrawable(isEnabled()?(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK):0xffaaaaaa,uidata.UI_COLOR_MAINHL,radius);
        mrd.setLayer(this);
        setBackgroundDrawable(mrd);
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
