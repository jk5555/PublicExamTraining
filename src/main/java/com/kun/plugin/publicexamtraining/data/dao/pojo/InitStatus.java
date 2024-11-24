package com.kun.plugin.publicexamtraining.data.dao.pojo;



import java.util.Date;

public class InitStatus {

    private Integer initFlag;
    private Date initTime;

    public Integer getInitFlag() {
        return initFlag;
    }

    public void setInitFlag(Integer initFlag) {
        this.initFlag = initFlag;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }
}
