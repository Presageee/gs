package com.gs.cache.impl;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
class LocalCacheData {
    private long expiredTime;

    private long createTime;

    private byte[] data;

    public LocalCacheData(byte[] data) {
        this.data = data;
        this.expiredTime = -1;
        this.createTime = System.currentTimeMillis();
    }

    public LocalCacheData(long expiredTime, byte[] data) {
        this.expiredTime = expiredTime;
        this.data = data;
        this.createTime = System.currentTimeMillis();
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
