package com.yzrilyzr.myclass;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.util.*;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.ui.mySimpleAdapter;
public class myMusicPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener,Runnable,OnItemClickListener,OnItemLongClickListener
		{
				int pos=0;
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
						{
								// TODO: Implement this method
								curMusic=p3;
								play();
						}

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
						{
								// TODO: Implement this method
								return true;
						}

				private ArrayList<music> MUSICS;
				private myInterface myinterface;
				public final int OMR=2,OMS=3,AMR=4,AMS=5;
				private int type=3;
				public int curMusic=0;
				private boolean isRefresh=false,useRan=false;
				public void setCurrentMusic(int i)
						{curMusic = i;}

				@Override
				public void run()
						{
								try
										{
												if(isRefresh)
														{
																int i=getCurrentPosition();
																if(myinterface!=null)myinterface.refresh(this,i,parseTime(i));
																new Handler().postDelayed(this,500);
														}
										}
								catch(Exception e)
										{}
						}

				public void play()
						{
								try
										{
												parsePos();
												reset();
												setDataSource(MUSICS.get(curMusic).path);
												setOnCompletionListener(this);
												prepare();
												start();
												callOnMediaChange();
												startRefresh();
												//log(musicList[curMusic].getAbsolutePath());
										}
								catch(IOException e)
										{/*log(e.toString());*/}
						}
				public void callOnMediaChange()
						{
								if(myinterface!=null)myinterface.onMediaChange(this,MUSICS.get(curMusic));
						}
				public void parsePos()
						{
								if(curMusic<0)curMusic=MUSICS.size()-1;
								if(curMusic>=MUSICS.size())curMusic=0;
						}

				public void startRefresh()
						{
								if(isRefresh)return;
								isRefresh=true;
								new Handler().postDelayed(this,500);
								//thread.start();
						}
				public void stopRefresh()
						{
								isRefresh=false;
						}
				public void setInterface(myInterface m)
						{myinterface=m;}
				@Override
				public void onCompletion(MediaPlayer p1)
						{
								// TODO: Implement this method
								int max=MUSICS.size();
								if(myinterface!=null)myinterface.onCompletion(this);
								if(type==OMR)
										{}
								if(type==OMS)
										{seekTo(0);return;}
								if(type==AMR&&!useRan)
										{curMusic++;}
								if(type==AMR&&useRan)
										{curMusic=new Random().nextInt(max-1);}
								if(type==AMS&&!useRan)
										{curMusic++;if(curMusic>=max)
														{seekTo(0);return;}}
								if(type==AMS&&useRan)
										{curMusic=new Random().nextInt(max-1);if(curMusic>=max)
														{seekTo(0);return;}}
								//log(type+","+curMusic);
								play();
						}
				public int playOrPause()
						{
								if(isPlaying())pause();
								else start();
								return isPlaying()?R.drawable.pause:R.drawable.play;
						}
				public int setRandom()
						{
								useRan=!useRan;
								return useRan?R.drawable.playbyrandom:R.drawable.playbylist;
						}
				public void next()
						{
								curMusic++;
								if(useRan)curMusic=new Random().nextInt(MUSICS.size() -1);
								boolean b=isPlaying();
								play();
								if(!b)pause();
						}
				public void prev()
						{
								curMusic--;
								if(useRan)curMusic=new Random().nextInt(MUSICS.size() -1);
								boolean b=isPlaying();
								play();
								if(!b)pause();
						}
				public int setMode()
						{
								type++;
								if(type>AMS)type=OMR;
								int t = 0;
								if(type==AMR)t=R.drawable.repeat;
								if(type==AMS)t=R.drawable.allstop;
								if(type==OMR)t=R.drawable.onerepeat;
								if(type==OMS)t=R.drawable.onestop;
								return t;
						}
				public interface myInterface
						{
								//每次刷新
								public abstract void refresh(myMusicPlayer p1,int progress,String curtime);
								//更换音乐
								public abstract void onMediaChange(myMusicPlayer p1,music info);
								//放完
								public abstract void onCompletion(myMusicPlayer p1);
								//	public abstract void receiveList(ListAdapter p1);
						}
				public void setCurrentMusicByFilter(final File p)
						{
								//new Thread(new Runnable(){@Override public void run(){
								try
										{
												for(int i=0;i<MUSICS.size();i++)
														{
																String a=p.getCanonicalPath();
																String b=new File(MUSICS.get(i).path).getCanonicalPath();
																if(a.equals(b))
																		{
																				setCurrentMusic(i);
																				break;
																		}
														}
										}
								catch(Exception e)
										{}
						}
				//}}).start();

				public class music
						{
								public Object TEXT,APIC,//: 歌词作者 
								TENC,//: 编码 
								WXXX,//: URL链接(URL) 
								TCOP,//: 版权(Copyright) 
								TOPE,//: 原艺术家 
								TCOM,//: 作曲家 
								TDAT,//: 日期 
								TPE3,//: 指挥者 
								TPE2,//: 乐队 
								TPE1,//: 艺术家相当于ID3v1的Artist 
								TPE4,//: 翻译（记录员、修改员） 
								TYER,//: 年代相当于ID3v1的Year 
								USLT,//: 歌词 
								TALB,//: 专辑相当于ID3v1的Album 
								TIT1,//: 内容组描述 
								TIT2,//: 标题相当于ID3v1的Title 
								TIT3,//: 副标题 
								TCON,//: 流派（风格）相当于ID3v1的Genre见下表 
								TBPM,//: 每分钟节拍数 
								COMM,//: 注释相当于ID3v1的Comment 
								TDLY,//: 播放列表返录 
								TRCK,//: 音轨（曲号）相当于ID3v1的Track 
								TFLT,//: 文件类型 
								TIME,//: 时间　 
								TKEY,//: 最初关键字 
								TLAN,//: 语言 
								TLEN,//: 长度 
								TMED,//: 媒体类型 
								TOAL,//: 原唱片集 
								TOFN,//: 原文件名 
								TOLY,//: 原歌词作者 
								TORY,//: 最初发行年份 
								TOWM,//: 文件所有者（许可证者） 
								TPOS,//: 作品集部分 
								TPUB,//: 发行人 
								TRDA,//: 录制日期 
								TRSN,//: Intenet电台名称 
								TRSO,//: Intenet电台所有者 
								TSIZ,//: 大小 　 
								TSRC,//: ISRC（国际的标准记录代码） 
								TSSE,//: 编码使用的软件（硬件设置） 
								UFID,//: 唯一的文件标识符 
								AENC;//: 音频加密技术
								public String path;
								public int DURATION;
						}	
				public void destory()
						{
								stopRefresh();
								stop();
						}
				public void setPath(String s)
						{
								File[] fs=new File(s).listFiles(new FileFilter() {
														public boolean accept(File file)
																{
																		//if the file extension is .txt return true, else false
																		String n=file.getName();
																		String[] f=myFileExplorer.EXT[8].split("|");
																		for(String d:f)
																				{
																						if(n.endsWith(d)&&file.isFile())return true;
																				}
																		return false;
																}
												}
								);
								Arrays.sort(fs, new Comparator<File>() {

														@Override
														public int compare(File o1, File o2)
																{
																		// TODO: Implement this method
																		if (o1.isDirectory() && o2.isFile())
																				return -1;
																		if (o1.isFile() && o2.isDirectory())
																				return 1;
																		return o1.getName().compareTo(o2.getName());
																}
																});
								MUSICS=new ArrayList<music>();
								for(File f:fs)
										{
												music m=new MusicId3(f.toString()).getMusic();
												MUSICS.add(m);
										}
						}
				public String parseTime(long ms)
						{
								int m=(int)Math.floor(ms/60000);
								int s=(int)Math.floor((ms-60000*m)/1000);
								return (m<10?"0"+m:m)+":"+(s<10?"0"+s:s);
						}
				public class MusicId3
						{
								public music getMusic(){return music;}
								private music music;
								public MusicId3(String mp3path)
										{
												try{
																music=new music();
																music.path=mp3path;
																InputStream is=new FileInputStream(mp3path);
																byte[] id3=new byte[3];
																is.read(id3,0,3);
																String tmp="";
																if(new String(id3).equalsIgnoreCase("ID3"))
																		{
																				//System.out.println("ID3");
																				int ver=is.read();
																				int revision=is.read();
																				int flag=is.read();
																				/*1.标志字节
																				 标志字节一般为0，定义如下：
																				 abc00000
																				 a -- 表示是否使用不同步(一般不设置)
																				 b -- 表示是否有扩展头部，一般没有(至少Winamp没有记录)，所以一般也不设置
																				 c -- 表示是否为测试标签(99.99%的标签都不是测试用的啦，所以一般也不设置)*/
																				int total_Size=is.read()*0x200000
																						+is.read()*0x4000
																						+is.read()*0x80
																						+is.read();
																				//	System.out.println("ver:"+ver+",revision:"+revision+",flag:"+flag+",totalsize:"+total_Size);
																				//	System.out.println("===========");
																				int index=0;
																				while(index<total_Size)
																						{
																								byte[] id=new byte[4];
																								is.read(id);
																								String FrameID=new String(id);//名字
																								int FrameSize=
																										is.read()*0x1000000
																										+is.read()*0x10000
																										+is.read()*0x100
																										+is.read();//大小
																								if(FrameSize<=0)break;
																								byte[] FrameFlag=new byte[2];//标识
																								is.read(FrameFlag);
																								byte[] FrameInfo=new byte[FrameSize];
																								String enc="utf-8";
																								if(idIsString(FrameID))
																										{
																												switch(is.read())
																														{
																																case 0:enc="ISO-8859-1";break;
																																case 1:enc="UTF-16";break;
																																case 2:enc="UTF-16BE";break;
																																case 3:enc="UTF-8";break;
																														}												
																												FrameInfo=new byte[FrameSize-1];//内容
																												is.read(FrameInfo);
																												getObjByName(FrameID,music,new String(FrameInfo,enc));
																										}
																								else
																										{
																												is.read(FrameInfo);
																												getObjByName(FrameID,music,FrameInfo);
																										}

																								if(FrameID.equals("APIC"))
																										{
																												byte[] bs=new byte[FrameInfo.length-13];
																												for(int i=0;i<bs.length;i++)
																														{
																																bs[i]=FrameInfo[i+13];
																														}
																												music.APIC=bs;
																										}
																								String info=FrameInfo.length>1000?"too long": new String(FrameInfo,enc);
																								tmp+=("\n\nID:"+FrameID+",Size:"+FrameSize+",flag:"+FrameFlag[1]);
																								tmp+=("\nenc:"+enc+",info:"+info);
																								index+=10+FrameSize;
																						}	
																				is.close();
																			//	util.alert(ListView.getContext(),tmp);
																				//System.out.println(music.TPE2);
																		}
														}catch(Throwable e){}
										}
								public boolean idIsString(String id)
										{
												if(id.equals(
															 "TEXT")||id.equals(//: 歌词作者 
															 "TENC")||id.equals(//: 编码 
															 "WXXX")||id.equals(//: URL链接(URL) 
															 "TCOP")||id.equals(//: 版权(Copyright) 
															 "TOPE")||id.equals(//: 原艺术家 
															 "TCOM")||id.equals(//: 作曲家 
															 "TDAT")||id.equals(//: 日期 
															 "TPE3")||id.equals(//: 指挥者 
															 "TPE2")||id.equals(//: 乐队 
															 "TPE1")||id.equals(//: 艺术家相当于ID3v1的Artist 
															 "TPE4")||id.equals(//: 翻译（记录员、修改员） 
															 "TYER")||id.equals(//: 年代相当于ID3v1的Year 
															 "USLT")||id.equals(//: 歌词 
															 "TALB")||id.equals(//: 专辑相当于ID3v1的Album 
															 "TIT1")||id.equals(//: 内容组描述 
															 "TIT2")||id.equals(//: 标题相当于ID3v1的Title 
															 "TIT3")||id.equals(//: 副标题 
															 "TCON")||id.equals(//: 流派（风格）相当于ID3v1的Genre见下表 
															 //"TBPM")||id.equals(//: 每分钟节拍数 
															 "COMM")||id.equals(//: 注释相当于ID3v1的Comment 
															 "TDLY")||id.equals(//: 播放列表返录 
															 //"TRCK")||id.equals(//: 音轨（曲号）相当于ID3v1的Track 
															 "TFLT")||id.equals(//: 文件类型 
															 //"TIME")||id.equals(//: 时间　 
															 "TKEY")||id.equals(//: 最初关键字 
															 "TLAN")||id.equals(//: 语言 
															 //"TLEN")||id.equals(//: 长度 
															 "TMED")||id.equals(//: 媒体类型 
															 "TOAL")||id.equals(//: 原唱片集 
															 "TOFN")||id.equals(//: 原文件名 
															 "TOLY")||id.equals(//: 原歌词作者 
															 "TORY")||id.equals(//: 最初发行年份 
															 "TOWM")||id.equals(//: 文件所有者（许可证者） 
															 "TPOS")||id.equals(//: 作品集部分 
															 "TPUB")||id.equals(//: 发行人 
															 "TRDA")||id.equals(//: 录制日期 
															 "TRSN")||id.equals(//: Intenet电台名称 
															 "TRSO")||id.equals(//: Intenet电台所有者 
															// "TSIZ")||id.equals(//: 大小 　 
															 "TSRC")||id.equals(//: ISRC（国际的标准记录代码） 
															 "TSSE")||id.equals(//: 编码使用的软件（硬件设置） 
															 "UFID")||id.equals(//: 唯一的文件标识符 
															 "AENC"))return true;//: 音频加密技术
												else  return false;
										}
								public void  getObjByName(String i,music m,Object obj)
										{
												switch(i)
														{
																case "APIC":m.APIC=obj;break;
																case "TEXT":m.TEXT=obj;break;//: 歌词作者 
																case "TENC":m.TENC=obj;break;//: 编码 
																case "WXXX":m.WXXX=obj;break;//: URL链接(URL) 
																case "TCOP":m.TCOP=obj;break;//: 版权(Copyright) 
																case "TOPE":m.TOPE=obj;break;//: 原艺术家 
																case "TCOM":m.TCOM=obj;break;//: 作曲家 
																case "TDAT":m.TDAT=obj;break;//: 日期 
																case "TPE3":m.TPE3=obj;break;//: 指挥者 
																case "TPE2":m.TPE2=obj;break;//: 乐队 
																case "TPE1":m.TPE1=obj;break;//: 艺术家相当于ID3v1的Artist 
																case "TPE4":m.TPE4=obj;break;//: 翻译（记录员、修改员） 
																case "TYER":m.TYER=obj;break;//: 年代相当于ID3v1的Year 
																case "USLT":m.USLT=obj;break;//: 歌词 
																case "TALB":m.TALB=obj;break;//: 专辑相当于ID3v1的Album 
																case "TIT1":m.TIT1=obj;break;//: 内容组描述 
																case "TIT2":m.TIT2=obj;break;//: 标题相当于ID3v1的Title 
																case "TIT3":m.TIT3=obj;break;//: 副标题 
																case "TCON":m.TCON=obj;break;//: 流派（风格）相当于ID3v1的Genre见下表 
																case "TBPM":m.TBPM=obj;break;//: 每分钟节拍数 
																case "COMM":m.COMM=obj;break;//: 注释相当于ID3v1的Comment 
																case "TDLY":m.TDLY=obj;break;//: 播放列表返录 
																case "TRCK":m.TRCK=obj;break;//: 音轨（曲号）相当于ID3v1的Track 
																case "TFLT":m.TFLT=obj;break;//: 文件类型 
																case "TIME":m.TIME=obj;break;//: 时间　 
																case "TKEY":m.TKEY=obj;break;//: 最初关键字 
																case "TLAN":m.TLAN=obj;break;//: 语言 
																case "TLEN":m.TLEN=obj;break;//: 长度 
																case "TMED":m.TMED=obj;break;//: 媒体类型 
																case "TOAL":m.TOAL=obj;break;//: 原唱片集 
																case "TOFN":m.TOFN=obj;break;//: 原文件名 
																case "TOLY":m.TOLY=obj;break;//: 原歌词作者 
																case "TORY":m.TORY=obj;break;//: 最初发行年份 
																case "TOWM":m.TOWM=obj;break;//: 文件所有者（许可证者） 
																case "TPOS":m.TPOS=obj;break;//: 作品集部分 
																case "TPUB":m.TPUB=obj;break;//: 发行人 
																case "TRDA":m.TRDA=obj;break;//: 录制日期 
																case "TRSN":m.TRSN=obj;break;//: Intenet电台名称 
																case "TRSO":m.TRSO=obj;break;//: Intenet电台所有者 
																case "TSIZ":m.TSIZ=obj;break;//: 大小 　 
																case "TSRC":m.TSRC=obj;break;//: ISRC（国际的标准记录代码） 
																case "TSSE":m.TSSE=obj;break;//: 编码使用的软件（硬件设置） 
																case "UFID":m.UFID=obj;break;//: 唯一的文件标识符 
																case "AENC":m.AENC=obj;break;//: 音频加密技
														}
										}
										}
								public void getFileList(ListView lv){
												//new Thread(new Runnable(){@Override public void run(){try{
												ListView=lv;
												Context ctx=lv.getContext();
												List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
												final String a="a",b="b",c="c";
												for(int i=0;i<MUSICS.size();i++){
																Map map=new HashMap<String,Object>();
																music music=MUSICS.get(i);
																map.put(a,i==curMusic?R.drawable.play:R.drawable.format_music);
																map.put(b,(i+1)+"."+music.TIT2);
																map.put(c,parseTime(music.DURATION)+" "+music.TPE1);
																list.add(map);
														}
												SimpleAdapter=new mySimpleAdapter(ctx,list,R.layout.layout_entry,new String[]{a,b,c},null);
												if(lv!=null)setListView(lv);
										}
								private ListView ListView;
								private SimpleAdapter SimpleAdapter;
								public void setListView(ListView l){
												ListView=l;
												l.setAdapter(SimpleAdapter);
												l.setOnItemClickListener(this);
												l.setOnItemLongClickListener(this);
												l.setSelection(curMusic);
										}
						
						/*
				 Comparator<Music> comparator = new Comparator<Music>(){
				 @Override public int compare(Music s1,Music s2) {
				 if(!s1.PATH.equals(s2.PATH)){
				 return s1.PATH.compareTo(s2.PATH);
				 }
				 return 0;
				 }
				 };
				 public void setPath(Context ctx,String path){
				 //				new Thread(new Runnable(){@Override public void run(){
				 try{
				 MUSICS=queryMusics(ctx);
				 String fs=new File(path).getCanonicalPath();
				 for(int i=0;i<MUSICS.size();i++){
				 String cf=MUSICS.get(i).FILE.getParentFile().getCanonicalPath();
				 if(!cf.equals(fs)){
				 MUSICS.remove(i);
				 --i;
				 }
				 }
				 Collections.sort(MUSICS,comparator);
				 }catch(IOException e){}
				 //	}}).start();
				 }

				 }
				 

				 public interface myInterface{
				 //每次刷新
				 public abstract void refresh(myMusicPlayer p1,int progress,String curtime);
				 //更换音乐
				 public abstract void onMediaChange(myMusicPlayer p1,Music info);
				 //放完
				 public abstract void onCompletion(myMusicPlayer p1);
				 //	public abstract void receiveList(ListAdapter p1);
				 }
				 public ArrayList<Music> queryMusics(Context ctx) {
				 ArrayList<Music> musiclistResult = new ArrayList<Music>();
				 ContentResolver cr = ctx.getContentResolver();
				 Cursor musics = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				 //Log.d("", "musics : " + musics.getCount());
				 musics.moveToFirst();
				 while (!musics.isAfterLast()) {
				 Music temp = new Music();
				 temp._ID = musics.getInt(musics.getColumnIndex(MediaStore.Audio.Media._ID));
				 temp.ALBUM = musics.getString(musics.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				 temp.ARTIST = musics.getString(musics.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				 temp.ALBUM_ID= musics.getInt(musics.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)); 
				 //temp.ALBUM_PIC=getAlbumArt(ctx,temp.ALBUM_ID); 
				 temp.FILE= new File(musics.getString(musics.getColumnIndex(MediaStore.Audio.Media.DATA)));
				 temp.PATH=musics.getString(musics.getColumnIndex(MediaStore.Audio.Media.DATA));
				 temp.DURATION = musics.getLong(musics.getColumnIndex(MediaStore.Audio.Media.DURATION));
				 temp.TITLE = musics.getString(musics.getColumnIndex(MediaStore.Audio.Media.TITLE));
				 temp.isExist =temp.FILE.exists();
				 musiclistResult.add(temp);
				 musics.moveToNext();
				 }
				 musics.close();
				 return musiclistResult;
				 }public String getAlbumArt(Context ctx,int album_id) { 
				 String mUriAlbums = "content://media/external/audio/albums"; 
				 String[] projection = new String[] { "album_art" }; 
				 Cursor cur = ctx.getContentResolver().query( 
				 Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), 
				 projection, null, null, null); 
				 String album_art = null; 
				 if (cur.getCount() > 0 && cur.getColumnCount() > 0) { 
				 cur.moveToNext(); 
				 album_art = cur.getString(0); 
				 } 
				 cur.close(); 
				 cur = null; 
				 return album_art; 
				 } 
				 @SuppressWarnings("unused")
				 public class Music {
				 public int _ID,ALBUM_ID;
				 public File FILE;
				 public long DURATION;
				 public boolean isExist;
				 //public Bitmap ALBUM_PIC;
				 public String TITLE, ARTIST, ALBUM,PATH;
				 }	*/
		
		}
