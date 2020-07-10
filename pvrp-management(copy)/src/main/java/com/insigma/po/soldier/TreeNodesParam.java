package com.insigma.po.soldier;

/**
 * 
 * 类描述： 省市县树形统计查询条件入参  
 * 创建人：刘伟民   
 * 创建时间：2020年5月25日 上午11:13:16   
 * @version
 */
public class TreeNodesParam {
	private String queryType;
	private String status;
	private String checkStatus;
	private String ybId;
	private String year;
	private String personnelType;
	private String registerStatus;
	private String regionalCode;
	private String flag;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRegionalCode() {
		return regionalCode;
	}
	public void setRegionalCode(String regionalCode) {
		this.regionalCode = regionalCode;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getYbId() {
		return ybId;
	}
	public void setYbId(String ybId) {
		this.ybId = ybId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPersonnelType() {
		return personnelType;
	}
	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}
	public String getRegisterStatus() {
		return registerStatus;
	}
	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}
}
