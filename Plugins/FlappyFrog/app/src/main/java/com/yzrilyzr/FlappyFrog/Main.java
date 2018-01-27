package com.yzrilyzr.FlappyFrog;

import android.content.*;
import android.graphics.*;
import android.media.*;
import android.view.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

import android.content.pm.PackageInfo;
import android.widget.Toast;
import com.yzrilyzr.FloatWindow.API;
public class Main implements SurfaceHolder.Callback,Runnable
{
    SurfaceHolder holder;
    @Override
    public void surfaceCreated(SurfaceHolder p1)
    {
        // TODO: Implement this method
		holder=p1;
        startRender();
    }
    @Override
    public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
    {
        // TODO: Implement this method
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p1)
    {
        // TODO: Implement this method
        rendering=false;
    }

    @Override
    public void run()
    {
        // TODO: Implement this method
        rendering=true;
        Canvas c=null;
        while(rendering)
        {
            try
            {
                c=holder.lockCanvas();
                c.drawColor(0xffdcedff);
                render(c);
                holder.unlockCanvasAndPost(c);
            }
            catch(Throwable e)
            {
            }
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{}
        }

    }
    public float dip(float a)
    {
        return ctx.getResources().getDisplayMetrics().density*a;
    }
    public InputStream loadAsset(String file)
    {
        try
        {
            InputStream is=null;
            String apk=ctx.getPackageManager().getPackageInfo("com.yzrilyzr.FlappyFrog",PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.publicSourceDir;
            ZipFile zf = new ZipFile(apk);
            ZipEntry entry=zf.getEntry(file);
            is=zf.getInputStream(entry);
            return is;   
        }
        catch(Throwable e)
        {}
        return null;
    }
    public Bitmap bmpFromAsset(String file) throws FileNotFoundException
    {
        InputStream is=loadAsset(file);
        if(is==null)throw new FileNotFoundException("");
        return BitmapFactory.decodeStream(is);
    }
    public Bitmap scale(Bitmap b,float r)
    {
        Matrix Matrix=new Matrix();
        Matrix.postScale(r,r);
        return Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),Matrix,false);
    }
    Context ctx;
    SurfaceView view;
    Object window;
    boolean rendering=false,isStart=false,gameover=false,go2=false,playBgm=false,isLoad=false;
    Paint paint;
    int deltaTime=10,btime,totalTime=0,pass=0;
    float meter=0;
    Bitmap[] bmps;
    long time=0;
    int passedPipe=0;
	int gap=400;
    ArrayList<rectObj> PIPES,CLOUDS;
    rectObj FROG,PIPE,PASS;
    rectObj restartObj,bgmObj;
    String txt="";
    public Main(Context ctx,Intent intent) throws Throwable
    {
		this.ctx=ctx;
		PIPES=new ArrayList<rectObj>();
        CLOUDS=new ArrayList<rectObj>();
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(dip(18f));
		view=new SurfaceView(ctx){
            @Override public boolean onTouchEvent(MotionEvent e)
            {
                Touch(e);
                return true;
            }
        };
        holder=view.getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
        holder.addCallback(this);
        window=API.class2Object(API.WINDOW_CLASS,new Class[]{Context.class,int.class,int.class},new Object[]{ctx,(int)dip(270),(int)dip(480)});
        API.invoke(window,"setTitle","FlappyFrog");
        API.invoke(window,"show");
        API.invoke(window,"setBar",8,0,0,0);
        API.invoke(window,"addView",view);
        API.invoke(window,"setInterface",
            new API.WindowInterface(){
                @Override
                public void onButtonDown(int code)
                {
                    // TODO: Implement this method
                    if(code==API.ButtonCode.CLOSE)rendering=false;
                    if(code==API.ButtonCode.MIN)
                    {
                        try
                        {
                            boolean b=API.invoke(window,"getMin");
                            if(b)
                            {
                                rendering=false;
                                //ViewGroup v=(ViewGroup) API.invoke(window,"getContentView");
                                //v.removeAllViews();
                            }
                            else
                            {
                                //API.invoke(window,"addView",view);
                                startRender();
                            }
                        }
                        catch (Throwable e)
                        {}
                    }
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
            });
		bmps=new Bitmap[9];
        String im="assets/images/";
        Bitmap c=bmpFromAsset(im+"clouds.png");
        for(int i=0;i<=3;i++)
            bmps[i]=Bitmap.createBitmap(c,0,i*64,128,64);
        bmps[4]=scale(bmpFromAsset(im+"frog.png"),dip(2f/3f));
        bmps[5]=scale(bmpFromAsset(im+"ground.png"),dip(5f/3f));
        bmps[6]=scale(bmpFromAsset(im+"pipe.png"),dip(1f));
        Matrix m=new Matrix();
        m.postRotate(180);
        bmps[7]=Bitmap.createBitmap(bmps[6],0,0,bmps[6].getWidth(),bmps[6].getHeight(),m,false);
        bmps[8]=Bitmap.createBitmap(bmps[4],0,0,bmps[4].getWidth(),bmps[4].getHeight(),m,false);
        FROG=new rectObj(0,0,bmps[4].getWidth(),bmps[4].getHeight(),0,0);
        PIPE=new rectObj(0,0,bmps[6].getWidth(),bmps[6].getHeight(),0,0);
        restartObj=new rectObj(0,0,0,0,0,0);
        bgmObj=new rectObj(0,0,0,0,0,0);
    }
	public void playSound(final String file)
    {
        new Thread(new Runnable(){
                @Override
                public void run()
                {
                    // TODO: Implement this method
                    try
                    {
                        int TEST_SR =44100;
                        int TEST_CONF =AudioFormat.CHANNEL_CONFIGURATION_STEREO;
                        int TEST_FORMAT= AudioFormat.ENCODING_PCM_16BIT;
                        int TEST_MODE =AudioTrack.MODE_STREAM;
                        int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
                        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
                        AudioTrack track=new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,2*minBuffSize, TEST_MODE);
                        track.play();
                        for(int i=0;i<1;i++)
                        {
                            InputStream is=loadAsset("assets/sounds/"+file+".wav");
                            byte[] bu=new byte[2*minBuffSize];
                            while(is.read(bu)!=-1)
                            {
                                if(!playBgm&&file.equals("bgm"))break;
                                track.write(bu,0,bu.length);
                            }
                            if(file.equals("bgm")&&playBgm)i=-1;
                        }
                        track.stop();
                        track.release();
                    }
                    catch(Throwable e)
                    {txt=e+"";}
                }
            }).start();
    }
    public void Touch(MotionEvent e)
    {
        if(!isLoad)return;
        if(e.getAction()==MotionEvent.ACTION_DOWN)
        {
            if(collsion(bgmObj,new rectObj(e.getX(),e.getY(),0,0,0,0),0,0))
            {
                playBgm=!playBgm;
                if(playBgm)playSound("bgm");
            }

            if(!isStart)
            {
                if(gameover&&collsion(restartObj,new rectObj(e.getX(),e.getY(),0,0,0,0),0,0))
                {
                    gameover=false;
                    FROG.vy=0;
                    PIPES.clear();
                }
                else if(!gameover)
                {
                    isStart=true;
                    time=System.currentTimeMillis();
                    passedPipe=0;
                    meter=-5*deltaTime;
                    go2=false;
                    createMap(0,4);
                    createClouds();
                    FROG.y=view.getHeight()/2-FROG.h/2;
                }
            }
            if(isStart&&!gameover)
            {
                FROG.vy=deltaTime*2;
                playSound("flap");
            }
        }
    }
    public void createClouds()
    {
        CLOUDS.clear();
        for(int i=0;i<10;i++)
        {
            float scale=(float)(Math.random()*3f)+3f;
            int x=new Random().nextInt(view.getWidth())+view.getWidth();
            int y=new Random().nextInt(view.getHeight()/2);
            int vx=-5-new Random().nextInt(5);
            CLOUDS.add(new rectObj(x,y,128f*scale,64f*scale,vx,new Random().nextInt(4)));
        }
    }
    public void createMap(float mete,int off)
    {
        int s=view.getHeight()/3;
        PIPES.clear();
        for(int i=off;i<5+off;i++)
        {
            int yy=new Random().nextInt(view.getHeight()/2);
            PIPES.add(new rectObj(i*gap+mete,yy-PIPE.h,PIPE.w,PIPE.h,0,0));
            PIPES.add(new rectObj(i*gap+mete,yy+s,PIPE.w,PIPE.h,0,0));
            PIPES.add(new rectObj(i*gap+mete,yy,PIPE.w,s,0,0));
        }
    }
    public void startRender()
    {
        if(rendering)return;
        new Thread(this).start();
    }
    public void render(Canvas canvas)
    {
        int vw=view.getWidth(),vh=view.getHeight();

        float y=(float)Math.sin(Math.PI/180f*meter)*vh/40f;
        FROG.x=vw/3-FROG.w/2;
        if(!isStart&&!gameover)FROG.y=vh/2-FROG.h/2+y;
        if(isStart||gameover)
        {
            FROG.y-=FROG.vy;
            FROG.vy-=deltaTime/10;
        }
        if(FROG.y<0)
        {
            FROG.y=0;
        }
        if(FROG.y>vh-FROG.h)
        {
            FROG.y=vh-FROG.h;
            gameover=true;
        }

        for(int i=0;i<PIPES.size();i+=3)
        {
            rectObj top=PIPES.get(i);
            rectObj bot=PIPES.get(i+1);
            rectObj ppp=PIPES.get(i+2);
            if(top.x-meter+top.w>0)
            {
                canvas.drawBitmap(bmps[7],top.x-meter,top.y,paint);
                canvas.drawBitmap(bmps[6],bot.x-meter,bot.y,paint);
                if(collsion(FROG,top,meter,0)||collsion(FROG,bot,meter,0))
                {
                    gameover=true;
                }
                else if(collsion(FROG,ppp,meter,0)&&PASS!=ppp&&!gameover)
                {
                    PASS=ppp;
                    passedPipe++;
                    int s=view.getHeight()/3;
                    int gap=(int)(this.gap+PIPES.get(PIPES.size()-1).x);
                    int yy=new Random().nextInt(view.getHeight()/2);
                    PIPES.add(new rectObj(gap,yy-PIPE.h,PIPE.w,PIPE.h,0,0));
                    PIPES.add(new rectObj(gap,yy+s,PIPE.w,PIPE.h,0,0));
                    PIPES.add(new rectObj(gap,yy,PIPE.w,s,0,0));
                    playSound("score"+(new Random().nextInt(9)+1));
                }
            }
            if(top.x-meter+top.w<-deltaTime*40)
            {
                PIPES.remove(top);
                PIPES.remove(ppp);
                PIPES.remove(bot);
            }
            if(top.x-meter>vw)break;
        }
        if(!go2&&gameover)
        {
            go2=true;
            totalTime+=btime;
            isStart=false;
            playSound("hurt"+(new Random().nextInt(8)+1));
        }
        if(FROG.vy>-20||gameover)canvas.drawBitmap(gameover?bmps[8]:bmps[4],FROG.x,FROG.y,paint);
        else if(!gameover)
        {
            Matrix m=new Matrix();
            m.postRotate(-2*FROG.vy,FROG.w/2,FROG.h/2);
            m.postTranslate(FROG.x,FROG.y);
            canvas.drawBitmap(bmps[4],m,paint);
        }

        float w5=bmps[5].getWidth();
        for(float x=-meter%w5-w5;x<vw;x+=w5)
        {
            canvas.drawBitmap(bmps[5],x,vh-bmps[5].getHeight(),paint);
        }

        for(int i=0;i<CLOUDS.size();i++)
        {
            rectObj r=CLOUDS.get(i);
            r.x+=r.vx;
            Matrix m=new Matrix();
            m.postScale(r.w/128f,r.h/64f);
            m.postTranslate(r.x,r.y);
            canvas.drawBitmap(bmps[(int)r.vy],m,paint);
            if(r.x+r.w<-r.w)
            {
                CLOUDS.remove(r);
                Random ran=new Random();
                float scale=(float)(Math.random()*3f)+3f;
                int xx=ran.nextInt(vw)+vw;
                int yy=ran.nextInt(vh/2);
                int vx=-5-ran.nextInt(5);
                CLOUDS.add(new rectObj(xx,yy,128f*scale,64f*scale,vx,ran.nextInt(4)));
            }
            if(r.x>vw)break;
        }
        paint.setTextSize(dip(18));
        long ntime=System.currentTimeMillis();
        long atime=isStart?(ntime-time)/1000:0;
        if(!gameover)btime=(int)atime;
        String a="+"+passedPipe+"s";

        paint.setColor(0xff000000);
        canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/3-paint.getTextSize(),paint);
        paint.setColor(0xffff0000);
        a="-"+btime+"s"+"\n"+txt;
        canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/3,paint);
        paint.setTextSize(dip(13));
        a="累计被续"+totalTime+"秒";
        if(!isStart||gameover)canvas.drawText(a,vw/2-paint.measureText(a)/2,paint.getTextSize(),paint);
        paint.setColor(0xff000000);
        a="\"5\"可奉告";
        canvas.drawText(a,0,paint.getTextSize(),paint);
        a="请州长夫人演唱";
        float tw=paint.measureText(a);
        canvas.drawText(a,bgmObj.x=vw-tw,paint.getTextSize(),paint);
        bgmObj.y=0;
        bgmObj.h=paint.getTextSize();
        bgmObj.w=tw;

        paint.setTextSize(dip(18));
        if(gameover)
        {
            paint.setColor(0xff0000000);
            a="我为长者续命"+passedPipe+"秒";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/2,paint);
            a="志己的生命减少"+btime+"秒";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/2+paint.getTextSize(),paint);
            int i=(int)(((float)passedPipe/(float)btime)*100);
            a="而且这个效率efficiency:"+i+"%";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/2+2*paint.getTextSize(),paint);
            a="重新续";
            paint.setTextSize(dip(21));
            float ew=paint.measureText(a);
            canvas.drawText(a,restartObj.x=vw/2-ew/2,vh*3/4,paint);
            restartObj.y=vh*3/4-paint.getTextSize();
            restartObj.h=paint.getTextSize();
            restartObj.w=ew;
        }
        if(!isLoad)
        {
            canvas.drawColor(0xff000000);
            paint.setColor(0xffffffff);
            paint.setTextSize(dip(18));
            a="[微小的提示]";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/4,paint);
            a="为了获得坠好的游戏体验，请:";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/4+paint.getTextSize(),paint);
            a="打开音量";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/4+2*paint.getTextSize(),paint);
            a="穿上红色的衣服";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/4+3*paint.getTextSize(),paint);
            paint.setColor(0xffff0000);
            paint.setTextSize(dip(20));
            a="Loading…";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/2+paint.getTextSize(),paint);
            a="历史的行程:"+(int)(meter/5)+"%";
            canvas.drawText(a,vw/2-paint.measureText(a)/2,vh/2+3*paint.getTextSize(),paint);
            if(meter>500)isLoad=true;
        }
        if(!gameover)meter+=(float)deltaTime/1.5f;
    }

    public boolean collsion(rectObj a,rectObj b,float ax,float ay)
    {
        if(
            a.x+ax-b.x<b.w&&a.y+ay-b.y<b.h&&a.x+ax>b.x&&a.y+ay>b.y||//被检测矩形在该矩形的右下
            b.x-a.x-ax<a.w&&b.y-a.y-ay<a.h&&a.x+ax<b.x&&a.y+ay<b.y||//被检测矩形在该矩形的左上
            a.x+ax-b.x<b.w&&b.y-a.y-ay<a.h&&a.x+ax>b.x&&a.y+ay<b.y||//被检测矩形在该矩形的右上
            b.x-a.x-ax<a.w&&a.y+ay-b.y<b.h&&a.x+ax<b.x&&a.y+ay>b.y)//被检测矩形在该矩形的左下
            return true;

        return false;
    }


    public class rectObj
    {
        public float x,y,w,h,vx,vy;
        public rectObj(float x,float y,float w,float h,float vx,float vy)
        {
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
            this.vx=vx;
            this.vy=vy;
        }
    }
}
