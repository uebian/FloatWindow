package com.yzrilyzr.floatingwindow.api;
import android.content.*;
import android.content.pm.*;
import android.view.*;
import java.lang.reflect.*;
import java.util.zip.*;

import android.content.res.Resources;
import java.io.InputStream;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import com.yzrilyzr.floatingwindow.IData;

public class API
{
    public static final String WINDOW_CLASS="com.yzrilyzr.floatingwindow.Window";
    public static final int API_VERSION=1;
	public static final float density=Resources.getSystem().getDisplayMetrics().density;
    public static void startMainService(Context ctx,String targetClass)
    {
		Intent intent=new Intent();
        intent.setAction("com.yzrilyzr.Service");
        intent.setPackage("com.yzrilyzr.floatingwindow");
        intent.putExtra("pkg",ctx.getPackageName());
        intent.putExtra("class",targetClass);
        ctx.startService(intent);
    }
    public static void startMainActivity(Context ctx,String targetClass)
    {
		Intent intent=new Intent();
        intent.setAction("com.yzrilyzr.Service");
        intent.setPackage("com.yzrilyzr.floatingwindow");
        intent.putExtra("pkg",ctx.getPackageName());
        intent.putExtra("class",targetClass);
		intent.putExtra(IData.TAG,IData.ACTIVITY);
        ctx.startService(intent);
    }
    public static InputStream getPkgFile(Context ctx,String pkgName,String file)throws Throwable
    {
        String apk=ctx.getPackageManager().getPackageInfo(pkgName,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.publicSourceDir;
		ZipFile f=new ZipFile(apk);
		ZipEntry en=f.getEntry(file);
		return f.getInputStream(en);
    }
    public static void exPkgFile(Context ctx,String pkgName,String file,String to) throws Throwable
    {
        String apk=ctx.getPackageManager().getPackageInfo(pkgName,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.publicSourceDir;
		ZipFile f=new ZipFile(apk);
		ZipEntry en=f.getEntry(file);
        InputStream i=f.getInputStream(en);
		BufferedOutputStream o=new BufferedOutputStream(new FileOutputStream(to));
		byte[] b=new byte[2048];
		int p=0;
		while((p=i.read(b))!=-1)o.write(b,0,p);
		i.close();
		o.close();
		f.close();
    }
	public static View parseXmlViewFromFile(Context ctx,String pkgName,String file) 
    {
        try
        {
            InputStream is = getPkgFile(ctx,pkgName,file);
            byte[] data=new byte[is.available()];
            is.read(data);
			is.close();
            Class<?> clazz = Class.forName("android.content.res.XmlBlock");
            Constructor<?> constructor = clazz.getDeclaredConstructor(byte[].class);
            constructor.setAccessible(true);
            Object block = constructor.newInstance(data);
            Method method = clazz.getDeclaredMethod("newParser");
            method.setAccessible(true);
            XmlPullParser parser = (XmlPullParser) method.invoke(block);
            return LayoutInflater.from(ctx).inflate(parser,null);
        }
        catch(Throwable e)
        {System.out.println(e);}
        return null;
    }
	public static int dip(int pxValue)
    {
        return (int) (pxValue / density + 0.5f);
    }
    public static int px(float dipValue)
    {
        return  (int)(dipValue*density+0.5f);
    }
}
