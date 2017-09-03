package com.yzrilyzr.FloatWindow;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.yzrilyzr.FloatWindow.API_APP.*;
import com.yzrilyzr.ui.*;
import java.io.*;
import java.util.*;

import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import com.yzrilyzr.FloatWindow.view.StarterView;
import com.yzrilyzr.myclass.util;
import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import com.yzrilyzr.FloatWindow.view.LongTextView;
public class MainService extends Service
{
    private Context ctx=this;
    private boolean created=false;
    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO: Implement this method
        Intent inte =new Intent(this,BroadcastReceiver.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,0,inte,0);
        Notification.Builder builder1 = new Notification.Builder(this)
            .setSmallIcon(R.drawable.icon) //设置图标
            .setTicker("FloatWindow主服务已开始运行")
            .setContentTitle("FloatWindow主服务正在运行")//设置标题
            .setContentText("点击这里进行设置")//消息内容
            .setSound(null)
            .setVibrate(null)
            .setAutoCancel(false)//打开程序后图标消失
            .setContentIntent(pendingIntent)
            .setLights(uidata.UI_COLOR_MAIN,500,2000);
        Notification notification1 = builder1.build();
        startForeground(1,notification1);
        if(intent!=null)
        {
            String st=intent.getStringExtra("IData");
            if(st==null)st="";
            if(st.equals("start"))start();
            if(st.equals("settings"))showMenu();
            if(st.equals("pluginStart"))showStarterButton();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy()
    {
        // TODO: Implement this method
        super.onDestroy();
        stopForeground(false);
    }
    private void showStarterButton()
    {
        if(created)return;
        created=true;
        final int dd=util.dip2px(50);
        final Window x=new Window(ctx,dd,dd).show();
        final Window xxx=new Window(ctx,-1,-1);
        LinearLayout a =(LinearLayout)x.getMainView();
        a.setBackground(null);
        a.removeAllViews();
        ImageView i=new ImageView(ctx);
        i.setImageResource(R.drawable.main_icon);
        final StarterView cv=new StarterView(ctx);
        LinearLayout aa=(LinearLayout) xxx.getMainView();
        aa.setOnTouchListener(null);
        aa.setBackground(null);
        aa.removeAllViews();
        aa.addView(cv);
        cv.setListener(new StarterView.listener(){
                int code=-1;
                @Override
                public void onItemClick(int which)
                {
                    // TODO: Implement this method
                    code=which;
                }
                @Override
                public void onAnimEnd()
                {
                    // TODO: Implement this method
                    xxx.dismiss();
                    if(code==3)
                    {
                        showMenu();
                    }
                    if(code==4)
                    {
                        stopSelf();
                        stopService(new Intent(ctx,PluginService.class));
                        System.exit(0);
                    }
                    if(code==5)
                    {
                        showStartApp();
                    }
                }
                @Override
                public void onAnimStart()
                {
                    // TODO: Implement this method
                    code=-1;
                }
            });
        i.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View p1, MotionEvent p2)
                {
                    // TODO: Implement this method
                    cv.setPosition(p2.getRawX(),p2.getRawY());
                    x.moveableView(p1,p2);
                    return false;
                }
            });
        i.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    xxx.show();
                    cv.toggle();
                }
            });
        a.addView(i);
    }

    @Override
    public void onCreate()
    {
        // TODO: Implement this method
        super.onCreate();
        if(created)return;
        uidata.readData(this);
        uidata.initIcon(this);

    }

    private void start()
    {
        if(created)return;
        //■■■■■■■■■■■■■■■■■■■■■■■■■■■

        //●●●●●●●●●●●●●●●●●●●●●●●●●●●
        int wid,hei;
        final Window w=new Window(ctx,wid=util.dip2px(210),hei=util.dip2px(260));
        w.setPosition((uidata.SCREEN_WIDTH-wid)/2,(uidata.SCREEN_HEIGHT-hei)/2);
        w.show()
            .setCanResize(false);
        LinearLayout v=(LinearLayout) w.getMainView();
        v.removeAllViews();
        v.setOnTouchListener(null);
        v.setOrientation(1);
        v.setGravity(Gravity.CENTER);
        v.setBackground(new myRippleDrawable(uidata.UI_COLOR_BACK,uidata.UI_COLOR_BACK,uidata.UI_RADIUS));
        myRoundDrawable rr=new myRoundDrawable(ctx,R.drawable.launch);
        ImageView iv=new ImageView(ctx);
        int ii=util.dip2px(180);
        iv.setImageDrawable(rr);
        iv.setLayoutParams(new LinearLayout.LayoutParams(ii,ii));
        v.addView(iv);
        myTextViewTitleBack tv=new myTextViewTitleBack(ctx);
        tv.setText("FloatWindow");
        tv.setGravity(Gravity.CENTER);
        v.addView(tv);
        myLoadingView ml=new myLoadingView(ctx);
        ii=util.dip2px(40);
        ml.setLayoutParams(new LinearLayout.LayoutParams(ii,ii));
        v.addView(ml);
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run()
                {
                    // TODO: Implement this method
                    w.dismiss();
                    showStarterButton();
                }
            },2000);
    }
    private void showMenu()
    {
        final Window w=new Window(ctx,-2,-2);
        //ViewGroup bar=(ViewGroup) w.getMainView().getChildAt(1);
        //for(int i=0;i<3;i++)
        //bar.removeViewAt(2);
        w.setBar(8,8,8,0).setIcon(R.drawable.menu_w);
        View v=LayoutInflater.from(ctx).inflate(R.layout.window_menu,null);
        OnClickListener ocl=new OnClickListener(){
            @Override
            public void onClick(View p1)
            {
                // TODO: Implement this method
                switch(p1.getId())
                {
                    case R.id.windowcontrolpanelmyLinearLayoutRound1:
                        showSystemStatus();
                        break;
                    case R.id.windowcontrolpanelmyLinearLayoutRound2:
                        showAPIsearch();
                        break;
                    case R.id.windowcontrolpanelmyLinearLayoutRound3:
                        showSystemStatus();
                        break;
                    case R.id.windowcontrolpanelmyLinearLayoutRound4:
                        showHelp();
                        break;
                    case R.id.windowcontrolpanelmyLinearLayoutRound5:
                        showAbout();
                        break;
                    case R.id.windowcontrolpanelmyLinearLayoutRound6:
                        stopSelf();
                        stopService(new Intent(ctx,PluginService.class));
                        System.exit(0);
                        break;
                }
            }
        };
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound1).setOnClickListener(ocl);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound2).setOnClickListener(ocl);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound3).setOnClickListener(ocl);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound4).setOnClickListener(ocl);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound5).setOnClickListener(ocl);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound6).setOnClickListener(ocl);

        
        w.addView(v)
            .setTitle("菜单")
            .setCanResize(false)
            .show();
    }

    private void showSystemStatus()
    {
        Window w=new Window(ctx,-2,-2)
            .setTitle("程序状态")
            .setBar(8,0,8,0)
            .setIcon(R.drawable.systeminfo)
            .setCanResize(false)
            .show();
        ScrollView sv=new ScrollView(ctx);
        final myTextViewBack m=new myTextViewBack(ctx);
        sv.setLayoutParams(new LinearLayout.LayoutParams(-1,util.dip2px(240)));
        sv.addView(m);
        final myProgressBar mpb=new myProgressBar(ctx);
        mpb.setMax((int)(Runtime.getRuntime().maxMemory()/1048576));
        m.setText("载入中…");
        w.addView(sv);
        w.addView(mpb);
        final Handler h=new Handler();
        final boolean[] b=new boolean[]{true};
        final OutputStream sw=new ByteArrayOutputStream();
        PrintStream pw=new PrintStream(sw);
        System.setOut(pw);
        h.postDelayed(new Runnable(){
                @Override
                public void run()
                {
                    // TODO: Implement this method
                    Runtime run = Runtime.getRuntime();
                    long max = run.maxMemory();
                    long total = run.totalMemory();
                    long free = run.freeMemory();
                    long usable = max - total + free;
                    String a= "虚拟机最大内存:"+ max/1048576+"MB\n"+
                        "虚拟机可用内存:"+ usable/1048576+"MB\n"+
                        "已分配内存:" + total/1048576+"MB\n"+
                        "已分配内存中的剩余空间:"+ free/1048576+"MB\n"+
                        "可用的CPU个数:"+run.availableProcessors()+"个\n"+
                        "已载入的插件("+PluginService.pluginObject.size()+"个):\n";
                    for(Object o:PluginService.pluginObject)
                        a+=o.getClass().getName()+"\n";
                    a+="程序输出:\n";
                    a+=sw.toString()+"\n";
                    m.setText(a);
                    mpb.setSecondaryProgress((int)(total/1048576));
                    mpb.setProgress((int)((total-free)/1048576));
                    if(b[0])h.postDelayed(this,1000);
                }
            },1000);
        w.setInterface(new WindowInterface(){
                @Override
                public void onButtonDown(int code)
                {
                    // TODO: Implement this method
                    if(code==ButtonCode.CLOSE)b[0]=false;
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
        myButton mm=new myButton(ctx);
        mm.setText("清理内存");
        w.addView(mm);
        mm.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    System.gc();
                }
            });
    }
    private void showAPIsearch()
    {
        Window w=new Window(ctx,-2,-2)
            .setTitle("API查询")
            .setIcon(R.drawable.help)
            .setBar(0,0,8,0)
            .show();
        myTextViewBack m=new myTextViewBack(ctx);
        m.setText("请输入完整的Java包名和类名，内部类用$连接");
        w.addView(m);
        final myEditText met=new myEditText(ctx);
        myButton mb=new myButton(ctx);
        w.addView(met);
        w.addView(mb);
        met.setText("com.yzrilyzr.FloatWindow.API_APP");
        mb.setText("查询");
        mb.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    try
                    {
                        String s=met.getText().toString();
                        Class.forName(s);
                        showAPIClassInfoMenu(s);
                    }
                    catch(ClassNotFoundException e)
                    {
                        util.toast(ctx,"找不到该类");
                    }
                }
            });

    }
    private void showAPIClassInfo(String clazz) throws ClassNotFoundException{
        Window w=new Window(ctx,-2,-2)
        //.setBar(8,0,0,0)
        .setTitle("类 信息")
        .setIcon(R.drawable.objects)
        .show();
        Class cl=Class.forName(clazz);
        myTextViewBack mtv=new myTextViewBack(ctx);
        String a="类型:"+cl.toString();
        a=append(a,"继承关系:");
        Class parent=cl.getSuperclass();
        a=append(a,""+parent);
        while(parent!=Object.class){
            parent=parent.getSuperclass();
            a=append(a,""+parent);
        }
        Object[] ts=cl.getInterfaces();
        a=append(a,"实现的接口:");
        for(Object t:ts){
            a=append(a,t.toString());
        }
        Object[] cs=cl.getClasses();
        a=append(a,"内部类:");
        for(Object t:cs){
            a=append(a,t.toString());
        }
        mtv.setText(a);
        w.addView(mtv);

        
    }
    private void showAPIClassSrcCode(String clazz){
        try
        {
            Class<?> c=Class.forName(clazz);
            LongTextView ltv=new LongTextView(ctx);
            ltv.setText(new ClassSrc(c).get());
            new Window(ctx,-2,-2)
                .setTitle(c.getSimpleName()+".class")
                .setIcon(R.drawable.objects)
                .addView(ltv)
                .show();
        }
        catch (ClassNotFoundException e)
        {}
    }
    private void showAPIClassInfoMenu(final String clazz)
    {
        Window w=new Window(ctx,-2,-2)
            .setTitle("对象类")
            .setIcon(R.drawable.objects)
            .setBar(8,0,8,0)
            .setCanResize(false)
            .show();
        View v=LayoutInflater.from(ctx).inflate(R.layout.window_api_class,null);
        w.addView(v);
        OnClickListener ocl=new OnClickListener(){
            @Override
            public void onClick(View p1)
            {
                // TODO: Implement this method
                try{
                switch(p1.getId()){
                    case R.id.windowapiclassmyLinearLayoutRipple1:
                        showAPIClassInfo(clazz);
                        break;
                }
                }catch(Throwable e){}
            }
        };
        v.findViewById(R.id.windowapiclassmyLinearLayoutRipple1).setOnClickListener(ocl);
        v.findViewById(R.id.windowapiclassmyLinearLayoutRipple2).setOnClickListener(ocl);
        v.findViewById(R.id.windowapiclassmyLinearLayoutRipple3).setOnClickListener(ocl);
        v.findViewById(R.id.windowapiclassmyLinearLayoutRipple4).setOnClickListener(ocl);
        myButton m=new myButton(ctx);
        m.setText("查看源码");
        m.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    showAPIClassSrcCode(clazz);
                }
            });
        w.addView(m);
        /*
         BufferedReader bb=new BufferedReader(new InputStreamReader(System.in));
         //  String classname=bb.readLine();
         Class c=Class.forName("ArrayListTest.java");
         //-------------------获取方法的详细信息
         Method m[]=c.getDeclaredMethods();
         for(int i=0;i<m.length;i++)
         {
         //--------------------获得方法的名字
         System.out.println("方法的名字是:"+m[i].getName());
         //--------------------获得方法参数的类型和有几个参数
         Class b[]=m[i].getParameterTypes();//获得所有的参数并且存放到数组B中
         for(int j=0;j<b.length;j++)
         {
         System.out.println("参数的类型是"+b[j]);
         }
         //--------------------获得方法返回值的类型
         System.out.println(m[i].getReturnType());//获得方法的返回值类型
         //--------------------获得方法的修饰符
         int mod=m[i].getModifiers();
         System.out.println("方法的修饰符有"+Modifier.toString(mod));
         //--------------------获得方法的异常类型
         Class e[]=m[i].getExceptionTypes();//获得所有的异常类型存放到数组e中
         for(int k=0;k<e.length;k++)
         {
         System.out.println("方法的异常类型是："+e[k]);
         }
         System.out.println("-------------------------------------------------------------------");
         }
         //----------------------------获得属性的详细信息


         }

         }
         package Class     //测试.com.tc.test.Class的forname方法获得属性信息;

         import java.lang.reflect.Field;
         import java.lang.reflect.Modifier;

         public class test {

         public static void main(String[] args) throws ClassNotFoundException {
         Class c=Class.forName("Class测试.com.tc.test.Class的forname方法获得属性信息.student");//把要使用的类加载到内存中,并且把有关这个类的所有信息都存放到对象c中
         Field f[]=c.getDeclaredFields();//把属性的信息提取出来，并且存放到field类的对象中，因为每个field的对象只能存放一个属性的信息所以要用数组去接收
         for(int i=0;i<f.length;i++)
         {
         System.out.println("属性的名称是:"+f[i].getName());//获得属性的名字
         System.out.println("属性的类型是:"+f[i].getType());//获得属性的类型
         int mod=f[i].getModifiers();//获得修饰符的常量总和
         System.out.println(mod);
         System.out.println("属性的修饰符有:"+Modifier.toString(mod));//modifier类可以根据常量总和去计算到底有哪些修饰符
         System.out.println("-------------------------------------------------------");
         }

         }

         }
        */
        /*, v.setOnClickListener(new OnClickListener(){
         @Override
         public void onClick(View p1)
         {
         // TODO: Implement this method
         try{
         Class clazz=Class.forName(ed.getText().toString());
         Method[] ms=clazz.getDeclaredMethods();
         Field[] fs=clazz.getDeclaredFields();
         Constructor[] cos=clazz.getDeclaredConstructors();
         Annotation[] as= clazz.getDeclaredAnnotations();
         Class[] cs=clazz.getDeclaredClasses();
         final List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();
         for(Field f:fs){
         Map<String,Object> map=new HashMap<String,Object>();

         map.put("name",f.getName());
         map.put("icon",R.drawable.box_blue);
         map.put("info",Modifier.toString(f.getModifiers()));
         l.add(map);
         }
         for(Method f:ms){
         Map<String,Object> map=new HashMap<String,Object>();

         map.put("name",f.getName()+":"+f.getGenericReturnType());
         map.put("icon",R.drawable.box_red);
         map.put("info",Modifier.toString(f.getModifiers()));
         l.add(map);
         }
         for(Class f:cs){
         Map<String,Object> map=new HashMap<String,Object>();

         map.put("name",f.getName());
         map.put("icon",R.drawable.objects);
         map.put("info",Modifier.toString(f.getModifiers()));
         l.add(map);
         }
         SimpleAdapter ad=new SimpleAdapter(ctx,l,R.layout.window_applist_entry,new String[]{"icon","name","info"},new int[]{R.id.windowapplistentryImageView1,R.id.windowapplistentryTextView1,R.id.windowapplistentryTextView2});
         li.setAdapter(ad);
         }
         catch(Throwable e){

         }
         }
         });*/

    }
    private String append(String a,String s){
        return a+"\n"+s;
    }
    private void showHelp()
    {
        new Window(ctx,-2,-2)
            .setTitle("帮助")
            .setBar(8,8,8,0)
            .setIcon(R.drawable.help)
            .setCanResize(false)
            .setMessage("无内容")
            .show();

    }
    private void showAbout()
    {
        new Window(ctx,-2,-2)
            .setTitle("关于")
            .setBar(8,8,8,0)
            .setIcon(R.drawable.info)
            .setCanResize(false)
            .setMessage("FloatWindow 悬浮窗\n支持载入插件 多窗口 多任务\n可更换主题颜色 字体 组件样式\n具有更高的安全性\n提供开放的插件API\n…\n\n使用未知插件带来的问题\n由插件作者负责\n本程序作者不承担任何责任\n\n如有问题，请联系作者\n作者:yzrilyzr(QQ1303895279)")
            .show();
    }
    private void showStartApp()
    {
        util.toast(ctx,"加载程序列表中…");
        final Window w=new Window(ctx,-2,util.dip2px(480))
            .setTitle("启动程序")
            .setIcon(R.drawable.start)
            .setBar(0,0,0,0);
        final List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();

        PackageManager mPackageManager = ctx.getPackageManager();
        List<ApplicationInfo> installedAppList =mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : installedAppList)
        {
            boolean flag = false;
            if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                flag = true;
            }
            else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                flag = true;
            }
            if (flag)
            {
                Map<String,Object> m=new HashMap<String,Object>();
                m.put("icon",appInfo.loadIcon(mPackageManager));
                m.put("name",""+appInfo.loadLabel(mPackageManager).toString());
                String info="";
                try
                {
                    info+=mPackageManager.getPackageInfo(appInfo.packageName, 0).versionName;
                    //newInfo.versionCode =String.valueOf(mPackageManager.getPackageInfo(newInfo.packageName, 0).versionCode);
                }
                catch(Throwable e)
                {}
                m.put("pkg",appInfo.packageName);
                m.put("info",info);
                l.add(m);
            }
        }
        SimpleAdapter ad=new SimpleAdapter(ctx,l,R.layout.window_applist_entry,new String[]{"icon","name","info"},new int[]{R.id.windowapplistentryImageView1,R.id.windowapplistentryTextView1,R.id.windowapplistentryTextView2}){ 
            class ViewHolder
            {
                TextView text1,text2;
                ImageView icon;
			}
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ViewHolder holder;
                if(convertView==null)
                {
                    convertView=LayoutInflater.from(ctx).inflate(R.layout.window_applist_entry,parent,false);
                    holder=new ViewHolder();
                    holder.text1 = (TextView) convertView.findViewById(R.id.windowapplistentryTextView1);
                    holder.text2 = (TextView) convertView.findViewById(R.id.windowapplistentryTextView2);
                    holder.icon = (ImageView) convertView.findViewById(R.id.windowapplistentryImageView1);
                    convertView.setTag(holder);
                }
                else holder=(ViewHolder) convertView.getTag();
                Map m=l.get(position);
                holder.text1.setText(""+m.get("name"));
                holder.text2.setText(""+m.get("info"));
                Object o=m.get("icon");
                if(o!=null)
                    holder.icon.setImageDrawable((Drawable)o);
                return convertView;
			}
        };
        ListView mlv=new myListView(ctx);
        mlv.setAdapter(ad);
        w.addView(mlv);
        w.show();
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
                {
                    // TODO: Implement this method
                    util.runAppByPkgName(ctx,(String)l.get(p3).get("pkg"));
                }
            });
    }


}
