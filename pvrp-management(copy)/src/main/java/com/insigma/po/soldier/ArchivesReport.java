package com.insigma.po.soldier;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivesReport {
	private Integer rowno;
	private String year;
	private Integer paramKey;
	private String paramValue;
	private String resetCategory;
	private Integer hzcount;
	private Integer nbcount;
	private Integer wzcount;
	private Integer jxcount;
	private Integer huzcount;
	private Integer sxcount;
	private Integer jhcount;
	private Integer qzcount;
	private Integer zscount;
	private Integer tzcount;
	private Integer lscount;
	private Integer total;
	//子节点信息 如父节点为浙江子节点就杭州等子节点
	private List<ArchivesReport> children;
	private Integer returnReceivePlaceCount;//退档统计
	private Integer actualReceptionCount;//实际接收
	private Integer notReceptionCount;//未接收
	private Integer planReceptionCount;//预计接收
	private Integer totalReturnReceiveCount;//省内退档统计
	private Integer totalActualReceptionCount;//省内实际接收
	private Integer totalNotReceptionCount;//省内未接收
	private Integer totalPlanReceptionCount;//省内预计接收
	private List<SoldierBasicInfo>returnOPlace;//退档详细信息存取
	private List<SoldierBasicInfo>cityStatistics;//市统计
	private String relocateReason;//易地安置
	private String areaName;//地区名称
	private String regionalCode;
	private String regionalName;
	private Integer materials1;
	private Integer materials2;
	private Integer materials3;
	private Integer materials4;
	private Integer materials5;
	private Integer materials6;
	private Integer materials7;
	private Integer materials8;
	private Integer materials9;
	private Integer materials10;
	private Integer materials11;
	private Integer materials12;
	private Integer materials13;
	private Integer materials14;
	private Integer materials15;
	private Integer materials16;
	private Integer materials17;
	private Integer materials18;
	private Integer materials19;
	private Integer materials20;
	private Integer materials21;
	private Integer materials22;
	private Integer auditRemarkNum;
	private Integer remarkNum;
	
	private String name;
	private String bigUnitName;
	private String archivesAuditRemark;
	
	private Integer isHardAreaCount;//艰边地区人次
	private Integer rewardCount;//立功及表彰人次
	private Integer punishmentCount;//处分人次
	
	private String idcard;
	private String archivesScore;
	private String  examScore;
	private String  totalScore;
	private String status;
	
	private String remark;
	
	private Integer count1;
	private Integer count2;
	private Integer count3;
	private Integer count4;
	private Integer count5;
	private Integer count6;
	private Integer count7;
	private Integer count8;
	private Integer count9;
	private Integer count10;
	private Integer count11;
	
	private Integer total1;
	private Integer total2;
	private Integer total3;
	
	private String proportion1;
	private String proportion2;
	private String proportion3;

	public Integer getTotalReturnReceiveCount() {
		return totalReturnReceiveCount;
	}

	public void setTotalReturnReceiveCount(Integer totalReturnReceiveCount) {
		this.totalReturnReceiveCount = totalReturnReceiveCount;
	}

	public Integer getTotalActualReceptionCount() {
		return totalActualReceptionCount;
	}

	public void setTotalActualReceptionCount(Integer totalActualReceptionCount) {
		this.totalActualReceptionCount = totalActualReceptionCount;
	}

	public Integer getTotalNotReceptionCount() {
		return totalNotReceptionCount;
	}

	public void setTotalNotReceptionCount(Integer totalNotReceptionCount) {
		this.totalNotReceptionCount = totalNotReceptionCount;
	}

	public Integer getTotalPlanReceptionCount() {
		return totalPlanReceptionCount;
	}

	public void setTotalPlanReceptionCount(Integer totalPlanReceptionCount) {
		this.totalPlanReceptionCount = totalPlanReceptionCount;
	}

	public List<SoldierBasicInfo> getCityStatistics() {
		return cityStatistics;
	}

	public void setCityStatistics(List<SoldierBasicInfo> cityStatistics) {
		this.cityStatistics = cityStatistics;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getReturnReceivePlaceCount() {
		return returnReceivePlaceCount;
	}

	public void setReturnReceivePlaceCount(Integer returnReceivePlaceCount) {
		this.returnReceivePlaceCount = returnReceivePlaceCount;
	}

	public Integer getActualReceptionCount() {
		return actualReceptionCount;
	}

	public void setActualReceptionCount(Integer actualReceptionCount) {
		this.actualReceptionCount = actualReceptionCount;
	}

	public Integer getNotReceptionCount() {
		return notReceptionCount;
	}

	public void setNotReceptionCount(Integer notReceptionCount) {
		this.notReceptionCount = notReceptionCount;
	}

	public Integer getPlanReceptionCount() {
		return planReceptionCount;
	}

	public void setPlanReceptionCount(Integer planReceptionCount) {
		this.planReceptionCount = planReceptionCount;
	}

	public List<SoldierBasicInfo> getReturnOPlace() {
		return returnOPlace;
	}

	public void setReturnOPlace(List<SoldierBasicInfo> returnOPlace) {
		this.returnOPlace = returnOPlace;
	}

	public Integer getTotal1() {
		return total1;
	}
	public void setTotal1(Integer total1) {
		this.total1 = total1;
	}
	public Integer getTotal2() {
		return total2;
	}
	public void setTotal2(Integer total2) {
		this.total2 = total2;
	}
	public Integer getTotal3() {
		return total3;
	}
	public void setTotal3(Integer total3) {
		this.total3 = total3;
	}
	public String getProportion1() {
		return proportion1;
	}
	public void setProportion1(String proportion1) {
		this.proportion1 = proportion1;
	}
	public String getProportion2() {
		return proportion2;
	}
	public void setProportion2(String proportion2) {
		this.proportion2 = proportion2;
	}
	public String getProportion3() {
		return proportion3;
	}
	public void setProportion3(String proportion3) {
		this.proportion3 = proportion3;
	}
	public Integer getCount1() {
		return count1;
	}
	public void setCount1(Integer count1) {
		this.count1 = count1;
	}
	public Integer getCount2() {
		return count2;
	}
	public void setCount2(Integer count2) {
		this.count2 = count2;
	}
	public Integer getCount3() {
		return count3;
	}
	public void setCount3(Integer count3) {
		this.count3 = count3;
	}
	public Integer getCount4() {
		return count4;
	}
	public void setCount4(Integer count4) {
		this.count4 = count4;
	}
	public Integer getCount5() {
		return count5;
	}
	public void setCount5(Integer count5) {
		this.count5 = count5;
	}
	public Integer getCount6() {
		return count6;
	}
	public void setCount6(Integer count6) {
		this.count6 = count6;
	}
	public Integer getCount7() {
		return count7;
	}
	public void setCount7(Integer count7) {
		this.count7 = count7;
	}
	public Integer getCount8() {
		return count8;
	}
	public void setCount8(Integer count8) {
		this.count8 = count8;
	}
	public Integer getCount9() {
		return count9;
	}
	public void setCount9(Integer count9) {
		this.count9 = count9;
	}
	public Integer getCount10() {
		return count10;
	}
	public void setCount10(Integer count10) {
		this.count10 = count10;
	}
	public Integer getCount11() {
		return count11;
	}
	public void setCount11(Integer count11) {
		this.count11 = count11;
	}
	public Integer getAuditRemarkNum() {
		return auditRemarkNum;
	}
	public void setAuditRemarkNum(Integer auditRemarkNum) {
		this.auditRemarkNum = auditRemarkNum;
	}
	public Integer getRemarkNum() {
		return remarkNum;
	}
	public void setRemarkNum(Integer remarkNum) {
		this.remarkNum = remarkNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getArchivesScore() {
		return archivesScore;
	}
	public void setArchivesScore(String archivesScore) {
		this.archivesScore = archivesScore;
	}
	public String getExamScore() {
		return examScore;
	}
	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}
	public String getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getIsHardAreaCount() {
		return isHardAreaCount;
	}
	public void setIsHardAreaCount(Integer isHardAreaCount) {
		this.isHardAreaCount = isHardAreaCount;
	}
	public Integer getRewardCount() {
		return rewardCount;
	}
	public void setRewardCount(Integer rewardCount) {
		this.rewardCount = rewardCount;
	}
	public Integer getPunishmentCount() {
		return punishmentCount;
	}
	public void setPunishmentCount(Integer punishmentCount) {
		this.punishmentCount = punishmentCount;
	}
	public Integer getRowno() {
		return rowno;
	}
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBigUnitName() {
		return bigUnitName;
	}
	public void setBigUnitName(String bigUnitName) {
		this.bigUnitName = bigUnitName;
	}
	public String getArchivesAuditRemark() {
		return archivesAuditRemark;
	}
	public void setArchivesAuditRemark(String archivesAuditRemark) {
		this.archivesAuditRemark = archivesAuditRemark;
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
	public Integer getMaterials1() {
		return materials1;
	}
	public void setMaterials1(Integer materials1) {
		this.materials1 = materials1;
	}
	public Integer getMaterials2() {
		return materials2;
	}
	public void setMaterials2(Integer materials2) {
		this.materials2 = materials2;
	}
	public Integer getMaterials3() {
		return materials3;
	}
	public void setMaterials3(Integer materials3) {
		this.materials3 = materials3;
	}
	public Integer getMaterials4() {
		return materials4;
	}
	public void setMaterials4(Integer materials4) {
		this.materials4 = materials4;
	}
	public Integer getMaterials5() {
		return materials5;
	}
	public void setMaterials5(Integer materials5) {
		this.materials5 = materials5;
	}
	public Integer getMaterials6() {
		return materials6;
	}
	public void setMaterials6(Integer materials6) {
		this.materials6 = materials6;
	}
	public Integer getMaterials7() {
		return materials7;
	}
	public void setMaterials7(Integer materials7) {
		this.materials7 = materials7;
	}
	public Integer getMaterials8() {
		return materials8;
	}
	public void setMaterials8(Integer materials8) {
		this.materials8 = materials8;
	}
	public Integer getMaterials9() {
		return materials9;
	}
	public void setMaterials9(Integer materials9) {
		this.materials9 = materials9;
	}
	public Integer getMaterials10() {
		return materials10;
	}
	public void setMaterials10(Integer materials10) {
		this.materials10 = materials10;
	}
	public Integer getMaterials11() {
		return materials11;
	}
	public void setMaterials11(Integer materials11) {
		this.materials11 = materials11;
	}
	public Integer getMaterials12() {
		return materials12;
	}
	public void setMaterials12(Integer materials12) {
		this.materials12 = materials12;
	}
	public Integer getMaterials13() {
		return materials13;
	}
	public void setMaterials13(Integer materials13) {
		this.materials13 = materials13;
	}
	public Integer getMaterials14() {
		return materials14;
	}
	public void setMaterials14(Integer materials14) {
		this.materials14 = materials14;
	}
	public Integer getMaterials15() {
		return materials15;
	}
	public void setMaterials15(Integer materials15) {
		this.materials15 = materials15;
	}
	public Integer getMaterials16() {
		return materials16;
	}
	public void setMaterials16(Integer materials16) {
		this.materials16 = materials16;
	}
	public Integer getMaterials17() {
		return materials17;
	}
	public void setMaterials17(Integer materials17) {
		this.materials17 = materials17;
	}
	public Integer getMaterials18() {
		return materials18;
	}
	public void setMaterials18(Integer materials18) {
		this.materials18 = materials18;
	}
	public Integer getMaterials19() {
		return materials19;
	}
	public void setMaterials19(Integer materials19) {
		this.materials19 = materials19;
	}
	public Integer getMaterials20() {
		return materials20;
	}
	public void setMaterials20(Integer materials20) {
		this.materials20 = materials20;
	}
	public Integer getMaterials21() {
		return materials21;
	}
	public void setMaterials21(Integer materials21) {
		this.materials21 = materials21;
	}
	public Integer getMaterials22() {
		return materials22;
	}
	public void setMaterials22(Integer materials22) {
		this.materials22 = materials22;
	}
	public String getRelocateReason() {
		return relocateReason;
	}
	public void setRelocateReason(String relocateReason) {
		this.relocateReason = relocateReason;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getParamKey() {
		return paramKey;
	}
	public void setParamKey(Integer paramKey) {
		this.paramKey = paramKey;
	}
	public String getResetCategory() {
		return resetCategory;
	}
	public void setResetCategory(String resetCategory) {
		this.resetCategory = resetCategory;
	}
	public Integer getHzcount() {
		return hzcount;
	}
	public void setHzcount(Integer hzcount) {
		this.hzcount = hzcount;
	}
	public Integer getNbcount() {
		return nbcount;
	}
	public void setNbcount(Integer nbcount) {
		this.nbcount = nbcount;
	}
	public Integer getWzcount() {
		return wzcount;
	}
	public void setWzcount(Integer wzcount) {
		this.wzcount = wzcount;
	}
	public Integer getJxcount() {
		return jxcount;
	}
	public void setJxcount(Integer jxcount) {
		this.jxcount = jxcount;
	}
	public Integer getHuzcount() {
		return huzcount;
	}
	public void setHuzcount(Integer huzcount) {
		this.huzcount = huzcount;
	}
	public Integer getSxcount() {
		return sxcount;
	}
	public void setSxcount(Integer sxcount) {
		this.sxcount = sxcount;
	}
	public Integer getJhcount() {
		return jhcount;
	}
	public void setJhcount(Integer jhcount) {
		this.jhcount = jhcount;
	}
	public Integer getQzcount() {
		return qzcount;
	}
	public void setQzcount(Integer qzcount) {
		this.qzcount = qzcount;
	}
	public Integer getZscount() {
		return zscount;
	}
	public void setZscount(Integer zscount) {
		this.zscount = zscount;
	}
	public Integer getTzcount() {
		return tzcount;
	}
	public void setTzcount(Integer tzcount) {
		this.tzcount = tzcount;
	}
	public Integer getLscount() {
		return lscount;
	}
	public void setLscount(Integer lscount) {
		this.lscount = lscount;
	}
}
