package com.yzrilyzr.uidemo;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.yzrilyzr.myclass.*;
import com.yzrilyzr.ui.*;
import java.io.*;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.View.OnLongClickListener;
import com.yzrilyzr.FloatWindow.R;
import java.util.ArrayList;

public class musicplayer extends myActivity
		{/*
				static myMusicPlayer mmp;
				static mySeekBar msb;
				static String path;
				static myImageButton e;
				static boolean isDown=false,isCreated=false;
				static myFileExplorer mfe;
				static myListView list1,list2;
				static TextView aa,ss,dd;
				static Context ctx;
				static myViewPager page;
				static ImageView album;
				static ArrayList<View> pages;
				static myMusicPlayer.music musicinfo;
				static Bitmap albumbitmap;
				@Override

				protected void onCreate(Bundle savedInstanceState)
						{
								// TODO: Implement this method
								ctx=this;
								super.onCreate(savedInstanceState);
								try
										{
												setContentView(R.layout.demo_musicplayer);
												if(!isCreated)
														{init();}
												//init();
												init2();
												init3();
												try
														{
																//mmp.refreshMusicInfo(this);
																mmp.callOnMediaChange();
														}
												catch(Exception e)
														{}
												isCreated=true;
										}
								catch(Exception e)
										{util.check(this,e);}
						}
				private void init3()
						{
								mfe.getFileList(list1,path);
								//	mfe.setListView(list);
								mfe.setInterface(new file());
								msb.setOnChangeListener(new seek());
								mmp.setInterface(new music());
								pages=new ArrayList<View>();
								pages.add(album);
								pages.add(list1);
								pages.add(list2);
								album.setOnLongClickListener(new OnLongClickListener(){

														@Override
														public boolean onLongClick(View p1)
																{
																		// TODO: Implement this method
																		new myAlertDialog(musicplayer.this)
																				.setPositiveButton("保存图片",new myDialogInterface(){
																								public void click(View p1,int p3)
																										{
																												try
																														{
																																String filePath="/sdcard/yzr的app/album.png";
																																albumbitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(filePath));
																																MediaScannerConnection.scanFile(musicplayer.this,
																																		new String[] { filePath }, null,
																																		new MediaScannerConnection.OnScanCompletedListener() {
																																						@Override
																																						public void onScanCompleted(String path, Uri uri)
																																								{
																																										//Log.v("MediaScanWork", "file " + path
																																										//	+ " was scanned seccessfully: " + uri);
																																								}
																																				});
																														}
																												catch(Exception e)
																														{}
																										}
																						})
																				.show();
																		return true;
																}
												});
								//page.setAdapter(new pagerAdapter());
								page.setPages(album,list1,list2);
						}
				private void init()
						{
								mfe=new myFileExplorer(this);
								path=Environment.getExternalStorageDirectory().getAbsolutePath();	
								mmp=new myMusicPlayer();
								//mmp.init(this);
								//mmp.queryMusics(this);
						}

				private void init2()
						{
								e=(myImageButton)((LinearLayout)findViewById(R.id.demomusicplayerLinearLayout1)).getChildAt(2);
								//mfe=new myFileExplorer(this);
								aa=(TextView)findViewById(R.id.demomusicplayerTextView1);//.setText(name);
								ss=(TextView)findViewById(R.id.demomusicplayerTextView5);//setText(p4);
								dd=(TextView)findViewById(R.id.demomusicplayerTextView4);
								list1=new myListView(this);//(ListView)findViewById(R.id.demomusicplayerListView1);//.setText("00:00");
								list2=new myListView(this);	
								album=new ImageView(this);
								album.setImageResource(R.drawable.format_music);
								page=(myViewPager)findViewById(R.id.demomusicplayerViewPager1);	
								//file=new file();
								//path="/sdcard/柚子人/音乐/文件夹";
								msb=(mySeekBar)findViewById(R.id.demomusicplayerSeekBar1);
								//seek=new seek();	
								//mmp=new myMusicPlayer();
								//music=new music();
						}
				class seek implements mySeekBar.OnChange
						{
								@Override public void onChange(mySeekBar msb,int p)
										{dd.setText(mmp.parseTime(p));}
								@Override public void onDown(mySeekBar msb)
										{isDown=true;}
								@Override public void onUp(mySeekBar msb)
										{mmp.seekTo(msb.getProgress());isDown=false;}

						}
				class file implements myFileExplorer.myInterface
						{
								@Override
								public void openFile(File f,int i)
										{
												// TODO: Implement this method
												mmp.setPath(f.getParent());
												mmp.setCurrentMusicByFilter(f);
												mmp.play();
												mmp.getFileList(list2);
												//list.setSelection(mmp.curMusic);
										}

								@Override
								public void openDir(File f,int i)
										{
												// TODO: Implement this method
												path=f.getAbsolutePath();
										}

								@Override
								public void longClick(File f,int i)
										{
												// TODO: Implement this method
												String a="";
												a+=musicinfo.TALB;
												a+=musicinfo.TIT2;
												//a+=musicinfo.TIT3;
												a+=musicinfo.TPE1;
												a+=musicinfo.TPE2;
												//a+=musicinfo.TPE3;
												//a+=musicinfo.TPE4;
												a+=musicinfo.COMM;
												//a+=musicinfo;
												util.alert(musicplayer.this,a);
										}

						}	/*	
				 class pagerAdapter extends PagerAdapter{

				 @Override
				 public boolean isViewFromObject(View arg0, Object arg1) {
				 // TODO Auto-generated method stub
				 return arg0 == arg1;
				 }

				 @Override
				 public int getCount() {
				 // TODO Auto-generated method stub
				 return pages.size();
				 }

				 @Override
				 public void destroyItem(ViewGroup container, int position,
				 Object object) {
				 // TODO Auto-generated method stub
				 container.removeView(pages.get(position));
				 }

				 @Override
				 public Object instantiateItem(ViewGroup container, int position) {
				 // TODO Auto-generated method stub
				 container.addView(pages.get(position));


				 return pages.get(position);
				 }
				 };

				 
				class music implements myMusicPlayer.myInterface
						{				
								@Override public void onCompletion(myMusicPlayer p1)
										{
												e.setImageResource(p1.isPlaying()?R.drawable.pause:R.drawable.play);
										}
								@Override public void refresh(myMusicPlayer p1,int p2,String p3)
										{
												if(!isDown)
														{msb.setProgress(p2);
																dd.setText(p3);
														}
										}
								@Override public void onMediaChange(myMusicPlayer p1,myMusicPlayer.music s)
										{
												msb.setMax((int)p1.getDuration());
												aa.setText((String)s.TIT2);
												ss.setText(mmp.parseTime(p1.getDuration()));
												dd.setText("00:00");
												musicinfo=s;
												//ool.toast(musicplayer.this,f);
												byte[] b=(byte[]) s.APIC;
												if(b!=null)
														{
																albumbitmap=BitmapFactory.decodeByteArray(b,0,b.length);
																album.setImageBitmap(albumbitmap);
														}
												//Cursor cursor =musicplayer.this. query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
												e.setImageResource(p1.isPlaying()?R.drawable.pause:R.drawable.play);
												//mmp.getFileList(list2);
										}
						}
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event)
						{
								// TODO: Implement this method
								if(keyCode==KeyEvent.KEYCODE_BACK&&page.getCurrentItem()==1)
										{
												try
														{
																mfe.setListView(list1);
																if(mfe.goBack())return true;
														}
												catch(Exception e)
														{util.check(this,e);}
										}
								return super.onKeyDown(keyCode, event);
						}

				@Override
				protected void onDestroy()
						{
								// TODO: Implement this method
								super.onDestroy();
								try
										{sendnotify();}
								catch(Exception e)
										{}
						}

				public void sendnotify()
						{
								Notification.Builder builder1 = new Notification.Builder(this);
								builder1.setSmallIcon(R.drawable.icon); //设置图标
								builder1.setTicker("正在后台运行"); 
								builder1.setContentTitle((String)musicinfo.TIT2); //设置标题
								builder1.setContentText(musicinfo.TPE1+" "+musicinfo.TALB); //消息内容
								builder1.setWhen(System.currentTimeMillis()); //发送时间
								builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
								builder1.setAutoCancel(true);//打开程序后图标消失
								Intent intent =new Intent(this,musicplayer.class);
								PendingIntent pendingIntent =PendingIntent.getActivity(this, 0, intent, 0);
								builder1.setContentIntent(pendingIntent);
								Notification notification1 = builder1.build();
								((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(124,notification1); // 通过通知管理器发送通知			}
						}

				public void ran(View v)
						{((ImageButton)v).setImageResource(mmp.setRandom());}
				public void prev(View v)
						{mmp.prev();}
				public void pause(View v)
						{((ImageButton)v).setImageResource(mmp.playOrPause());}
				public void next(View v)
						{mmp.next();}
				public void mode(View v)
						{((ImageButton)v).setImageResource(mmp.setMode());}
				public void list1(View v)
						{page.setCurrentItem(1,true);}
				public void list2(View v)
						{page.setCurrentItem(2,true);}
				public void list0(View v)
						{page.setCurrentItem(0,true);}*/
		}
