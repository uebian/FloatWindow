package com.yzrilyzr.plugin;

import java.io.*;

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
                //  System.out.println("ver:"+ver+",revision:"+revision+",flag:"+flag+",totalsize:"+total_Size);
                //  System.out.println("===========");
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
                //  util.alert(ListView.getContext(),tmp);
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
