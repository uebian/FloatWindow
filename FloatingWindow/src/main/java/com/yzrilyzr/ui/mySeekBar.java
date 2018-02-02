package com.yzrilyzr.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;
import com.yzrilyzr.myclass.util;

public class mySeekBar extends SeekBar
{
	private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean ondown=false;
	public mySeekBar(Context c, AttributeSet a)
	{
		super(c, a);	
	}
	public mySeekBar(Context c)
	{
		this(c, null);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		float hei=getHeight(),wid=getWidth();
		float r=hei/2f,r2=hei/6f,r3=hei/3f;
		float drawx=r+(wid-2f*r)*(float)getProgress()/(float)getMax();
		float drawx2=r+(wid-2f*r)*(float)getSecondaryProgress()/(float)getMax();
		paint.setColor(0xffeeeeee);
		canvas.drawRoundRect(new RectF(r,r3,wid-r,hei-r3),r2,r2,paint);
		paint.setColor((uidata.UI_COLOR_MAIN|0xff000000)-0x90000000);
		canvas.drawRoundRect(new RectF(r,r3,drawx2,hei-r3),r2,r2,paint);
		paint.setColor(uidata.UI_COLOR_MAIN);
		canvas.drawRoundRect(new RectF(r,r3,drawx,hei-r3),r2,r2,paint);
		paint.setColor(0xffffffff);
		if(ondown)canvas.drawCircle(drawx,hei/2,r,paint);
		canvas.drawCircle(drawx,hei/2,r3,paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int a=event.getAction();
		if(a==MotionEvent.ACTION_DOWN||a==MotionEvent.ACTION_MOVE)ondown=true;
		else ondown=false;
		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,-2), WidgetUtils.measure(heightMeasureSpec, util.px(30)));
	}
}
