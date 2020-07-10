package com.insigma.po.soldier;

/**
 * 
 * 类描述： 士兵报到管理 
 * 创建人：刘伟民 
 * 创建时间：2020年3月19日 下午4:30:14
 * @version
 */
public class SoldierRegister extends Base{
	private Integer ybId;
	private Integer sbiId;
	private String name;
	private String idcard;
	private String idcardStr;
	private String domicile;
	private String resetPlace;
	private String registerTimeSlot;//应报到时间
	private String registerTime;//报到时间
	private String resetType;//原安置方式
	private String newResetType;//选择安置方式
	private String registerStatus;//报到状态
	private String regionalCode;//所属区县
	private String regionalName;//区县名称
	private String sysDate;//系统时间 ，用于开具放弃待遇告知书中的开具日期
	private String receiveDate;//接收时间 -告知书中的档案接收时间
	private String registerType;//报到类型 1、政府安排工作退役士兵报到  2、自主就业退役士兵报到 
	private String renounceStatus;//放弃安置待遇告知书开具状态
	private String personnelType;
	private String queryType;
	private String areaId;
	private String year;
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getIdcardStr() {
		return idcardStr;
	}
	public void setIdcardStr(String idcardStr) {
		this.idcardStr = idcardStr;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getPersonnelType() {
		return personnelType;
	}
	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}
	public String getRegisterTimeSlot() {
		return registerTimeSlot;
	}
	public void setRegisterTimeSlot(String registerTimeSlot) {
		this.registerTimeSlot = registerTimeSlot;
	}
	public Integer getYbId() {
		return ybId;
	}
	public void setYbId(Integer ybId) {
		this.ybId = ybId;
	}
	public Integer getSbiId() {
		return sbiId;
	}
	public void setSbiId(Integer sbiId) {
		this.sbiId = sbiId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getDomicile() {
		return domicile;
	}
	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}
	public String getResetPlace() {
		return resetPlace;
	}
	public void setResetPlace(String resetPlace) {
		this.resetPlace = resetPlace;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getResetType() {
		return resetType;
	}
	public void setResetType(String resetType) {
		this.resetType = resetType;
	}
	public String getNewResetType() {
		return newResetType;
	}
	public void setNewResetType(String newResetType) {
		this.newResetType = newResetType;
	}
	public String getRegisterStatus() {
		return registerStatus;
	}
	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}
	public String getRegionalCode() {
		return regionalCode;
	}
	public void setRegionalCode(String regionalCode) {
		this.regionalCode = regionalCode;
	}
	public String getRegionalName() {
		return regionalName;
	}
	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}
	public String getSysDate() {
		return sysDate;
	}
	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getRenounceStatus() {
		return renounceStatus;
	}
	public void setRenounceStatus(String renounceStatus) {
		this.renounceStatus = renounceStatus;
	}
	
}
