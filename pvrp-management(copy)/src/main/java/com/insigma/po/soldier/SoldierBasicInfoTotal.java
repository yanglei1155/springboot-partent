package com.insigma.po.soldier;

/**
 * 
 * 类描述：   士兵基础数据分析汇总查询
 * 创建人：刘伟民   
 * 创建时间：2020年4月1日 上午11:23:25   
 * @version
 */
public class SoldierBasicInfoTotal{

    private Integer ybId;
     
    private Integer flag;
    
    private Integer pnum;
    
    private String regionalName;
    
    private String regionalCode;

	public Integer getYbId() {
		return ybId;
	}

	public void setYbId(Integer ybId) {
		this.ybId = ybId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getPnum() {
		return pnum;
	}

	public void setPnum(Integer pnum) {
		this.pnum = pnum;
	}

	public String getRegionalName() {
		return regionalName;
	}

	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}

	public String getRegionalCode() {
		return regionalCode;
	}

	public void setRegionalCode(String regionalCode) {
		this.regionalCode = regionalCode;
	}
    
}