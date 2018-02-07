package com.yzrilyzr.ui;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import com.yzrilyzr.icondesigner.VECfile;
import com.yzrilyzr.myclass.util;
public class WidgetUtils
{
	//setMeasuredDimension(WidgetUtils.measureWidth(widthMeasureSpec,wid),WidgetUtils.measureHeight(heightMeasureSpec,tool.dip2px(ctx,30)));
	public static int measure(int measureSpec, int def)
	{
		int result = 0;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.EXACTLY)
		{
			result = size;
		}
		else
		{
			result = def;
			if (mode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, size);
			}
		}
		return result;
	}	
	public static void setIcon(ImageView v,AttributeSet a)
	{
		if(a==null)return;
		String p=a.getAttributeValue(null,"vec");
		if(p==null)return;
		try
		{
			v.setImageBitmap(VECfile.createBitmap(v.getContext(),p,util.px(50),util.px(50)));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}
