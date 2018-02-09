package com.yzrilyzr.ui;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.graphics.*;
import com.yzrilyzr.myclass.util;

public class myImageButton extends ImageButton implements myTouchProcessor.Event
{
	private myTouchProcessor pr=new myTouchProcessor(this);
    private myRippleDrawable mrd;
	private boolean useRound=false,color2Back=false;
    public myImageButton(Context c,AttributeSet a)
    {
        super(c,a);
        setScaleType(ImageView.ScaleType.FIT_CENTER);
        WidgetUtils.setIcon(this,a);
		float radius=uidata.UI_RADIUS;
		if(a!=null)
		{
			radius=a.getAttributeFloatValue(null,"radius",uidata.UI_RADIUS);
			useRound=a.getAttributeBooleanValue(null,"round",false);
			color2Back=a.getAttributeBooleanValue(null,"color2Back",false);
        }
		mrd=new myRippleDrawable(isEnabled()?(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK):0xffaaaaaa,radius);
        mrd.setLayer(this);
        setBackgroundDrawable(mrd);
    }
	public myImageButton(Context c){
		this(c,null);
	}
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // TODO: Implement this method
        super.onSizeChanged(w, h, oldw, oldh);
        if(useRound)mrd.setRadius(h/2f);
    }
	@Override
	public void setEnabled(boolean enabled)
	{
		// TODO: Implement this method
		super.setEnabled(enabled);
		mrd.setColor(enabled?(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK):0xffaaaaaa);
	}
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(pr.process(this,event))return true;
		return super.onTouchEvent(event);
	}
	@Override
	public void onDown(View v, MotionEvent m)
	{
		mrd.shortRipple(m.getX(),m.getY());
	}
	@Override
	public void onUp(View v, MotionEvent m)
	{
	}
	@Override
	public boolean onView(View v, MotionEvent m)
	{
		return false;
	}
	@Override
	public void onClick(View v)
	{
	}
	@Override
	public boolean onLongClick(View v, MotionEvent m)
	{
		mrd.longRipple(m.getX(),m.getY());
		return false;
	}
}
