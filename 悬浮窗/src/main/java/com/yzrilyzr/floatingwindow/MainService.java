package com.yzrilyzr.floatingwindow;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.yzrilyzr.ui.uidata;
public class MainService extends Service
{
    
    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        return null;
    }
/*
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
        sv.setLayoutParams(new LinearLayout.LayoutParams(-1,util.px(240)));
        sv.addView(m);
        final myProgressBar mpb=new myProgressBar(ctx);
        mpb.setMax((int)(Runtime.getRuntime().maxMemory()/1048576));
        m.setText("载入中…");
        w.addView(sv);
        w.addView(mpb);
		final StringBuilder msg=new StringBuilder();
        final Handler h=new Handler();
        final boolean[] b=new boolean[]{true};
        final OutputStream sw=new OutputStream(){
			@Override
			public void write(int p1) throws IOException
			{
			}
			public void write(byte[] buffer) throws IOException {
				msg.append(new String(buffer));
			}

			public void write(byte[] buffer, int offset, int count) throws IOException {
				msg.append(new String(buffer,offset,count));
			}
			
		};
        PrintStream pw=new PrintStream(sw);
        System.setOut(pw);
		System.setErr(pw);
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
					m.setText("");
                    m.append(String.format(
					"虚拟机最大内存:%dMB\n虚拟机可用内存:%dMB\n已分配内存:%dMB\n已分配内存中的剩余空间:%dMB\n可用的CPU个数:%d个\n已载入的插件(%d个):\n",
						max/1048576l,usable/1048576l,total/1048576l,
						free/1048576l,run.availableProcessors(),
						PluginService.pluginObject.size()));
                    for(Object o:PluginService.pluginObject){
                        m.append(o.getClass().getName());
						m.append("\n");
						}
                    m.append("程序输出:\n");
					m.append(msg.toString());
                    m.append("\n");
                    mpb.setSecondaryProgress((int)(total/1048576));
                    mpb.setProgress((int)((total-free)/1048576));
                    if(b[0])h.postDelayed(this,1000);
                }
            },100);
        w.setWindowInterface(new WindowInterface(){
                @Override
                public void onButtonDown(int code)
                {
                    // TODO: Implement this method
                    if(code==Window.ButtonCode.CLOSE)b[0]=false;
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
        met.setText("com.yzrilyzr.floatingwindow.api.API");
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
    private void showAPIClassInfo(String clazz) throws ClassNotFoundException
	{
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
        while(parent!=Object.class)
		{
            parent=parent.getSuperclass();
            a=append(a,""+parent);
        }
        Object[] ts=cl.getInterfaces();
        a=append(a,"实现的接口:");
        for(Object t:ts)
		{
            a=append(a,t.toString());
        }
        Object[] cs=cl.getClasses();
        a=append(a,"内部类:");
        for(Object t:cs)
		{
            a=append(a,t.toString());
        }
        mtv.setText(a);
        w.addView(mtv);
    }
    private void showAPIClassSrcCode(String clazz)
	{
        try
        {
            Class<?> c=Class.forName(clazz);
            LongTextView ltv=new LongTextView(ctx);
            ltv.setText(new ClassSrc(c,false).get());
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
                try
				{
					switch(p1.getId())
					{
						case R.id.windowapiclassmyLinearLayoutRipple1:
							showAPIClassInfo(clazz);
							break;
					}
                }
				catch(Throwable e)
				{}
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
         });

    }*//*
    private String append(String a,String s)
	{
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
            .setMessage("FloatingWindow 悬浮窗\n支持载入插件 多窗口 多任务\n可更换主题颜色 字体 组件样式\n具有更高的安全性\n提供开放的插件API\n…\n\n使用未知插件带来的问题\n由插件作者负责\n本程序作者不承担任何责任\n\n如有问题，请联系作者\n作者:yzrilyzr(QQ1303895279)")
            .show();
    }
    private void showStartApp()
    {
        util.toast(ctx,"加载程序列表中…");
        final Window w=new Window(ctx,-2,util.px(480))
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
*/

}
