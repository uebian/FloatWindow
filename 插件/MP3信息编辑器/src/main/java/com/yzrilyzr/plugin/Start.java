package com.yzrilyzr.plugin;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import android.app.Service;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import com.yzrilyzr.FloatWindow.API;

public class Start extends Service
{
    String file=null;
    MediaPlayer mp;
    ViewGroup view;
    Object seekbar;
    TextView t1,t2;
    public Start(final Context ctx,Intent Intent) throws Throwable
    {
        //View v=(View)data.get("view")
        Object o=API.class2Object(API.WINDOW_CLASS,new Class[]{Context.class,int.class,int.class},new Object[]{ctx,-2,-2});
        API.invoke(o,"show");
        API.invoke(o,"setTitle","音乐播放器");
        view=(ViewGroup) API.parseXmlViewFromFile(ctx,"main","com.yzrilyzr.plugin");
        API.invoke(o,"addView",new Class[]{View.class},new Object[]{view});
        API.invoke(o,"setIcon",new Class[]{Drawable.class},new Object[]{ctx.getApplicationInfo().loadIcon(ctx.getPackageManager())});
        file=Intent.getStringExtra("path");
        if(file!=null)
        {
            mp=new MediaPlayer();
            mp.setDataSource(file);
            mp.prepare();
            mp.start();
            final Handler ha=new Handler();
            ha.postDelayed(
                new Runnable(){
                    @Override
                    public void run()
                    {
                        // TODO: Implement this method
                        //mu=new MusicId3(file);
                        try
                        {
                            t1.setText(parseTime(mp.getCurrentPosition()));
                            API.invoke(seekbar,"setProgress",new Class[]{int.class},new Object[]{mp.getCurrentPosition()});
                            if(mp.getCurrentPosition()<mp.getDuration())ha.postDelayed(this,50);
                        }
                        catch (Throwable e)
                        {
                        }
                    }
                },50);
            MusicId3 id3=new MusicId3(file);
            music music=id3.getMusic();
            byte[] b=(byte[])music.APIC;
            ((ImageView)view.getChildAt(1)).setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
            ((TextView)view.getChildAt(0)).setText(music.TPE1+"\n"+music.TIT2+"\n"+music.TALB);
            LinearLayout l=(LinearLayout)view.getChildAt(3);
            t1=(TextView) l.getChildAt(0);
            t2=(TextView) l.getChildAt(2);
            seekbar=l.getChildAt(1);
            API.invoke(seekbar,"setMax",new Class[]{int.class},new Object[]{mp.getDuration()});
            t2.setText(parseTime(mp.getDuration()));
        }
        //throw new Exception("ok");
    }
    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        return null;
    }

    public String parseTime(long ms)
    {
        int m=(int)Math.floor(ms/60000);
        int s=(int)Math.floor((ms-60000*m)/1000);
        return (m<10?"0"+m:m)+":"+(s<10?"0"+s:s);
    }
}
