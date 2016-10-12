package com.traciing.domain;

/**
 * 箱子属性
 * Created by Carl_yang on 2016/6/11.
 */
public class TemInfo {

    private String TemValue;
    private String Boxid;
    private String loss_time;

    public String getTemValue() {
        return TemValue;
    }

    public void setTemValue(String temValue) {
        TemValue = temValue;
    }

    public String getBoxid() {
        return Boxid;
    }

    public void setBoxid(String boxid) {
        Boxid = boxid;
    }

    public String getLoss_time() {
        return loss_time;
    }

    public void setLoss_time(String loss_time) {
        this.loss_time = loss_time;
    }
}
