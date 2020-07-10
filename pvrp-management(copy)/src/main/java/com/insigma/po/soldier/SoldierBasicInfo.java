package com.insigma.po.soldier;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 类描述： 士兵基础数据  
 * 创建人：刘伟民   
 * 创建时间：2020年4月3日 上午10:45:31   
 * @version
 */
public class SoldierBasicInfo extends Base  {
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

    private String enlistedMaterials1;

    private String enlistedMaterials2;

    private String enlistedMaterials3;

    private String retiredMaterials1;

    private String retiredMaterials2;

    private String retiredMaterials3;

    private String archivesScore;

    private String officersMaterials1;

    private String officersMaterials2;

    private String officersMaterials3;

    private String officersMaterials4;

    private String officersMaterials5;

    private String officersMaterials6;

    private String isHardArea;

    private String hardAreaRemark;

    private String rewardMaterials;

    private String rewardRemark;

    private String punishmentMaterials;

    private String punishmentRemark;

    private String relocateReasonMaterials1;

    private String relocateReasonMaterials2;

    private String relocateReasonMaterials3;

    private String relocateReasonCode;

    private String retiredType;

    private String idcardMaterials;

    private String marriageMaterials;

    private String spouseName;

    private String martyrChildrenMaterials;

    private String isMartyrChildren;

    private String disabilityCategoriesMaterials;

    private String disabilityCategoriesCode;

    private String disableGradeCode;

    private String archivesAuditRemark;

    private String regionalCode;
    
    private String regionalName;

    private Integer noticeStatus;

    private String filePath;

    private Integer dataType;

    private Integer resetType;

    private Integer newResetType;

    private Integer registerStatus;

    private Integer status;

    private Integer checkStatus;

    private String checkTime;

    private String createTime;

    private String modifyTime;

    private String remark;

    @javax.persistence.Transient
    private String paramValue;//战区名
    @Transient
    private Integer count;//数量（根据档案状态筛选）

    private String returnRemark;//退档原因(如档案审核结果为退档可填此项内容)
    
    private String isRelocateReason;
    
    private String validatorMsg;//更新校验信息
    
    private String personnelType;//人员类型 1、退役士兵；2、退出消防兵
    
    private String examScore;

    private String professional;
    
    private Integer wheTransfer;//是否移交 1已移交 2未移交 
    
    private String totalScore;



    private SoldierResetInfo srivo;
    
    public boolean equals(Object obj) {
    	SoldierBasicInfo u = (SoldierBasicInfo)obj;
		return this.getIdcard().equals(u.getIdcard()) ;
	}
    

	public String getTotalScore() {
		return totalScore;
	}


	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}



    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWheTransfer() {
		return wheTransfer;
	}
	public void setWheTransfer(Integer wheTransfer) {
		this.wheTransfer = wheTransfer;
	}
	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getExamScore() {
		return examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public SoldierResetInfo getSrivo() {
		return srivo;
	}

	public void setSrivo(SoldierResetInfo srivo) {
		this.srivo = srivo;
	}

	public String getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}

	public String getValidatorMsg() {
		return validatorMsg;
	}

	public void setValidatorMsg(String validatorMsg) {
		this.validatorMsg = validatorMsg;
	}

	public String getIsRelocateReason() {
		return isRelocateReason;
	}

	public void setIsRelocateReason(String isRelocateReason) {
		this.isRelocateReason = isRelocateReason;
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
        this.idcard = idcard == null ? "" : idcard.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? "" : sex.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? "" : nation.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? "" : birthday.trim();
    }

    public String getTroopsName() {
        return troopsName;
    }

    public void setTroopsName(String troopsName) {
        this.troopsName = troopsName == null ? "" : troopsName.trim();
    }

    public String getBigUnitName() {
        return bigUnitName;
    }

    public void setBigUnitName(String bigUnitName) {
        this.bigUnitName = bigUnitName == null ? "" : bigUnitName.trim();
    }

    public String getSoldiersMilitaryRank() {
        return soldiersMilitaryRank;
    }

    public void setSoldiersMilitaryRank(String soldiersMilitaryRank) {
        this.soldiersMilitaryRank = soldiersMilitaryRank == null ? "" : soldiersMilitaryRank.trim();
    }

    public String getEnlistedDate() {
        return enlistedDate;
    }

    public void setEnlistedDate(String enlistedDate) {
        this.enlistedDate = enlistedDate == null ? "" : enlistedDate.trim();
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus == null ? "" : marriageStatus.trim();
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile == null ? "" : domicile.trim();
    }

    public String getResetPlace() {
        return resetPlace;
    }

    public void setResetPlace(String resetPlace) {
        this.resetPlace = resetPlace == null ? "" : resetPlace.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getResetCategory() {
        return resetCategory;
    }

    public void setResetCategory(String resetCategory) {
        this.resetCategory = resetCategory == null ? "" : resetCategory.trim();
    }

    public String getRetiredDate() {
        return retiredDate;
    }

    public void setRetiredDate(String retiredDate) {
        this.retiredDate = retiredDate == null ? "" : retiredDate.trim();
    }

    public String getPoliAffiCode() {
        return poliAffiCode;
    }

    public void setPoliAffiCode(String poliAffiCode) {
        this.poliAffiCode = poliAffiCode == null ? "" : poliAffiCode.trim();
    }

    public String getTimJoiParty() {
        return timJoiParty;
    }

    public void setTimJoiParty(String timJoiParty) {
        this.timJoiParty = timJoiParty == null ? "" : timJoiParty.trim();
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
        this.formerUnit = formerUnit == null ? "" : formerUnit.trim();
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
        this.enlistedMaterials1 = enlistedMaterials1 == null ? "" : enlistedMaterials1.trim();
    }

    public String getEnlistedMaterials2() {
        return enlistedMaterials2;
    }

    public void setEnlistedMaterials2(String enlistedMaterials2) {
        this.enlistedMaterials2 = enlistedMaterials2 == null ? "" : enlistedMaterials2.trim();
    }

    public String getEnlistedMaterials3() {
        return enlistedMaterials3;
    }

    public void setEnlistedMaterials3(String enlistedMaterials3) {
        this.enlistedMaterials3 = enlistedMaterials3 == null ? "" : enlistedMaterials3.trim();
    }

    public String getRetiredMaterials1() {
        return retiredMaterials1;
    }

    public void setRetiredMaterials1(String retiredMaterials1) {
        this.retiredMaterials1 = retiredMaterials1 == null ? "" : retiredMaterials1.trim();
    }

    public String getRetiredMaterials2() {
        return retiredMaterials2;
    }

    public void setRetiredMaterials2(String retiredMaterials2) {
        this.retiredMaterials2 = retiredMaterials2 == null ? "" : retiredMaterials2.trim();
    }

    public String getRetiredMaterials3() {
        return retiredMaterials3;
    }

    public void setRetiredMaterials3(String retiredMaterials3) {
        this.retiredMaterials3 = retiredMaterials3 == null ? "" : retiredMaterials3.trim();
    }

    public String getArchivesScore() {
        return archivesScore;
    }

    public void setArchivesScore(String archivesScore) {
        this.archivesScore = archivesScore == null ? "" : archivesScore.trim();
    }

    public String getOfficersMaterials1() {
        return officersMaterials1;
    }

    public void setOfficersMaterials1(String officersMaterials1) {
        this.officersMaterials1 = officersMaterials1 == null ? "" : officersMaterials1.trim();
    }

    public String getOfficersMaterials2() {
        return officersMaterials2;
    }

    public void setOfficersMaterials2(String officersMaterials2) {
        this.officersMaterials2 = officersMaterials2 == null ? "" : officersMaterials2.trim();
    }

    public String getOfficersMaterials3() {
        return officersMaterials3;
    }

    public void setOfficersMaterials3(String officersMaterials3) {
        this.officersMaterials3 = officersMaterials3 == null ? "" : officersMaterials3.trim();
    }

    public String getOfficersMaterials4() {
        return officersMaterials4;
    }

    public void setOfficersMaterials4(String officersMaterials4) {
        this.officersMaterials4 = officersMaterials4 == null ? "" : officersMaterials4.trim();
    }

    public String getOfficersMaterials5() {
        return officersMaterials5;
    }

    public void setOfficersMaterials5(String officersMaterials5) {
        this.officersMaterials5 = officersMaterials5 == null ? "" : officersMaterials5.trim();
    }

    public String getOfficersMaterials6() {
        return officersMaterials6;
    }

    public void setOfficersMaterials6(String officersMaterials6) {
        this.officersMaterials6 = officersMaterials6 == null ? "" : officersMaterials6.trim();
    }

    public String getIsHardArea() {
        return isHardArea;
    }

    public void setIsHardArea(String isHardArea) {
        this.isHardArea = isHardArea == null ? "" : isHardArea.trim();
    }

    public String getHardAreaRemark() {
        return hardAreaRemark;
    }

    public void setHardAreaRemark(String hardAreaRemark) {
        this.hardAreaRemark = hardAreaRemark == null ? "" : hardAreaRemark.trim();
    }

    public String getRewardMaterials() {
        return rewardMaterials;
    }

    public void setRewardMaterials(String rewardMaterials) {
        this.rewardMaterials = rewardMaterials == null ? "" : rewardMaterials.trim();
    }

    public String getRewardRemark() {
        return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
        this.rewardRemark = rewardRemark == null ? "" : rewardRemark.trim();
    }

    public String getPunishmentMaterials() {
        return punishmentMaterials;
    }

    public void setPunishmentMaterials(String punishmentMaterials) {
        this.punishmentMaterials = punishmentMaterials == null ? "" : punishmentMaterials.trim();
    }

    public String getPunishmentRemark() {
        return punishmentRemark;
    }

    public void setPunishmentRemark(String punishmentRemark) {
        this.punishmentRemark = punishmentRemark == null ? "" : punishmentRemark.trim();
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
        this.retiredType = retiredType == null ? "" : retiredType.trim();
    }

    public String getIdcardMaterials() {
        return idcardMaterials;
    }

    public void setIdcardMaterials(String idcardMaterials) {
        this.idcardMaterials = idcardMaterials == null ? "" : idcardMaterials.trim();
    }

    public String getMarriageMaterials() {
        return marriageMaterials;
    }

    public void setMarriageMaterials(String marriageMaterials) {
        this.marriageMaterials = marriageMaterials == null ? "" : marriageMaterials.trim();
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName == null ? "" : spouseName.trim();
    }

    public String getMartyrChildrenMaterials() {
        return martyrChildrenMaterials;
    }

    public void setMartyrChildrenMaterials(String martyrChildrenMaterials) {
        this.martyrChildrenMaterials = martyrChildrenMaterials == null ? "" : martyrChildrenMaterials.trim();
    }

    public String getIsMartyrChildren() {
        return isMartyrChildren;
    }

    public void setIsMartyrChildren(String isMartyrChildren) {
        this.isMartyrChildren = isMartyrChildren == null ? "" : isMartyrChildren.trim();
    }

    public String getDisabilityCategoriesMaterials() {
        return disabilityCategoriesMaterials;
    }

    public void setDisabilityCategoriesMaterials(String disabilityCategoriesMaterials) {
        this.disabilityCategoriesMaterials = disabilityCategoriesMaterials == null ? "" : disabilityCategoriesMaterials.trim();
    }

    public String getDisabilityCategoriesCode() {
        return disabilityCategoriesCode;
    }

    public void setDisabilityCategoriesCode(String disabilityCategoriesCode) {
        this.disabilityCategoriesCode = disabilityCategoriesCode == null ? "" : disabilityCategoriesCode.trim();
    }

    public String getDisableGradeCode() {
        return disableGradeCode;
    }

    public void setDisableGradeCode(String disableGradeCode) {
        this.disableGradeCode = disableGradeCode == null ? "" : disableGradeCode.trim();
    }

    public String getArchivesAuditRemark() {
        return archivesAuditRemark;
    }

    public void setArchivesAuditRemark(String archivesAuditRemark) {
        this.archivesAuditRemark = archivesAuditRemark == null ? "" : archivesAuditRemark.trim();
    }

    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode == null ? "" : regionalCode.trim();
    }

    public String getRegionalName() {
		return regionalName;
	}

	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}

	public Integer getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? "" : filePath.trim();
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getResetType() {
        return resetType;
    }

    public void setResetType(Integer resetType) {
        this.resetType = resetType;
    }

    public Integer getNewResetType() {
		return newResetType;
	}

	public void setNewResetType(Integer newResetType) {
		this.newResetType = newResetType;
	}


	public Integer getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(Integer registerStatus) {
		this.registerStatus = registerStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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
        this.remark = remark == null ? "" : remark.trim();
    }

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
}