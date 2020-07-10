package com.insigma.po.soldier;

import java.util.Date;

public class PersonnelArchivesLog {
    private Integer palId;

    private Integer sbiId;

    private String context;

    private String operator;

    private Date createTime;

    public Integer getPalId() {
        return palId;
    }

    public void setPalId(Integer palId) {
        this.palId = palId;
    }

    public Integer getSbiId() {
        return sbiId;
    }

    public void setSbiId(Integer sbiId) {
        this.sbiId = sbiId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}