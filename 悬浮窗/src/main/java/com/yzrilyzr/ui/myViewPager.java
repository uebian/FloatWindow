package com.yzrilyzr.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class myViewPager extends HorizontalScrollView
{
	private int screenWidth=0,curScreen=0;
	private float lastx;
	private LinearLayout ll;
	private OnPageChangeListener lis=null;
	public myViewPager(Context context)
	{
		this(context,null);
	}
	public myViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	private void init()
	{
		ll=new LinearLayout(getContext());
		addView(ll);
		setHorizontalScrollBarEnabled(false);
	}
	public void setPages(View... views)
	{
		ll.removeAllViews();
		for(View v:views)
		{
			ll.addView(v);
			v.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,-2));
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO: Implement this method
		super.onSizeChanged(w, h, oldw, oldh);
		for(int i = 0; i < ll.getChildCount(); i++)
		{
			View view = ll.getChildAt(i);
			view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,-2));
		}
	}
	public void setCurrentItem(int i,boolean b)
	{
		curScreen=i;
		if(b)smoothScrollTo(screenWidth*i,0);
		else scrollTo(screenWidth*i,0);
	}
	public int getCurrentItem()
	{
		return curScreen;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		screenWidth = MeasureSpec.getSize(widthMeasureSpec);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		// TODO: Implement this method
		//getParent().requestDisallowInterceptTouchEvent(true);
		switch(ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				lastx=ev.getX();
				break;
			case MotionEvent.ACTION_UP:
				int l=curScreen;
				float dx=lastx-ev.getX();
				if(Math.abs(dx)>screenWidth/16)
				{
					if(dx<0)l--;
					else l++;
					if(lis!=null)lis.onPageChanged(curScreen,l);
				}
				smoothScrollTo((curScreen=l)*screenWidth,0);
				return true;
		}
		return super.onTouchEvent(ev);
	}
	public void setOnPageChangedListener(OnPageChangeListener l)
	{
		lis=l;
	}
	public interface OnPageChangeListener
	{
		public abstract void onPageChanged(int last,int newone);
	}
}
