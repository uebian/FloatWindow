package com.yzrilyzr.mp3edit;

import java.io.*;
import java.util.HashMap;

public class MusicId3
{
	public HashMap<String,Object> map;
	public int version,revision,flag;
	public MusicId3(String mp3path) throws Throwable
	{
			InputStream is=new FileInputStream(mp3path);
			byte[] id3=new byte[3];
			is.read(id3,0,3);
			map=new HashMap<String,Object>();
			if(new String(id3).equalsIgnoreCase("ID3"))
			{
				//System.out.println("ID3");
				version=is.read();
				revision=is.read();
				flag=is.read();
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
						map.put(FrameID,new String(FrameInfo,enc));
					}
					else
					{
						is.read(FrameInfo);
						map.put(FrameID,new String(FrameInfo,enc));
					}

					if(FrameID.equals("APIC"))
					{
						byte[] bs=new byte[FrameInfo.length-13];
						for(int i=0;i<bs.length;i++)
						{
							bs[i]=FrameInfo[i+13];
						}
						map.put("APIC",bs);
					}
					index+=10+FrameSize;
				}	
				is.close();
				//	util.alert(ListView.getContext(),tmp);
				//System.out.println(music.TPE2);
			}
			else{
				throw new Exception("不是标准的MP3文件");
			}
	}
	public static final String[] id=new String[]{
		"APIC",
		"TEXT",//: 歌词作者 
		"TENC",//: 编码 
		"WXXX",//: URL链接(URL) 
		"TCOP",//: 版权(Copyright) 
		"TOPE",//: 原艺术家 
		"TCOM",//: 作曲家 
		"TDAT",//: 日期 
		"TPE3",//: 指挥者 
		"TPE2",//: 乐队 
		"TPE1",//: 艺术家相当于ID3v1的Artist 
		"TPE4",//: 翻译（记录员、修改员） 
		"TYER",//: 年代相当于ID3v1的Year 
		"USLT",//: 歌词 
		"TALB",//: 专辑相当于ID3v1的Album 
		"TIT1",//: 内容组描述 
		"TIT2",//: 标题相当于ID3v1的Title 
		"TIT3",//: 副标题 
		"TCON",//: 流派（风格）相当于ID3v1的Genre见下表 
		"TBPM",//: 每分钟节拍数 
		"COMM",//: 注释相当于ID3v1的Comment 
		"TDLY",//: 播放列表返录 
		"TRCK",//: 音轨（曲号）相当于ID3v1的Track 
		"TFLT",//: 文件类型 
		"TIME",//: 时间　 
		"TKEY",//: 最初关键字 
		"TLAN",//: 语言 
		"TLEN",//: 长度 
		"TMED",//: 媒体类型 
		"TOAL",//: 原唱片集 
		"TOFN",//: 原文件名 
		"TOLY",//: 原歌词作者 
		"TORY",//: 最初发行年份 
		"TOWM",//: 文件所有者（许可证者） 
		"TPOS",//: 作品集部分 
		"TPUB",//: 发行人 
		"TRDA",//: 录制日期 
		"TRSN",//: Intenet电台名称 
		"TRSO",//: Intenet电台所有者 
		"TSIZ",//: 大小 　 
		"TSRC",//: ISRC（国际的标准记录代码） 
		"TSSE",//: 编码使用的软件（硬件设置） 
		"UFID",//: 唯一的文件标识符 
		"AENC"//: 音频加密技术
		};
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
}
