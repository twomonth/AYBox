package com.aygames.twomonth.aybox.util;

import java.util.ArrayList;
import java.util.Map;

/**
 * 网络接口常量类
 * Created by MyPC on 2017/3/1.
 */

public class Constans {
    //检测版本更新接口
    public final static String APP_UPDATE="http://www.syyouxi.com/SDKconfigAPI/AppisUpdate";
    //apk更新接口
    public final static String APP_DOWNLOAD = "http://www.syyouxi.com/SDKconfigAPI/startApp/";
    //分享订单发送接口
    public final static  String ORDER_SHARE = "http://www.syyouxi.com/ShareAPI/ShareAdd";
    //推广订单发送接口
    public final static String ORDER_TUIGUANG= "http://www.syyouxi.com/SDKconfig/Promote";
    //推广获取用户数据接口
    public final static  String TUIGUANG = "http://www.syyouxi.com/SDKconfigAPI/Pro";
    //礼包领取页
    public final static String GIFT ="http://www.ofwan.com/Home/Wap/gamegift/gid/";
    //分享游戏主页连接
    public final static String GAME_HOME="http://www.ofwan.com/gcontent/";
    //获取渠道名称
    public  final static String CHANNEL_NAME="http://www.syyouxi.com/SDKconfigAPI/GetChannelName";
    //登录接口
    public final static String URL_USER_REGISTER = "http://www.syyouxi.com/UserAPI/Register";
    //广告接口
    public final static String URL_ADVERTISINT="http://www.syyouxi.com/SDKconfigAPI/Advertising";
    //广告统计
    public final static String URL_ADVERADD = "http://www.syyouxi.com/SDKconfigAPI/AdvAdd/id/";
    //更新数量统计
    public final static String URL_APPISUPDATECLICK = "http://www.syyouxi.com/SDKconfigAPI/AppisUpdateClick/id/";
    //发送手机串号等用户信息
    public final static String URL_IMEI ="http://www.syyouxi.com/SDKconfigAPI/getImei";
    //用户手机串号
    public  static String iemi = "";
    //渠道号
    public static String channel = "";
    //现有包信息
    public static ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
    //消息推送接口
    public static String URL_MESSAGE = "http://www.syyouxi.com/SDKconfigAPI/MessageApi";
    //消息推送统计接口
    public static String URL_MESSAGE_TONGJI = "http://www.syyouxi.com/SDKconfigAPI/SDgetdata/";
    //获取app下载地址的接口
    public final static String URL_APPADDRESS = "http://www.syyouxi.com/index.php/Home/ChannelApi/ChannelBox";
    //获取内容页接口
    public final  static String URL_HOMEPAGE = "http://www.syyouxi.com/index.php/Home/ChannelApi/ChannelFind";
    //分享数据获取接口
    public static String URL_GETMESSAGE_FENXIANG = "http://www.syyouxi.com/index.php/Home/ChannelApi/ChannelGame";
    //礼包领取页面接口
    public static String URL_GETGIFT = "http://www.syyouxi.com/index.php/Home/ChannelApi/ChannelGameWap";
    //广告下载地址
    public static String URL_ADVERDOWNLOAD = "http://www.syyouxi.com/index.php/Home/ChannelApi/ChannelGameDownload";
}
