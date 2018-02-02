package com.yzrilyzr.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.myclass.util;
import android.widget.CheckBox;

public class myCheckBox extends CheckBox
{
	private boolean isChecked=false,isOn=false;
	private Bitmap bitmap;
	private OnCheckedChangeListener occl=null;
	public myCheckBox(Context c,AttributeSet a)
	{
		super(c,a);
		if(a!=null)method=a.getAttributeValue(null,"onClick");
	}
	public myCheckBox(Context c)
	{
		this(c,null);
	}
	public void setChecked(boolean c)
	{
		isChecked=c;invalidate();
	}
	public boolean getChecked()
	{
		return isChecked;
	}
	public void setListener(OnCheckedChangeListener o)
	{
		occl=o;
	}
	public interface OnCheckedChangeListener
	{
		public abstract void onCheckedChange(myCheckBox mcb,boolean state);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method

		Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(uidata.UI_COLOR_MAIN);
		paint.setStyle(Paint.Style.FILL);
		int w=getWidth(),h=getHeight();
		float r=util.px(5);
		int delta=util.px(5);
		RectF rf=new RectF(delta,delta,w-delta,h-delta);
		paint.setStrokeWidth(delta);
		if(isChecked)
		{
			canvas.drawRoundRect(rf,r,r,paint);
			Matrix matrix = new Matrix();
			float s=(float)getWidth()/(float)bitmap.getWidth();
			matrix.postScale(s,s); //长和宽放大缩小的比例
			//Bitmap resizeBmp = Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
			canvas.drawBitmap(bitmap,matrix,paint);
		}
		else
		{
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(0xffeeeeee);
			paint.setStrokeWidth(util.px(2));
			canvas.drawRoundRect(rf,r,r,paint);
		}

	}
	private String method;
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO: Implement this method
		//getParent().requestDisallowInterceptTouchEvent(true);
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				isOn=true;invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				float x=event.getX(),y=event.getY();
				if(x<0||x>getWidth()||y<0||y>getHeight())isOn=false;
				break;
			case MotionEvent.ACTION_UP:
				if(isOn)
				{
					isOn=false;
					isChecked=!isChecked;
					if(occl!=null)occl.onCheckedChange(this,isChecked);
					if(method!=null)util.call(getContext().getClass(),method,new Class[]{View.class,boolean.class},getContext(),new Object[]{this,isChecked});
					invalidate();
				}
				break;
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,util.px(30)),WidgetUtils.measure(heightMeasureSpec,util.px(30)));
		myIconDrawer m=new myIconDrawer(util.px(30));
		m.move(m.ww*3,m.ww*11);
		m.line(m.ww*7,m.ww*15);
		m.line(m.ww*7,m.ww*15);
		m.line(m.ww*17,m.ww*5);
		m.draw(myIconDrawer.DrawType.PATH,false);
		bitmap=m.getBitmap();
	}

}
