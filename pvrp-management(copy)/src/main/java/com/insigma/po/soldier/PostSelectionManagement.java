package com.insigma.po.soldier;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class PostSelectionManagement  extends  Base{
    private Integer psmId;

    private Integer sbiId;

    private String year;

    private String name;

    private Integer ruiId;

    private String unitName;

    private String wheLetOfIntro;

    private String letOfIntroFilePath;

    private String wheRegistrForm;

    private String registrFormFilePath;

    private String createTime;
    
    private String idcard;
    
    private String archivesScore;
    private String examScore;
    private String totalScore;
    /**
     * 报道截止时间
     */
    private String  reportEndTime;
    /**
     * 接收时间
     */
    private String  receiveTime;
    /**
     * 安置岗位
     */
    private String  resettlementPost;
    /**
     * 是否在编
     * 1 是
     * 0 否
     */
    private String  inProcess;
    /**
     * 未接收原因
     */
    private String  unreceiveReason;
    /**
     * 是否开具告知书
     */
    private String  issueNotification;
    /**
     * 开具路径
     */
    private String  issueFilePath;
    /**
     * 接收状态
     * 1 接收 0未接收
     */
    private String  receiveStatus;
    /**
     * 介绍信开具时间
     */
    private String createIntroletterTime;
    /**
     * 退役类型
     * 1 退役士兵 2退役消防
     */
    @Transient
    private String  personnelType ;
    /**
     * 安置部门
     */
    @Transient
    private String resettlementDepartment="退役军人移交安置处";
    /**
     *拟安置地
     */
     @Transient
     private String resetPlace;
    /**
     *根路径
     */
    @Transient
    private String rootPath;
    /**
     *地区编码
     */
    @Transient
    private String regionalCode;
    /**
     *批次
     */
    @Transient
    private String ybId;

    /**
     * 单位类别
     * @return
     */
    @Transient
    private String  unitCategory;
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

	public Integer getPsmId() {
        return psmId;
    }

    public void setPsmId(Integer psmId) {
        this.psmId = psmId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getRuiId() {
        return ruiId;
    }

    public void setRuiId(Integer ruiId) {
        this.ruiId = ruiId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
    }


    public String getLetOfIntroFilePath() {
        return letOfIntroFilePath;
    }

    public void setLetOfIntroFilePath(String letOfIntroFilePath) {
        this.letOfIntroFilePath = letOfIntroFilePath == null ? null : letOfIntroFilePath.trim();
    }


    public String getWheLetOfIntro() {
		return wheLetOfIntro;
	}

	public void setWheLetOfIntro(String wheLetOfIntro) {
		this.wheLetOfIntro = wheLetOfIntro;
	}

	public String getWheRegistrForm() {
		return wheRegistrForm;
	}

	public void setWheRegistrForm(String wheRegistrForm) {
		this.wheRegistrForm = wheRegistrForm;
	}

	public String getRegistrFormFilePath() {
        return registrFormFilePath;
    }

    public void setRegistrFormFilePath(String registrFormFilePath) {
        this.registrFormFilePath = registrFormFilePath == null ? null : registrFormFilePath.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}