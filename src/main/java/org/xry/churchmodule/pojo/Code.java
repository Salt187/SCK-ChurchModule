package org.xry.churchmodule.pojo;

public class Code {
    //成功码
    public final static Integer INSERT_OK = 20011;
    public final static Integer UPDATE_OK = 20021;
    public final static Integer DELETE_OK = 20031;
    public final static Integer SELECT_OK = 20041;

    //失败码
    public final static Integer INSERT_ERROR = 20010;
    public final static Integer UPDATE_ERROR = 20020;
    public final static Integer DELETE_ERROR = 20030;
    public final static Integer SELECT_ERROR = 20040;

    //异常码
    public final static Integer SYSTEM_ERROR = 50010;
    public final static Integer BUSINESS_ERROR = 50020;
    public final static Integer Unknown_ERROR = 50000;

}
