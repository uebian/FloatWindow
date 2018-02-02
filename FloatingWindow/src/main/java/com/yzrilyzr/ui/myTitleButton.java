package com.yzrilyzr.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.myclass.util;

public class myTitleButton extends ImageButton
{
	private Context ctx;
	private String text=null;
	private myRippleDrawable mrd;
	public myTitleButton(Context c,AttributeSet a)
	{
		super(c,a);
		ctx=c;
		if(a!=null)text=a.getAttributeValue(null,"text");
     	int p=uidata.UI_PADDING_DEFAULT;
		setPadding(p,p,p,p);
		mrd=new myRippleDrawable(uidata.UI_COLOR_MAIN,uidata.UI_COLOR_MAINHL,0);
		setBackground(mrd);
		WidgetUtils.setIcon(this,a);
		setScaleType(ImageView.ScaleType.FIT_XY);
		setOnTouchListener(new myTouchListener(){
				@Override public void onDown(View v,MotionEvent m)
				{mrd.shortRipple(m.getX(),m.getY());}
				@Override public boolean onLongClick(View v,MotionEvent m)
				{
					if(text!=null)
					{
						Toast t=new Toast(ctx);
						ViewGroup vg=(ViewGroup) LayoutInflater.from(ctx).inflate(R.layout.layout_toast,null);
						t.setView(vg);
						t.setDuration(1000);
						t.setGravity(Gravity.LEFT|Gravity.TOP,(int)(m.getRawX()-m.getX()),(int)(m.getRawY()-m.getY()+getHeight()/2));
						((TextView)vg.getChildAt(0)).setText(text);
						t.show();
					}
					return true;
				}
			});
	}
	public myTitleButton(Context c)
	{
		this(c,null);
	}
	public void setText(String s)
	{
		text=s;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO: Implement this method
		int i=util.px(50);
		setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,i),WidgetUtils.measure(heightMeasureSpec,i));
	}


}