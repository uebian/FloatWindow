package com.yzrilyzr.myclass;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;
import java.util.regex.*;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.ui.mySimpleAdapter;
import java.io.File;
import java.text.SimpleDateFormat;

public class myFileExplorer implements OnItemClickListener,OnItemLongClickListener
		{
				private String curPath="",PARENT="上级目录…";
				private Context ctx;
				private SimpleAdapter SimpleAdapter;
				private int pos=0;
				private ListView ListView;
				private myInterface myInterface;
				String[] scroll=new String[0];
				public static final String[] EXT=new String[]{
								//"null",
								"chm",
								"ebook",
								"xls|xlsx",
								//"other",
								"apk",
								"cal",
								"con",
								"swf",
								"avi|mp4|3gp|m3u|m4a|m4b|m4p|m4u|m4v|mov|mpe|mpeg|mpg|mpg4|mpga|rmvb",
								"mp3|aac|wav|mid|ogg|mp2|wma|wmv",
								"png|jpg|jpeg|gif|bmp",
								"xv",
								"html|mht|htm",
								"pdf",
								"ppt|pptx|pps",
								"txt|java|js|xml|conf|log|prop|rc|sh",
								"doc|docx|wps",
								"zip|7z|rar|gtar|gz|tar|jar|tgz"
						};
				public String getCurrentPath()
						{
								return curPath;
						}
				public static final int[] RES=new int[]{
								//R.drawable.chess_manual,
								R.drawable.chm,
								R.drawable.ebook,
								R.drawable.excel,
								//R.drawable.file,
								//R.drawable.folder,
								//R.drawable.format_app,
								R.drawable.format_app_holder,
								R.drawable.format_calendar,
								R.drawable.format_contacts,
								R.drawable.format_flash,
								//R.drawable.format_img_broken,
								R.drawable.format_media,
								R.drawable.format_music,
								R.drawable.format_picture,
								R.drawable.format_xv,
								R.drawable.html,
								R.drawable.pdf,
								R.drawable.ppt,
								R.drawable.text,
								R.drawable.word,
								R.drawable.zip_icon
						};

				public myFileExplorer(Context ct)
						{
								ctx = ct;
								getFileList(ListView, Environment.getExternalStorageDirectory().getAbsolutePath());
								//listview.setOnItemClickListener(this);
								//listview.setLayoutParams(new LinearLayout.LayoutParams(-1,tool.dip2px(ctx,height)));
						}
				public boolean goBack()
						{
								if (curPath.equals("/") || curPath.equals(""))return false;
								getFileList(ListView, new File(curPath).getParent());
								return true;
						}

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
						{
								// TODO: Implement this method
								try
										{
												File c=new File(curPath);
												File f=new File(c.getAbsolutePath() + "/" + ((TextView)p2.findViewById(R.id.fileentryTextView1)).getText());
												if (myInterface != null)
														{
																if (f.isFile())
																		{myInterface.openFile(f, p3);}
																else if (f.isDirectory())
																		{getFileList(ListView, f.getAbsolutePath());myInterface.openDir(f, p3);}
																else
																		{getFileList(ListView, c.getParent());}
														}
										}
								catch (Exception e)
										{util.check(ctx, e);}
						}


				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
						{
								try
										{
												File c=new File(curPath);
												File f=new File(c.getAbsolutePath() + "/" + ((TextView)p2.findViewById(R.id.fileentryTextView1)).getText());
												if (myInterface != null)
														{
																myInterface.longClick(f, p3);
														}
										}
								catch (Exception e)
										{util.check(ctx, e);}
								return true;
						}
				/*
				 public void pos(int type){
				 if(type==0)pos=listview.getFirstVisiblePosition();
				 if(type==1)listview.setSelection(pos);
				 }*/
				public String getFileTime(String f)
						{
								SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss ");
								return sdf.format(new File(f).lastModified());
						}
				public String getFilesize(String f)
						{
								double size=new File(f).length();
								String[] units=new String[]{"B","K","M","G","T","P"};
								double mod=1024.0;int i=0;
								for (i = 0;size >= mod;i++)
										{size /= mod;}
								return Math.round(size * 100.0) / 100.0 + units[i];
						}
				private int getMime(String filenl)
						{
								//filenl=filenl.replace(curPath,"");
								filenl = getExtensionName(filenl);
								int i=0;
								for (String s:EXT)
										{
												Matcher m=Pattern.compile(s).matcher(filenl.toLowerCase());
												if (m.matches())return RES[i];
												i++;
										}
								return R.drawable.file;
						}
				public static String getExtensionName(String filename)
						{ 
								if ((filename != null) && (filename.length() > 0))
										{ 
												int dot = filename.lastIndexOf('.'); 
												if ((dot > -1) && (dot < (filename.length() - 1)))
														{ 
																return filename.substring(dot + 1); 
														} 
										} 
								return ""; 
						} 

				public void getFileList(ListView lv, String str)
						{
								curPath = str;
								ListView = lv;
								int a=0,b=0;
								File[] files=new File(str).listFiles();
								for (File f:files)
										{
												if (f.isFile())a++;
												if (f.isDirectory())b++;
										}
								String[] f2=new String[a],d2=new String[b];
								a = 0;b = 0;
								for (File f:files)
										{
												if (f.isFile())
														{f2[a++] = f.toString();}
												if (f.isDirectory())
														{d2[b++] = f.toString();}
										}
								Arrays.sort(f2, String.CASE_INSENSITIVE_ORDER);
								Arrays.sort(d2, String.CASE_INSENSITIVE_ORDER);

								List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
								final String IMAGE="P",FILENAME="F",INFO="I";
								Map<String,Object> map=new HashMap<String,Object>();
								map.put(IMAGE, R.drawable.folder);
								map.put(FILENAME, PARENT);
								map.put(INFO, "");
								list.add(map);

								for (String s:d2)
										{
												map = new HashMap<String,Object>();
												map.put(IMAGE, R.drawable.folder);
												map.put(FILENAME, new File(s).getName());
												map.put(INFO, getFileTime(s) + getFilesize(s));
												list.add(map);
										}
								for (String s:f2)
										{
												map = new HashMap<String,Object>();
												map.put(IMAGE, getMime(s));
												map.put(FILENAME, new File(s).getName());
												map.put(INFO, getFileTime(s) + getFilesize(s));
												list.add(map);

										}
								SimpleAdapter = new mySimpleAdapter(ctx, list, R.layout.layout_entry, 
																										new String[]{IMAGE,FILENAME,INFO}, 
																										new int[]{R.id.fileentryImageView1,R.id.fileentryTextView1,R.id.fileentryTextView2}
																										);
								if (lv != null)setListView(lv);
						}
				/*private class icons{
				 public static final int chess_manual=0x7f020002;
				 public static final int chm=0x7f020003;
				 public static final int ebook=0x7f020004;
				 public static final int excel=0x7f020005;
				 public static final int file=0x7f020006;
				 public static final int folder=0x7f020007;
				 public static final int format_app=0x7f020008;
				 public static final int format_app_holder=0x7f020009;
				 public static final int format_calendar=0x7f02000a;
				 public static final int format_contacts=0x7f02000b;
				 public static final int format_flash=0x7f02000c;
				 public static final int format_img_broken=0x7f02000d;
				 public static final int format_media=0x7f02000e;
				 public static final int format_music=0x7f02000f;
				 public static final int format_picture=0x7f020010;
				 public static final int format_xv=0x7f020011;
				 public static final int html=0x7f020012;
				 public static final int pdf=0x7f02001b;
				 public static final int ppt=0x7f02001f;
				 public static final int text=0x7f020023;
				 public static final int word=0x7f020024;
				 public static final int zip_icon=0x7f020025;

				 }*/
				public void setListView(ListView p1)
						{
								ListView = p1;
								p1.setOnItemClickListener(this);
								p1.setOnItemLongClickListener(this);
								p1.setAdapter(SimpleAdapter);
						}
				public void setInterface(myInterface m)
						{myInterface = m;}
				public interface myInterface
						{
								public abstract void openFile(File f, int i);
								public abstract void openDir(File f, int i);
								public abstract void longClick(File f, int i);
								//public abstract void receiveList(ListAdapter p1);
						}
		}
