package com.yzrilyzr.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioButton;
import com.yzrilyzr.myclass.util;

public class myRadioButton extends RadioButton
{
	Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
	public myRadioButton(Context c,AttributeSet a)
	{
		super(c,a);
	}
	public myRadioButton(Context c)
	{
		this(c,null);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		float w=getWidth();
		if(isChecked())
		{
			paint.setColor(uidata.UI_COLOR_MAIN);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(w/2f,w/2f,w/5f,paint);
		}
		paint.setStrokeWidth(w/20f);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xffeeeeee);
		canvas.drawCircle(w/2f,w/2f,w/4f,paint);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,util.px(30)),WidgetUtils.measure(heightMeasureSpec,util.px(30)));
	}
}
