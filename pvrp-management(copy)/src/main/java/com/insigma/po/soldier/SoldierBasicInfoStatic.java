package com.insigma.po.soldier;

import javax.persistence.Transient;

/**
 * 
 *       
 * 类描述：  士兵基础数据-单机版档案审核结果数据导出
 * 创建人：liuwm   
 * 创建时间：2020年3月7日 下午3:54:45   
 * @version
 */
public class SoldierBasicInfoStatic extends Base{
	private Integer sbiId;

    private Integer ybId;

    private String idcard;

    private String name;

    private String sex;

    private String nation;

    private String birthday;

    private String troopsName;

    private String bigUnitName;

    private String soldiersMilitaryRank;

    private String enlistedDate;

    private String marriageStatus;

    private String domicile;

    private String resetPlace;

    private String phone;

    private String resetCategory;

    private String retiredDate;

    private String poliAffiCode;

    private String timJoiParty;

    private String eduLevCode;

    private String formerUnit;
    
    private String serviceDuration;

    private String enlistedMaterials1="0";

    private String enlistedMaterials2="0";

    private String enlistedMaterials3="0";

    private String retiredMaterials1="0";

    private String retiredMaterials2="0";

    private String retiredMaterials3="0";

    private String archivesScore;

    private String officersMaterials1="0";

    private String officersMaterials2="0";

    private String officersMaterials3="0";

    private String officersMaterials4="0";

    private String officersMaterials5="0";

    private String officersMaterials6="0";

    private String isHardArea;

    private String hardAreaRemark;

    private String rewardMaterials;

    private String rewardRemark;

    private String punishmentMaterials="0";

    private String punishmentRemark;

    private String relocateReasonMaterials1="0";

    private String relocateReasonMaterials2="0";

    private String relocateReasonMaterials3="0";

    private String relocateReasonCode;

    private String retiredType;

    private String idcardMaterials="0";

    private String marriageMaterials="0";

    private String spouseName;

    private String martyrChildrenMaterials="0";

    private String isMartyrChildren;

    private String disabilityCategoriesMaterials="0";

    private String disabilityCategoriesCode;

    private String disableGradeCode;

    private String archivesAuditRemark;

    private String regionalCode;
    
    private String regionalName;

    private String noticeStatus;

    private String filePath;

    private String dataType;

    private String resetType;

    private String newResetType;

    private String registerStatus;

    private String status;

    private Integer checkStatus;

    private String checkTime;

    private String createTime;

    private String modifyTime;

    private String remark;

    private String operator;

    private String returnRemark;

    private String errRemark;
    
    private String batchName;
    
    private String professional;
    
    private String registerTime;

    private String wheReceSelfSubs;

    private String selfSubsAmount;

    private String wheLivingExpenses;

    private String livingExpensesMonth;

    private String livingExpensesAmout;

    private String unitName;

    private String unitCategory;

    private String recommendationTime;

    private String receivingTime;

    private String postsName;

    private String wheOnTheStaff;

    private String wheMouGuard;

    private String unrecReaExpl;

    private String jobMessage;
    
    private String idcardStr;

	public String getIdcardStr() {
		return idcardStr;
	}

	public void setIdcardStr(String idcardStr) {
		this.idcardStr = idcardStr;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getWheReceSelfSubs() {
		return wheReceSelfSubs;
	}

	public void setWheReceSelfSubs(String wheReceSelfSubs) {
		this.wheReceSelfSubs = wheReceSelfSubs;
	}

	public String getSelfSubsAmount() {
		return selfSubsAmount;
	}

	public void setSelfSubsAmount(String selfSubsAmount) {
		this.selfSubsAmount = selfSubsAmount;
	}

	public String getWheLivingExpenses() {
		return wheLivingExpenses;
	}

	public void setWheLivingExpenses(String wheLivingExpenses) {
		this.wheLivingExpenses = wheLivingExpenses;
	}

	public String getLivingExpensesMonth() {
		return livingExpensesMonth;
	}

	public void setLivingExpensesMonth(String livingExpensesMonth) {
		this.livingExpensesMonth = livingExpensesMonth;
	}

	public String getLivingExpensesAmout() {
		return livingExpensesAmout;
	}

	public void setLivingExpensesAmout(String livingExpensesAmout) {
		this.livingExpensesAmout = livingExpensesAmout;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitCategory() {
		return unitCategory;
	}

	public void setUnitCategory(String unitCategory) {
		this.unitCategory = unitCategory;
	}

	public String getRecommendationTime() {
		return recommendationTime;
	}

	public void setRecommendationTime(String recommendationTime) {
		this.recommendationTime = recommendationTime;
	}

	public String getReceivingTime() {
		return receivingTime;
	}

	public void setReceivingTime(String receivingTime) {
		this.receivingTime = receivingTime;
	}

	public String getPostsName() {
		return postsName;
	}

	public void setPostsName(String postsName) {
		this.postsName = postsName;
	}

	public String getWheOnTheStaff() {
		return wheOnTheStaff;
	}

	public void setWheOnTheStaff(String wheOnTheStaff) {
		this.wheOnTheStaff = wheOnTheStaff;
	}

	public String getWheMouGuard() {
		return wheMouGuard;
	}

	public void setWheMouGuard(String wheMouGuard) {
		this.wheMouGuard = wheMouGuard;
	}

	public String getUnrecReaExpl() {
		return unrecReaExpl;
	}

	public void setUnrecReaExpl(String unrecReaExpl) {
		this.unrecReaExpl = unrecReaExpl;
	}

	public String getJobMessage() {
		return jobMessage;
	}

	public void setJobMessage(String jobMessage) {
		this.jobMessage = jobMessage;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getErrRemark() {
		return errRemark;
	}

	public void setErrRemark(String errRemark) {
		this.errRemark = errRemark;
	}

	public Integer getSbiId() {
		return sbiId;
	}

	public void setSbiId(Integer sbiId) {
		this.sbiId = sbiId;
	}

	public Integer getYbId() {
		return ybId;
	}

	public void setYbId(Integer ybId) {
		this.ybId = ybId;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTroopsName() {
		return troopsName;
	}

	public void setTroopsName(String troopsName) {
		this.troopsName = troopsName;
	}

	public String getBigUnitName() {
		return bigUnitName;
	}

	public void setBigUnitName(String bigUnitName) {
		this.bigUnitName = bigUnitName;
	}

	public String getSoldiersMilitaryRank() {
		return soldiersMilitaryRank;
	}

	public void setSoldiersMilitaryRank(String soldiersMilitaryRank) {
		this.soldiersMilitaryRank = soldiersMilitaryRank;
	}

	public String getEnlistedDate() {
		return enlistedDate;
	}

	public void setEnlistedDate(String enlistedDate) {
		this.enlistedDate = enlistedDate;
	}

	public String getMarriageStatus() {
		return marriageStatus;
	}

	public void setMarriageStatus(String marriageStatus) {
		this.marriageStatus = marriageStatus;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getResetCategory() {
		return resetCategory;
	}

	public void setResetCategory(String resetCategory) {
		this.resetCategory = resetCategory;
	}

	public String getRetiredDate() {
		return retiredDate;
	}

	public void setRetiredDate(String retiredDate) {
		this.retiredDate = retiredDate;
	}

	public String getPoliAffiCode() {
		return poliAffiCode;
	}

	public void setPoliAffiCode(String poliAffiCode) {
		this.poliAffiCode = poliAffiCode;
	}

	public String getTimJoiParty() {
		return timJoiParty;
	}

	public void setTimJoiParty(String timJoiParty) {
		this.timJoiParty = timJoiParty;
	}

	public String getEduLevCode() {
		return eduLevCode;
	}

	public void setEduLevCode(String eduLevCode) {
		this.eduLevCode = eduLevCode;
	}

	public String getFormerUnit() {
		return formerUnit;
	}

	public void setFormerUnit(String formerUnit) {
		this.formerUnit = formerUnit;
	}

	public String getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(String serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	public String getEnlistedMaterials1() {
		return enlistedMaterials1;
	}

	public void setEnlistedMaterials1(String enlistedMaterials1) {
		this.enlistedMaterials1 = enlistedMaterials1;
	}

	public String getEnlistedMaterials2() {
		return enlistedMaterials2;
	}

	public void setEnlistedMaterials2(String enlistedMaterials2) {
		this.enlistedMaterials2 = enlistedMaterials2;
	}

	public String getEnlistedMaterials3() {
		return enlistedMaterials3;
	}

	public void setEnlistedMaterials3(String enlistedMaterials3) {
		this.enlistedMaterials3 = enlistedMaterials3;
	}

	public String getRetiredMaterials1() {
		return retiredMaterials1;
	}

	public void setRetiredMaterials1(String retiredMaterials1) {
		this.retiredMaterials1 = retiredMaterials1;
	}

	public String getRetiredMaterials2() {
		return retiredMaterials2;
	}

	public void setRetiredMaterials2(String retiredMaterials2) {
		this.retiredMaterials2 = retiredMaterials2;
	}

	public String getRetiredMaterials3() {
		return retiredMaterials3;
	}

	public void setRetiredMaterials3(String retiredMaterials3) {
		this.retiredMaterials3 = retiredMaterials3;
	}

	public String getArchivesScore() {
		return archivesScore;
	}

	public void setArchivesScore(String archivesScore) {
		this.archivesScore = archivesScore;
	}

	public String getOfficersMaterials1() {
		return officersMaterials1;
	}

	public void setOfficersMaterials1(String officersMaterials1) {
		this.officersMaterials1 = officersMaterials1;
	}

	public String getOfficersMaterials2() {
		return officersMaterials2;
	}

	public void setOfficersMaterials2(String officersMaterials2) {
		this.officersMaterials2 = officersMaterials2;
	}

	public String getOfficersMaterials3() {
		return officersMaterials3;
	}

	public void setOfficersMaterials3(String officersMaterials3) {
		this.officersMaterials3 = officersMaterials3;
	}

	public String getOfficersMaterials4() {
		return officersMaterials4;
	}

	public void setOfficersMaterials4(String officersMaterials4) {
		this.officersMaterials4 = officersMaterials4;
	}

	public String getOfficersMaterials5() {
		return officersMaterials5;
	}

	public void setOfficersMaterials5(String officersMaterials5) {
		this.officersMaterials5 = officersMaterials5;
	}

	public String getOfficersMaterials6() {
		return officersMaterials6;
	}

	public void setOfficersMaterials6(String officersMaterials6) {
		this.officersMaterials6 = officersMaterials6;
	}

	public String getIsHardArea() {
		return isHardArea;
	}

	public void setIsHardArea(String isHardArea) {
		this.isHardArea = isHardArea;
	}

	public String getHardAreaRemark() {
		return hardAreaRemark;
	}

	public void setHardAreaRemark(String hardAreaRemark) {
		this.hardAreaRemark = hardAreaRemark;
	}

	public String getRewardMaterials() {
		return rewardMaterials;
	}

	public void setRewardMaterials(String rewardMaterials) {
		this.rewardMaterials = rewardMaterials;
	}

	public String getRewardRemark() {
		return rewardRemark;
	}

	public void setRewardRemark(String rewardRemark) {
		this.rewardRemark = rewardRemark;
	}

	public String getPunishmentMaterials() {
		return punishmentMaterials;
	}

	public void setPunishmentMaterials(String punishmentMaterials) {
		this.punishmentMaterials = punishmentMaterials;
	}

	public String getPunishmentRemark() {
		return punishmentRemark;
	}

	public void setPunishmentRemark(String punishmentRemark) {
		this.punishmentRemark = punishmentRemark;
	}

	public String getRelocateReasonMaterials1() {
		return relocateReasonMaterials1;
	}

	public void setRelocateReasonMaterials1(String relocateReasonMaterials1) {
		this.relocateReasonMaterials1 = relocateReasonMaterials1;
	}

	public String getRelocateReasonMaterials2() {
		return relocateReasonMaterials2;
	}

	public void setRelocateReasonMaterials2(String relocateReasonMaterials2) {
		this.relocateReasonMaterials2 = relocateReasonMaterials2;
	}

	public String getRelocateReasonMaterials3() {
		return relocateReasonMaterials3;
	}

	public void setRelocateReasonMaterials3(String relocateReasonMaterials3) {
		this.relocateReasonMaterials3 = relocateReasonMaterials3;
	}

	public String getRelocateReasonCode() {
		return relocateReasonCode;
	}

	public void setRelocateReasonCode(String relocateReasonCode) {
		this.relocateReasonCode = relocateReasonCode;
	}

	public String getRetiredType() {
		return retiredType;
	}

	public void setRetiredType(String retiredType) {
		this.retiredType = retiredType;
	}

	public String getIdcardMaterials() {
		return idcardMaterials;
	}

	public void setIdcardMaterials(String idcardMaterials) {
		this.idcardMaterials = idcardMaterials;
	}

	public String getMarriageMaterials() {
		return marriageMaterials;
	}

	public void setMarriageMaterials(String marriageMaterials) {
		this.marriageMaterials = marriageMaterials;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getMartyrChildrenMaterials() {
		return martyrChildrenMaterials;
	}

	public void setMartyrChildrenMaterials(String martyrChildrenMaterials) {
		this.martyrChildrenMaterials = martyrChildrenMaterials;
	}

	public String getIsMartyrChildren() {
		return isMartyrChildren;
	}

	public void setIsMartyrChildren(String isMartyrChildren) {
		this.isMartyrChildren = isMartyrChildren;
	}

	public String getDisabilityCategoriesMaterials() {
		return disabilityCategoriesMaterials;
	}

	public void setDisabilityCategoriesMaterials(String disabilityCategoriesMaterials) {
		this.disabilityCategoriesMaterials = disabilityCategoriesMaterials;
	}

	public String getDisabilityCategoriesCode() {
		return disabilityCategoriesCode;
	}

	public void setDisabilityCategoriesCode(String disabilityCategoriesCode) {
		this.disabilityCategoriesCode = disabilityCategoriesCode;
	}

	public String getDisableGradeCode() {
		return disableGradeCode;
	}

	public void setDisableGradeCode(String disableGradeCode) {
		this.disableGradeCode = disableGradeCode;
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

	public String getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(String noticeStatus) {
		this.noticeStatus = noticeStatus;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
}