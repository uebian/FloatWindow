package com.yzrilyzr.AudioEdit;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.yzrilyzr.FloatWindow.API;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder;

public class WaveView extends SurfaceView implements Callback
{
    Paint g=new Paint();
    byte[] data;
    Path left=new Path(),right=new Path();
    float xx1=500,xx2=600;
    boolean touch=false,start=false;
    int selectLine=1;
    float gap=4;
	private int lw=API.dip2px(6);
	SurfaceHolder holder;
    public WaveView(Context c,AttributeSet a)
    {
        super(c,a);
        g.setStyle(Paint.Style.STROKE);
        //g.setStrokeWidth(1);
        g.setStrokeWidth(API.dip2px(2));
        g.setTextSize(45f);
		holder=getHolder();
		holder.addCallback(this);
    }
    public void prev()
    {
        if(selectLine==1)xx1-=gap;
        if(selectLine==2)xx2-=gap;
    }
    public void next()
    {
        if(selectLine==1)xx1+=gap;
        if(selectLine==2)xx2+=gap;
    }

	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		stopRender();
	}
	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		startRender();
	}
	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}
	private void startRender()
	{
		if(start)return;
		start=true;
		new Thread(){
			@Override public void run()
			{
				while(start)
				{
					try
					{
						Canvas c=holder.lockCanvas();
						if(c!=null)
						{
							mdraw(c);
							holder.unlockCanvasAndPost(c);
						}
					}
					catch(Throwable e)
					{
						try
						{
							Thread.sleep(20);
						}
						catch (InterruptedException e2)
						{}
					}
				}
			}
		}.start();
	}
	private void stopRender(){
		start=false;
	}
    private void mdraw(Canvas canvas)
    {      
        try
        {
            if(data!=null)
            {
                float ww =getWidth();
                float hh = getHeight();
				float hah=hh/2;
                canvas.drawColor(Color.WHITE);
                g.setColor(Color.BLACK);
                canvas.drawLine(0,hah,ww,hah,g);
                //if(touch){
                g.setColor(Color.RED);
                canvas.drawLine(xx1,0,xx1,hh,g);
                g.setColor(Color.GREEN);
                canvas.drawLine(xx2,0,xx2,hh,g);
                //}
                float ly=0,ry=0;
                gap=ww/((float)data.length/4f);
                float x=0,y=0;
                left.reset();
                right.reset();
                for(int i=0; i<data.length/4;i++)
                {
                    x=i*gap;
                    y=(data[i*4+1]*256+data[i*4])*hh/65536f;
                    if(i==0)left.moveTo(x,y+hah);
                    else left.lineTo(x,y+hah);
                    ly+=Math.abs(y);
                    canvas.drawPoint(x,y+hah,g);
                    y=(data[i*4+3]*256+data[i*4+2])*hh/65536f;
                    if(i==0)right.moveTo(x,y+hah);
                    else right.lineTo(x,y+hah);
                    ry+=Math.abs(y);
                    canvas.drawPoint(x,y+hah,g);
                }
                g.setColor(0xaaff0000);
                canvas.drawPath(left,g);
				g.setStyle(Paint.Style.FILL);
				canvas.drawRect(0,hh-ly*8/data.length,lw,hh,g);
                g.setColor(0xaa0000ff);
				g.setStyle(Paint.Style.STROKE);
                canvas.drawPath(right,g);
				g.setStyle(Paint.Style.FILL);
				canvas.drawRect(ww-lw,hh-ry*8/data.length,ww,hh,g);
				g.setStyle(Paint.Style.STROKE);
            }
        }
        catch(Throwable e)
        {canvas.drawText(e.toString(),0/2,50,g);}
    }
    public void setData(byte[] d)
    {
        this.data=d;
        //setLayoutParams(new HorizontalScrollView.LayoutParams(50000,1024));
        //post
    }
    public byte[] getData()
    {
        int s=(int) (xx1/gap),e=(int)(xx2/gap);
        byte[] o=new byte[(e-s)*4];
        for(int i=s*4,u=0;i<e*4;i++)
        {
            o[u++]=data[i];
        }
        return o;
    }
    public void up()
	{
        int aa=(selectLine==1?(int) (xx1/gap):(int)(xx2/gap))-1;
        int l=(int) (data[aa*4]*0x100+data[aa*4+1]);
        int r=(int) (data[aa*4+2]*0x100+data[aa*4+3]+0x1*0xff);
        byte a=(byte) (l-l/0x100*0x100),b=(byte) (l/0x100);
        data[aa*4]=a;data[aa*4+1]=b;
        a=(byte) (r-r/0x100*0x100);b=(byte) (r/0x100);
        data[aa*4+2]=a;data[aa*4+3]=b;
        //
    }
    public void down()
	{
        int aa=selectLine==1?(int) (xx1/gap):(int)(xx2/gap);
        data[aa]=(byte) (data[aa]-0x1);
        //
	}
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        getParent().requestDisallowInterceptTouchEvent(true);
        // TODO: Implement this method
        int x=(int)(event.getX()/gap);
        int aa=100;
        try
        {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    float ex=event.getX();
                    if(ex>xx1-aa&&ex<xx1+aa)
                    {
                        xx1=x*gap;selectLine=1;
                    }
                    else if(ex>xx2-aa&&ex<xx2+aa)
                    {
                        xx2=x*gap;selectLine=2;
                    }
                    else
                    {
                        selectLine=0; 
                    }

                    touch=true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    ex=event.getX();
					if(ex<0)ex=0;
					if(ex>getWidth())ex=getWidth();
                    if(selectLine==1)
                    {
                        xx1=x*gap;
                    }
                    else if(selectLine==2)
                    {
                        xx2=x*gap;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    touch=false;
            }
            // out.out(data);
        }
        catch(Throwable e)
        {Toast.makeText(getContext(),e.toString(),0).show();}
        return true;
    }
}
