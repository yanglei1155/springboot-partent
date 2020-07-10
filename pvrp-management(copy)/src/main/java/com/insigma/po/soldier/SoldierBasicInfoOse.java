package com.insigma.po.soldier;

import java.util.Date;

/**
 * 类描述：  自主就业士兵基础信息
 * 创建人：刘伟民   
 * 创建时间：2020年6月4日 下午4:42:36   
 * @version
 */
public class SoldierBasicInfoOse extends Base{
    private Integer sbiId;

    private String year;

    private String idcard;

    private String name;

    private String sex;

    private String nation;

    private String birthday;

    private String bigUnitName;

    private String soldiersMilitaryRank;

    private String enlistedDate;

    private String retiredDate;

    private String marriageStatus;

    private String domicile;

    private String resetPlace;

    private String phone;

    private String poliAffiCode;

    private String timJoiParty;

    private String eduLevCode;

    private String formerUnit;

    private String serviceDuration;

    private String isHardArea;

    private String hardAreaRemark;

    private String rewardRemark;

    private String punishmentRemark;

    private String spouseName;

    private String disabilityCategoriesCode;

    private String disableGradeCode;

    private String relocateReasonCode;

    private String jobMessage;

    private String regionalCode;

    private String newResetType;

    private String registerStatus;

    private String createTime;

    private String modifyTime;

    private String remark;

    private String regionalName;
    
    private String personnelType;
    
    private String professional;

    private String filePath;
    
    private String idcardStr;
    
    
    
	public String getIdcardStr() {
		return idcardStr;
	}

	public void setIdcardStr(String idcardStr) {
		this.idcardStr = idcardStr;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}

	public String getRegionalName() {
		return regionalName;
	}

	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}

	public Integer getSbiId() {
        return sbiId;
    }

    public void setSbiId(Integer sbiId) {
        this.sbiId = sbiId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getBigUnitName() {
        return bigUnitName;
    }

    public void setBigUnitName(String bigUnitName) {
        this.bigUnitName = bigUnitName == null ? null : bigUnitName.trim();
    }

    public String getSoldiersMilitaryRank() {
        return soldiersMilitaryRank;
    }

    public void setSoldiersMilitaryRank(String soldiersMilitaryRank) {
        this.soldiersMilitaryRank = soldiersMilitaryRank == null ? null : soldiersMilitaryRank.trim();
    }

    public String getEnlistedDate() {
        return enlistedDate;
    }

    public void setEnlistedDate(String enlistedDate) {
        this.enlistedDate = enlistedDate == null ? null : enlistedDate.trim();
    }

    public String getRetiredDate() {
        return retiredDate;
    }

    public void setRetiredDate(String retiredDate) {
        this.retiredDate = retiredDate == null ? null : retiredDate.trim();
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus == null ? null : marriageStatus.trim();
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile == null ? null : domicile.trim();
    }

    public String getResetPlace() {
        return resetPlace;
    }

    public void setResetPlace(String resetPlace) {
        this.resetPlace = resetPlace == null ? null : resetPlace.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPoliAffiCode() {
        return poliAffiCode;
    }

    public void setPoliAffiCode(String poliAffiCode) {
        this.poliAffiCode = poliAffiCode == null ? null : poliAffiCode.trim();
    }

    public String getTimJoiParty() {
        return timJoiParty;
    }

    public void setTimJoiParty(String timJoiParty) {
        this.timJoiParty = timJoiParty == null ? null : timJoiParty.trim();
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
        this.formerUnit = formerUnit == null ? null : formerUnit.trim();
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration == null ? null : serviceDuration.trim();
    }

    public String getIsHardArea() {
        return isHardArea;
    }

    public void setIsHardArea(String isHardArea) {
        this.isHardArea = isHardArea == null ? null : isHardArea.trim();
    }

    public String getHardAreaRemark() {
        return hardAreaRemark;
    }

    public void setHardAreaRemark(String hardAreaRemark) {
        this.hardAreaRemark = hardAreaRemark == null ? null : hardAreaRemark.trim();
    }

    public String getRewardRemark() {
        return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
        this.rewardRemark = rewardRemark == null ? null : rewardRemark.trim();
    }

    public String getPunishmentRemark() {
        return punishmentRemark;
    }

    public void setPunishmentRemark(String punishmentRemark) {
        this.punishmentRemark = punishmentRemark == null ? null : punishmentRemark.trim();
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName == null ? null : spouseName.trim();
    }

    public String getDisabilityCategoriesCode() {
        return disabilityCategoriesCode;
    }

    public void setDisabilityCategoriesCode(String disabilityCategoriesCode) {
        this.disabilityCategoriesCode = disabilityCategoriesCode == null ? null : disabilityCategoriesCode.trim();
    }

    public String getDisableGradeCode() {
        return disableGradeCode;
    }

    public void setDisableGradeCode(String disableGradeCode) {
        this.disableGradeCode = disableGradeCode == null ? null : disableGradeCode.trim();
    }

    public String getRelocateReasonCode() {
        return relocateReasonCode;
    }

    public void setRelocateReasonCode(String relocateReasonCode) {
        this.relocateReasonCode = relocateReasonCode == null ? null : relocateReasonCode.trim();
    }

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage == null ? null : jobMessage.trim();
    }

    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode == null ? null : regionalCode.trim();
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
        this.remark = remark == null ? null : remark.trim();
    }
}