package com.yzrilyzr.ui;
import android.content.Context;
import android.util.AttributeSet;

public class myLinearLayoutRound extends myLinearLayout
{
	public myLinearLayoutRound(Context c,AttributeSet a)
	{
		super(c,a);
		if(a!=null){
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
	public myLinearLayoutRound(Context c)
	{
		this(c,null);
	}
	public void init()
	{
		mrd=new myRippleDrawable(!color2Back?uidata.UI_COLOR_MAIN:uidata.UI_COLOR_BACK,uidata.UI_COLOR_MAINHL,radius);
		setBackgroundDrawable(mrd);
	}
}
