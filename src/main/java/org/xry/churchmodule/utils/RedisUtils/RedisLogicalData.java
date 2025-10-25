package org.xry.churchmodule.utils.RedisUtils;

import java.time.LocalDateTime;

public class RedisLogicalData {
    private Object data;
    private LocalDateTime expireTime;

    public RedisLogicalData() {}

    public RedisLogicalData(Object data, LocalDateTime expireTime) {
        this.data = data;
        this.expireTime = expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
