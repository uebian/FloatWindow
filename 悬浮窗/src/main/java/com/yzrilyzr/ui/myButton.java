package com.yzrilyzr.ui;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.yzrilyzr.floatingwindow.R;
import android.graphics.drawable.Drawable;

public class myButton extends Button implements myTouchProcessor.Event
{
	private myTouchProcessor pr=new myTouchProcessor(this);
	private myRippleDrawable mrd;
    private boolean useRound=false,color2Back=false;
    public myButton(Context c,AttributeSet a)
    {
        super(c,a);
		float radius=uidata.UI_RADIUS;
		if(a!=null)
		{
			radius=a.getAttributeFloatValue(null,"radius",uidata.UI_RADIUS);
			useRound=a.getAttributeBooleanValue(null,"round",false);
			color2Back=a.getAttributeBooleanValue(null,"color2Back",false);
        }
		setTextColor(color2Back?uidata.UI_TEXTCOLOR_BACK:uidata.UI_TEXTCOLOR_MAIN);
        if(uidata.UI_USETYPEFACE)setTypeface(uidata.UI_TYPEFACE);
        setTextSize(uidata.UI_TEXTSIZE_DEFAULT);
        mrd=new myRippleDrawable(isEnabled()?(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK):0xffaaaaaa,radius);
        mrd.setLayer(this);
        setBackground(mrd);
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
	public myButton(Context c)
    {
        this(c,null);
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
