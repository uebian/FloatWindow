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
import com.yzrilyzr.icondesigner.VECfile;

public class StarterView extends View
{
    private Paint paint;
    private int progress=0;
    private boolean open=false,isAnim=false;
    private float dd,ee;
    private float margin;
    private float kx,ky;
    private Listener listener;
    private Bitmap[] bmp=new Bitmap[6];
    private RectF rect;
	private static final float arc=(float)(Math.PI/180f);
	private int SEL=-1;
	private String[] tip=new String[]{"添加程序","添加程序","添加程序","菜单","退出"};
    public StarterView(Context c)
    {
        super(c);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
		margin=util.px(3);
        paint.setShadowLayer(margin,0,margin/3,0x88666666);
        //int l=v.getPaddingLeft(),t=v.getPaddingTop(),r=v.getPaddingRight(),b=v.getPaddingBottom();
        //v.setPadding(l,t,r,b+=margin);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        dd=util.px(50);
        ee=util.px(40);
		int k=(int)ee;
        try
		{
			bmp[0]=VECfile.createBitmap(c,"add",k,k);
			bmp[1]=VECfile.createBitmap(c,"add",k,k);
			bmp[2]=VECfile.createBitmap(c,"add",k,k);
			bmp[3]=VECfile.createBitmap(c,"menu",k,k);
			bmp[4]=VECfile.createBitmap(c,"exit",k,k);
			bmp[5]=VECfile.createBitmap(c,"class",k,k);
		}
		catch (Exception e)
		{}
		paint.setTextSize(util.px(18));
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
        invalidate();
    }
    public void close()
    {
        open=false;
        isAnim=true;
        invalidate();
    }
    public void setListener(Listener l)
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
		rect=new RectF(kx-2f*dd,ky-2f*dd,kx+2f*dd,ky+2f*dd);
    }
    public void setIcon(int i,Bitmap b)
    {
        if(i==0)bmp[0]=scale(b);
        if(i==1)bmp[1]=scale(b);
        if(i==2)bmp[2]=scale(b);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO: Implement this metho
		paint.setColor(uidata.UI_COLOR_BACK);
		paint.setShadowLayer(margin,0,margin/3,0x88666666);
		canvas.drawArc(rect,-180,Math.min(progress,225),true,paint);
		paint.setColor(uidata.UI_COLOR_MAIN);
		if(SEL>=0&&SEL<=4&&!isAnim&&progress>=360)canvas.drawArc(rect,-180+SEL*45,45,true,paint);
		if(progress>225)
		{
			canvas.drawCircle(kx,ky,dd*((float)progress-225f)/180f,paint);
		}
		float R2=4f/3f*dd;
		paint.setShadowLayer(0,0,0,0);
		for(int i=0;i<5;i++)
		if(progress>=45*(i+1)){
			double d=arc*(22.5+45*i);
			canvas.drawBitmap(bmp[i],(float)(kx-Math.cos(d)*R2)-ee/2,(float)(ky-Math.sin(d)*R2)-ee/2,paint);
		}
		if(progress>=360)
		{
			if(SEL<0||SEL>=tip.length)canvas.drawBitmap(bmp[5],kx-ee/2,ky-ee/2,paint);
			else
			{
				paint.setColor(uidata.UI_TEXTCOLOR_MAIN);
				canvas.drawText(tip[SEL],kx,ky+paint.getTextSize()/2.5f,paint);
			}
		}
		if(isAnim)
		{
			if(progress<360&&open)
			{
				progress+=30;invalidate();
			}
			else if(progress>0&&!open)
			{
				progress-=30;invalidate();
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
    public interface Listener
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
			invalidate();
		}
		else if(act==MotionEvent.ACTION_UP)
		{
			if(listener!=null&&rr<dd*2)listener.onItemClick(SEL);
			close();
		}
        return true;
    }

}
