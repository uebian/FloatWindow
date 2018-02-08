package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import java.io.File;
import com.yzrilyzr.icondesigner.VecView;
import com.yzrilyzr.icondesigner.VECfile;
import java.io.IOException;

public class ImageViewer implements Window.WindowInterface
{
	Bitmap b;
	public ImageViewer(Context c,Intent e) throws IOException
	{
		int type=e.getIntExtra("type",0);
		File f=new File(e.getStringExtra("path"));
		ImageView iv=new ImageView(c);
		if(type==1)
		{
			b=BitmapFactory.decodeFile(f.getAbsolutePath());
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else if(type==2)
		{
			VECfile v=VECfile.readFile(f.getAbsolutePath());
			b=VECfile.createBitmap(v,v.width,v.height);
		}
		new Window(c,util.px(300),util.px(300))
			.setTitle(f.getName())
			.setBar(8,0,0,0)
			.setWindowInterface(this)
			.addView(iv)
			.setIcon(type==1?"image":(type==2?"class":"floatingwindow"))
			.show();
		iv.setImageBitmap(b);
	}
	@Override
	public void onButtonDown(int code)
	{
		if(code==Window.ButtonCode.CLOSE)b.recycle();
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO: Implement this method
	}

	@Override
	public void onPositionChanged(int x, int y)
	{
		// TODO: Implement this method
	}
}
