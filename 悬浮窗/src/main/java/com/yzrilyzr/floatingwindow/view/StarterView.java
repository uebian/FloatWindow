package com.yzrilyzr.floatingwindow.view;
import android.graphics.*;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.api.API;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.uidata;

public class StarterView extends View
{
    private Paint paint;
    private int progress=0;
    private boolean open=false,isAnim=false;
    private float dd,ee;
    private float margin;
    private float kx,ky;
    private listener listener;
    private Bitmap b1,b2,b3,b4,b5,b6;
    private float l=0;
    private float t=0;
    private float r=0;
    private float b=0;
	private static final float arc=(float)(Math.PI/180f);
	private int SEL=-1;
	private String[] tip=new String[]{"添加程序","添加程序","添加程序","菜单","退出"};
    public StarterView(Context c)
    {
        super(c);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        margin=util.px(3);
        paint.setShadowLayer(margin,0,margin/3,0x88666666);
        //int l=v.getPaddingLeft(),t=v.getPaddingTop(),r=v.getPaddingRight(),b=v.getPaddingBottom();
        //v.setPadding(l,t,r,b+=margin);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        dd=util.px(50);
        ee=util.px(40);
        /*b1=scale(resBit(R.drawable.add));
		 b2=scale(resBit(R.drawable.add));
		 b3=scale(resBit(R.drawable.add));
		 b4=scale(resBit(R.drawable.menu));
		 b5=scale(resBit(R.drawable.exit));
		 b6=scale(resBit(R.drawable.start));*/
		paint.setTextSize(util.px(18));
    }
    private Bitmap resBit(int id)
    {
        return BitmapFactory.decodeResource(getResources(),id);
    }
    private Bitmap scale(Bitmap b)
    {
        Matrix Matrix=new Matrix();
        Matrix.postScale(ee/(float)b.getWidth(),ee/(float)b.getHeight());
        return Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),Matrix,false);
    }
    public void open()
    {
		if(uidata.UI_USETYPEFACE)paint.setTypeface(uidata.UI_TYPEFACE);
        if(listener!=null)listener.onAnimStart();
        open=true;
        isAnim=true;
        inv();
    }
    public void close()
    {
        open=false;
        isAnim=true;
        inv();
    }
    public void setListener(listener l)
    {
        listener=l;
    }
    public void toggle()
    {
        open=!open;
        if(open)open();
        else close();
    }
    public boolean getState()
    {
        return open;
    }
    public void setPosition(float x,float y)
    {
        kx=x;ky=y;
        l=kx-2f*dd;
        t=ky-2f*dd;
        r=kx+2f*dd;
        b=ky+2f*dd;
    }
    public void setIcon(int i,Bitmap b)
    {
        if(i==0)b1=scale(b);
        if(i==1)b2=scale(b);
        if(i==2)b3=scale(b);
    }
    private void inv()
    {
        invalidate((int)(l-margin),(int)(t-margin),(int)(r+margin),(int)(b+margin));
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO: Implement this metho
		paint.setColor(uidata.UI_COLOR_BACK);
		paint.setShadowLayer(margin,0,margin/3,0x88666666);
		canvas.drawArc(new RectF(l,t,r,b),-180,Math.min(progress,225),true,paint);
		paint.setColor(uidata.UI_COLOR_MAIN);
		if(SEL>=0&&SEL<=4&&!isAnim&&progress>=360)canvas.drawArc(new RectF(l,t,r,b),-180+SEL*45,45,true,paint);
		if(progress>225)
		{
			canvas.drawCircle(kx,ky,dd*((float)progress-225f)/180f,paint);
		}
		float R2=4f/3f*dd;
		paint.setShadowLayer(0,0,0,0);
		if(progress>=45)
		{
			canvas.drawBitmap(b1,(float)(kx-Math.cos(arc*22.5)*R2)-ee/2,(float)(ky-Math.sin(arc*22.5)*R2)-ee/2,paint);
		}
		if(progress>=90)
		{
			canvas.drawBitmap(b2,(float)(kx-Math.cos(arc*67.5)*R2)-ee/2,(float)(ky-Math.sin(arc*67.5)*R2)-ee/2,paint);
		}
		if(progress>=135)
		{
			canvas.drawBitmap(b3,(float)(kx-Math.cos(arc*112.5)*R2)-ee/2,(float)(ky-Math.sin(arc*112.5)*R2)-ee/2,paint);
		}
		if(progress>=180)
		{
			canvas.drawBitmap(b4,(float)(kx-Math.cos(arc*157.5)*R2)-ee/2,(float)(ky-Math.sin(arc*157.5)*R2)-ee/2,paint);
		}
		if(progress>=225)
		{
			canvas.drawBitmap(b5,(float)(kx-Math.cos(arc*202.5)*R2)-ee/2,(float)(ky-Math.sin(arc*202.5)*R2)-ee/2,paint);
		}
		if(progress>=360)
		{
			if(SEL<0||SEL>=tip.length)canvas.drawBitmap(b6,kx-ee/2,ky-ee/2,paint);
			else
			{
				float a=paint.measureText(tip[SEL]);
				paint.setColor(uidata.UI_TEXTCOLOR_MAIN);
				canvas.drawText(tip[SEL],kx-a/2,ky+paint.getTextSize()/2.5f,paint);
			}
		}
		if(isAnim)
		{
			if(progress<360&&open)
			{
				progress+=(25);inv();
			}
			else if(progress>0&&!open)
			{
				progress-=25;inv();
			}
			if(progress<=0)
			{
				isAnim=false;
				progress=0;
				if(listener!=null)listener.onAnimEnd();
			}
			else if(progress>=360)
			{
				progress=360;
				isAnim=false;
			}
		}
    }
    public interface listener
    {
        public abstract void onItemClick(int which);
        public abstract void onAnimEnd();
        public abstract void onAnimStart();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO: Implement this method
        if(isAnim)return false;
        float xx=event.getX(),yy=event.getY();
        float rr=(float)Math.sqrt(Math.pow(kx-xx,2)+Math.pow(ky-yy,2));
		if(rr<dd*135f/180f)SEL=5;
		else if(rr<dd*2)
        {
            int de=(int) (Math.asin((xx-kx)/rr)*180f/Math.PI)+90;
            int i=de/45;
            if(yy<ky)SEL=i;
			else if(yy>ky&&de>135&&de<180)SEL=4;
			else SEL=-1;
        }
		else SEL=-1;
		int act=event.getAction();
		if(act==MotionEvent.ACTION_MOVE)
		{
			inv();
		}
		else if(act==MotionEvent.ACTION_UP)
		{
			if(listener!=null&&rr<dd*2)listener.onItemClick(SEL);
			close();
		}
        return true;
    }

}
