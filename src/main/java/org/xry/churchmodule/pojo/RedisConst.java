package org.xry.churchmodule.pojo;

public class RedisConst {
//--Redis前缀------------------------------------------------
    //token前缀
    public final static String TOKEN = "Token:";

    //用户信息缓存
    public final static String USER = "User:";

    //短信登录缓存
    public final static String PHONE = "Phone:";

//--时间限制-------------------------------------------
    //120分钟
    public final static Long ADMIN_EXPIRE = 120L;

    //120分钟
    public final static Long TOKEN_EXPIRE = 120L;

    //5分钟
    public final static Long MSG_EXPIRE = 5L;

//--预防缓存穿透的常值---------------------------------------------
    public final static String NULL = "ThereIsNoData";

//--分布式锁固定前缀------------------------------------------------
    public final static String LOCKED = "Locked:";

}
