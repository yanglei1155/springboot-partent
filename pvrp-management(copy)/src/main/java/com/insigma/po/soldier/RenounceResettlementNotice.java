package com.insigma.po.soldier;

/**
 * 类描述： 放弃安置待遇通知书开具信息  
 * 创建人：刘伟民   
 * 创建时间：2020年5月25日 下午3:19:59   
 * @version
 */
public class RenounceResettlementNotice {
    private Integer rrnId;

    private Integer sbiId;
    
    private String idcard;

    private String name;

    private String registerTimeSlot;

    private String receiveDate;

    private String renounceType;

    private String endDate;

    private String createTime;

    private String registerTime;
    
    private String regionalName;
    
    private String filePath;

    
	public Integer getSbiId() {
		return sbiId;
	}

	public void setSbiId(Integer sbiId) {
		this.sbiId = sbiId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getRrnId() {
		return rrnId;
	}

	public void setRrnId(Integer rrnId) {
		this.rrnId = rrnId;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegisterTimeSlot() {
		return registerTimeSlot;
	}

	public void setRegisterTimeSlot(String registerTimeSlot) {
		this.registerTimeSlot = registerTimeSlot;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getRenounceType() {
		return renounceType;
	}

	public void setRenounceType(String renounceType) {
		this.renounceType = renounceType;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getRegionalName() {
		return regionalName;
	}

	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}
}