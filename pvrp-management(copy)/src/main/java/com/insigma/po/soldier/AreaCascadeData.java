package com.insigma.po.soldier;

import lombok.Data;

import java.util.List;

/**
 * 
 * 类描述：展示地区级联数据
 * 创建人：刘伟民   
 * 创建时间：2020年6月3日 下午2:57:46   
 * @version
 */
@Data
public class AreaCascadeData {
	private Integer pnum;
	    
    private String regionalName;
    
    private String regionalCode;

	List<AreaCascadeData>children;

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
