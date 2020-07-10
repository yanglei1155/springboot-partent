package com.insigma.po.soldier;

import java.math.BigDecimal;
/**
 * 
 * 类描述：   灵活就业申请信息
 * 创建人：刘伟民   
 * 创建时间：2020年5月26日 下午4:17:36   
 * @version
 */
public class FlexibleEmploymentInfo {
    private Integer feiId;

    private Integer sbiId;

    private String bankAccount;

    private String name;

    private String bankName;

    private Integer wheReceRelfSubs;

    private BigDecimal selfSubsAmount;

    private Integer wheLivAllow;

    private BigDecimal paymentMoney;

    private Integer paymentMonth;

    private String domicile;
    
    private String filePath;

    private String idcard;
    
    
    public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFeiId() {
        return feiId;
    }

    public void setFeiId(Integer feiId) {
        this.feiId = feiId;
    }

    public Integer getSbiId() {
        return sbiId;
    }

    public void setSbiId(Integer sbiId) {
        this.sbiId = sbiId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public Integer getWheReceRelfSubs() {
        return wheReceRelfSubs;
    }

    public void setWheReceRelfSubs(Integer wheReceRelfSubs) {
        this.wheReceRelfSubs = wheReceRelfSubs;
    }

    public BigDecimal getSelfSubsAmount() {
        return selfSubsAmount;
    }

    public void setSelfSubsAmount(BigDecimal selfSubsAmount) {
        this.selfSubsAmount = selfSubsAmount;
    }

    public Integer getWheLivAllow() {
        return wheLivAllow;
    }

    public void setWheLivAllow(Integer wheLivAllow) {
        this.wheLivAllow = wheLivAllow;
    }

    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public Integer getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(Integer paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile == null ? null : domicile.trim();
    }
}