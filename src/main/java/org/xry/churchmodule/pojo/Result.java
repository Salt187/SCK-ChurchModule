package org.xry.churchmodule.pojo;

//前后端协议规范
public class Result {
    private Object data;
    private String msg;
    private Integer code;

    public Result(Object data, String msg, Integer code) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
