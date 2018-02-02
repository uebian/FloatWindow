package com.yzrilyzr.uidemo;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import com.yzrilyzr.myclass.*;
import java.io.*;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.View;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.ui.myIconDrawer;
import com.yzrilyzr.ui.myIconDrawer.DrawType;
public class iconView extends myActivity
	{/*
		ImageView iv,ivs;
		EditText tv;
		myIconDrawer mid,mids;
		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				// TODO: Implement this method
				super.onCreate(savedInstanceState);
				try
					{
						setContentView(R.layout.demo_icon);
						iv = (ImageView) findViewById(R.id.demoiconImageView1);
						ivs=(ImageView) findViewById(R.id.demoiconImageView2);
						tv= (EditText) findViewById(R.id.demoiconEditText1);
						mid = new myIconDrawer(600);
						mids=new myIconDrawer(200);
						ivs.setImageBitmap(mids.getBitmap());
						iv.setImageBitmap(mid.getBitmap());
						mid.draw(DrawType.RECT, false, 0, 0, mid.cw, mid.cw);
						mid.draw(DrawType.RECT, false, 0, 0, mid.cw, mid.cw);
					}
				catch(Exception e)
					{util.check(this,e);}
			}
		int iii=0;
		public void load(View v)
			{
				mid.clear();
				String[] str=tv.getText().toString().replace("\n","").replace(" ","").replace("	","").split(";");
				String tmp2="";
				for(String tmp:str)
					{
						tmp2+=tmp+";\n";
					}
				tv.setText(tmp2);
				for(int g=0;g<str.length;g++)
					{
						try
							{
								String sss=str[g];
								if(sss.equals(""))break;
								if(sss.indexOf("draw")!=-1)
									{
										sss=sss.split("m\\.draw")[1].replace(";","").replace("(","").replace(")","");
										String[] s=sss.split(",");
										float[] ps=new float[10];
										boolean f=false;DrawType dt=DrawType.POINT;
										for(int i=0,u=0;i<s.length;i++)
											{
												if(i==0)
													{
														switch(s[i].split("\\.")[1])
															{
																case "ARC":dt=DrawType.ARC;break;
																case "CIRCLE":dt=DrawType.CIRCLE;break;
																case "LINE":dt=DrawType.LINE;break;
																case "PATH":dt=DrawType.PATH;break;
																case "POINT":dt=DrawType.POINT;break;
																case "RECT":dt=DrawType.RECT;break;
																case "ROUNDRECT":dt=DrawType.ROUNDRECT;break;
															}
													}
												else if(i==1)
													{f=s[i].equals("true");}
												else
													{ps[u++]=computeFloat(s[i]);}
											}
										mid.draw(dt,f,ps[0],ps[1],ps[2],ps[3],ps[4],ps[5],ps[6],ps[7],ps[8],ps[9]);
									}
								if(sss.indexOf("line")!=-1){
									String[] p=sss.split("m\\.line")[1].replace(";","").replace("(","").replace(")","").split(",");
									mid.line(computeFloat(p[0]),computeFloat(p[1]));
								}
								if(sss.indexOf("move")!=-1){
										String[] p=sss.split("m\\.move")[1].replace(";","").replace("(","").replace(")","").split(",");
										mid.move(computeFloat(p[0]),computeFloat(p[1]));
									}
								
							}
						catch(Exception e)
							{util.toast(this,"错误"+e+"\n(at #"+(g+1)+")");
							}
					}
				mid.setLine();
				iv.setImageBitmap(mid.getBitmap());
			}
		public void ccc(View v)
			{
				mid.clear();mids.clear();			 
				draw(mid);draw(mids);
				mid.setLine();
				iv.setImageBitmap(mid.getBitmap());
				ivs.setImageBitmap(mids.getBitmap());
				iii++;
			}
		private float computeFloat(String s)
			{
				s=s
					.replace("m.ww",mid.ww+"")
					.replace("m.hw",mid.hw+"")
					.replace("m.cw",mid.cw+"")
					.replace("m.qw",mid.qw+"");
				if(s.indexOf("*")!=-1)
					{
						String[] ss=s.split("\\*");
						return Float.parseFloat(ss[0])*Float.parseFloat(ss[1]);
					}
				if(s.indexOf("/")!=-1)
					{
						String[] ss=s.split("/");
						return Float.parseFloat(ss[0])/Float.parseFloat(ss[1]);
					}
				return Float.parseFloat(s);
			}
		private void draw(myIconDrawer m)
			{
				if (iii == 11)iii = 0;
				if (iii == 0)
					{
						m.draw(DrawType.CIRCLE, false, m.hw, m.hw, m.ww * 7);
						m.draw(DrawType.CIRCLE, true, m.hw, m.ww * 6, m.ww);
						m.draw(DrawType.RECT, true, m.ww * 9, m.ww * 9, m.ww * 11, m.ww * 15);
					}
				if (iii == 1)
					{
						m.move(m.hw,m.qw);
						m.line(m.qw*3,m.hw);
						m.line(m.hw,m.qw*3);
						m.move(m.qw, m.hw);
						m.line(m.qw*3, m.hw);
						m.draw(DrawType.PATH, false);
					}
				if(iii==2)
					{
						m.move(m.hw,m.qw);
						m.line(m.qw,m.hw);
						m.line(m.hw,m.qw*3);
						m.move(m.qw, m.hw);
						m.line(m.qw*3, m.hw);
						m.draw(DrawType.PATH, false);
					}
				if(iii==3)
					{
						m.draw(DrawType.CIRCLE,true,m.hw,m.hw,m.ww);
						m.draw(DrawType.CIRCLE,true,m.hw,m.qw,m.ww);
						m.draw(DrawType.CIRCLE,true,m.hw,m.qw*3,m.ww);
					}
				if(iii==4)
					{
						Paint p=m.getPaint();
						float i=p.getStrokeWidth();
						p.setStrokeWidth(m.ww*4);
						m.draw(DrawType.CIRCLE,false,m.hw,m.hw,m.qw);
						p.setStrokeWidth(i);
						i=m.ww;
						m.move(i*8,i*2);
						m.line(i*12,i*2);
						m.line(i*12,i*4);
						m.line(i*8,i*4);
						m.line(i*8,i*2);

						m.move(i*8,i*18);
						m.line(i*12,i*18);
						m.line(i*12,i*16);
						m.line(i*8,i*16);
						m.line(i*8,i*18);

						m.move(i*2,i*8);
						m.line(i*2,i*12);
						m.line(i*4,i*12);
						m.line(i*4,i*8);
						m.line(i*2,i*8);

						m.move(i*16,i*8);
						m.line(i*18,i*8);
						m.line(i*18,i*12);
						m.line(i*16,i*12);
						m.line(i*16,i*8);

						m.move(i*3,i*6);
						m.line(i*4,i*7);
						m.line(i*7,i*4);
						m.line(i*6,i*3);
						m.line(i*3,i*6);

						m.move(i*14,i*3);
						m.line(i*13,i*4);
						m.line(i*16,i*7);
						m.line(i*17,i*6);
						m.line(i*14,i*3);

						m.move(i*6,i*17);
						m.line(i*7,i*16);
						m.line(i*4,i*13);
						m.line(i*3,i*14);
						m.line(i*6,i*17);

						m.move(i*14,i*17);
						m.line(i*13,i*16);
						m.line(i*16,i*13);
						m.line(i*17,i*14);
						m.line(i*14,i*17);
						m.draw(DrawType.PATH,true);
					}
				if(iii==5)
					{
						m.move(m.ww*4,m.ww*4);
						m.line(m.ww*16,m.ww*16);
						m.move(m.ww*16,m.ww*4);
						m.line(m.ww*4,m.ww*16);
						m.draw(DrawType.PATH,false);
					}
				if(iii==6)
					{
						m.draw(DrawType.LINE,false,m.ww*14,m.ww*3,m.ww*4.5f,m.ww*3);
						m.draw(DrawType.LINE,false,m.ww*4,m.ww*3.5f,m.ww*4,m.ww*14);
						m.draw(DrawType.ARC,false,m.ww*4,m.ww*3,m.ww*5,m.ww*4,180,90);
						m.draw(DrawType.ROUNDRECT,false,m.ww*6,m.ww*5,m.ww*16,m.ww*17,m.ww/2,m.ww/2);
					}
				if(iii==7){
						m.draw(DrawType.ARC,false,m.ww*4,m.ww*4,m.ww*5,m.ww*5,180,90);
						m.draw(DrawType.ARC,false,m.ww*4,m.ww*16,m.ww*5,m.ww*17,90,90);
						m.draw(DrawType.ARC,false,m.ww*15,m.ww*4,m.ww*16,m.ww*5,270,90);
						m.draw(DrawType.ARC,false,m.ww*15,m.ww*16,m.ww*16,m.ww*17,0,90);
						m.draw(DrawType.LINE,false,m.ww*4,m.ww*4.5f,m.ww*4,m.ww*16.5f);
						m.draw(DrawType.LINE,false,m.ww*16,m.ww*4.5f,m.ww*16,m.ww*16.5f);
						m.draw(DrawType.LINE,false,m.ww*4.5f,m.ww*17,m.ww*15.5f,m.ww*17);
						m.draw(DrawType.RECT,true,m.ww*6,m.ww*4.5f,m.ww*14,m.ww*7);
						m.draw(DrawType.CIRCLE,false,m.ww*10,m.ww*4,m.ww);
						m.draw(DrawType.LINE,false,m.ww*4.5f,m.ww*4,m.ww*9,m.ww*4);
						m.draw(DrawType.LINE,false,m.ww*11,m.ww*4,m.ww*15.5f,m.ww*4);
						
				}
				if(iii==8){
						m.draw(DrawType.CIRCLE,false,m.ww*6,m.ww*6,m.ww*2);
						m.draw(DrawType.CIRCLE,false,m.ww*6,m.ww*14,m.ww*2);
						m.draw(DrawType.CIRCLE,false,m.hw,m.hw,m.ww/5);
						m.draw(DrawType.LINE,false,m.ww*7.5f,m.ww*7.5f,m.ww*9.7f,m.ww*9.7f);
						m.draw(DrawType.LINE,false,m.ww*7.5f,m.ww*12.5f,m.ww*9.7f,m.ww*10.3f);
						m.draw(DrawType.LINE,false,m.ww*10.3f,m.ww*10.3f,m.ww*16,m.ww*16);
						m.draw(DrawType.LINE,false,m.ww*11,m.ww*9,m.ww*16,m.ww*4);
						
				}
				if(iii==9){
						m.draw(DrawType.ARC,false,m.ww*4,m.ww*4,m.ww*5,m.ww*5,180,90);
						m.draw(DrawType.ARC,false,m.ww*4,m.ww*15,m.ww*5,m.ww*16,90,90);
						m.draw(DrawType.ARC,false,m.ww*15,m.ww*4,m.ww*16,m.ww*5,270,90);
						m.draw(DrawType.ARC,false,m.ww*15,m.ww*15,m.ww*16,m.ww*16,0,90);
						m.draw(DrawType.RECT,false,m.ww*7,m.ww*7,m.ww*13,m.ww*13);
						m.draw(DrawType.POINT,false,m.hw,m.ww*4);
						m.draw(DrawType.POINT,false,m.hw,m.ww*16);
						m.draw(DrawType.POINT,false,m.ww*4,m.hw);
						m.draw(DrawType.POINT,false,m.ww*16,m.hw);
						m.draw(DrawType.POINT,false,m.ww*7,m.ww*4);
						m.draw(DrawType.POINT,false,m.ww*13,m.ww*4);
						m.draw(DrawType.POINT,false,m.ww*7,m.ww*16);
						m.draw(DrawType.POINT,false,m.ww*13,m.ww*16);
						m.draw(DrawType.POINT,false,m.ww*4,m.ww*7);
						m.draw(DrawType.POINT,false,m.ww*4,m.ww*13);
						m.draw(DrawType.POINT,false,m.ww*16,m.ww*7);
						m.draw(DrawType.POINT,false,m.ww*16,m.ww*13);
						
				}
				if(iii==10){
						m.move(m.ww*3,m.ww*11);
						m.line(m.ww*7,m.ww*15);
						m.line(m.ww*7,m.ww*15);
						m.line(m.ww*17,m.ww*5);
						m.draw(DrawType.PATH,false);
						
				}
			}
		private void save()
			{
				try
					{
						String fileName="icon";
						String filePath = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
							+ "/PIC" + fileName + ".png";
						FileOutputStream fos = new FileOutputStream(new File(filePath));
						mid.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
						MediaScannerConnection.scanFile(this,
							new String[] { filePath }, null,
							new MediaScannerConnection.OnScanCompletedListener() {
									@Override
									public void onScanCompleted(String path, Uri uri)
										{

										}});
						Toast.makeText(this, "保存成功,文件名:" + fileName + ".png",1).show();
					}
				catch (Throwable e)
					{}
			}*/
	}
