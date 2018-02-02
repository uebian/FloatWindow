package com.yzrilyzr.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.yzrilyzr.myclass.util;

public class myProgressBar extends ProgressBar
{
	private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);;
    public myProgressBar(Context c,AttributeSet a)
    {
        super(c,a);
		setIndeterminate(false);
    }
    public myProgressBar(Context c)
    {
		this(c,null);
	}
    @Override
    protected void onDraw(Canvas canvas)
    {
		float hei=getHeight(),wid=getWidth(),r=hei/2;
		float drawx=wid*(float)getProgress()/(float)getMax();
		float drawx2=wid*(float)getSecondaryProgress()/(float)getMax();
		paint.setColor(0xffeeeeee);
		canvas.drawRoundRect(new RectF(0,0,wid,hei),r,r,paint);
		paint.setColor((uidata.UI_COLOR_MAIN|0xff000000)-0x90000000);
		canvas.drawRoundRect(new RectF(0,0,drawx2,hei),r,r,paint);
		paint.setColor(uidata.UI_COLOR_MAIN);
		canvas.drawRoundRect(new RectF(0,0,drawx,hei),r,r,paint);
	}
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,-2),WidgetUtils.measure(heightMeasureSpec,util.px(10)));
    }
}
