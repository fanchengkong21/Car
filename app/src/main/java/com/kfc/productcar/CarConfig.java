package com.kfc.productcar;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/20  0:21
 * @PackageName com.kfc.productcar
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class CarConfig {
    public static String KEY_AUTHTOKEN_GETNAME = "key_authtoken_getname";
    public static String KEY_AUTHTOKEN_GETPASS = "key_authtoken_getpass";
    //登录
    public static String LOGIN="api.php?mod=logging&action=login";
    //退出登录
    public static String LOGOUT="api.php?mod=logging&action=logout";
    //发送验证码
    public static String SENDSMS="api.php?mod=sendSMS";
    //重置密码
    public static String RESETPASSWORD="api.php?mod=resetPassword";
    //注册
    public static String REGISTER="api.php?mod=register";
    //获取本地区所有经销商列表
    public static String GROUPLIST="api.php?mod=getGroupList";
    //获取经销商轮播图
    public static String GETDEALERSLIDE="api.php?mod=getDealerSlide";
    //获取经销商首页的其他信息
    public static String GETINDEXINFO="api.php?mod=getIndexInfo";
    //获取经销商首页的车型列表
    public static String GETDEALECARS="api.php?mod=getDealerCars";
    //获取用户基本信息
    public static String USERBASEINFO="api.php?mod=getUserBaseInfo";
    //获取用户推广二维码
    public static String USERQRCODE="api.php?mod=getUserQrcode";
    //上传头像
    public static String POSTPHOTO="api.php?mod=postPhoto";
    //实名认证(验证手机号)
    public static String REALNAME="api.php?mod=verifyPhone";
    //修改用户基本信息
    public static String EDITUSERBASEINFO="api.php?mod=editUserBaseInfo";
    //获取销售代表列表
   public static String SELLERLIST="api.php?mod=getSellerList";
    //根据经销商ID随机获取销售电话
    public static String GETSELLER="api.php?mod=getSeller";
    //给店内留言
    public static String MESSAGE="api.php?mod=postToDealer";
    //绑定销售/经销商
    public static String BINDSELLER="api.php?mod=bindingSeller";

    //获取我的收藏

    public static String FAVORITELIST="api.php?mod=getFavoriteList";
     //获取帖子

    public static String FORUMLIST = "api.php?mod=getThreadList";
    //获取帖子

    public static String MISC = "forum.php?mod=misc&action=recommend&do=add&ajaxmenu=1&ajaxdata=newjson";
    //点赞
    public static String SPACECP = "home.php?mod=spacecp&ac=favorite&type=thread&infloat=yes&handlekey=k_favorite&ajaxdata=newjson&favoritesubmit=1";
    //收藏
    public static String NEWTHREAD = "api.php?mod=newThread";
    //发布帖子
    public static String SWFUPLOAD = "misc.php?mod=swfupload&operation=upload&simple=1&type=image&ajaxdata=json";
    //上传图片
    public static String GETTHREADCLASS = "api.php?mod=getThreadClass";
    //获取版块主题
    //请求添加好友
    public static  String ADDFRIEND="home.php?mod=spacecp&ac=friend&op=add&ajaxdata=newjson&addsubmit=true";
    //获取车友列表
    public static String FRIENDLIST="api.php?mod=getFriendList";
    //删除好友
    public static String DELETEFRIEND="home.php?mod=spacecp&ac=friend&op=ignore&confirm=1&friendsubmit=true&ajaxdata=newjson";
    //获取好友请求列表
    public static  String REQUESTLIST="api.php?mod=getRequestList";
    //确认好友请求
    public static String REQUESTSURE="home.php?mod=spacecp&ac=friend&op=add&ajaxdata=newjson&add2submit=true";
    //获取消息列表
    public static String PMLIST="api.php?mod=getPmList";
    //获取两个人的对话列表
    public static String PMLISTONE="api.php?mod=getPmListOne";
    //给销售代表留言
    public static String SENDMESSAGE="home.php?mod=spacecp&ac=pm&op=send&pmsubmit=yes&pmsubmit=true&ajaxdata=newjson";
}
