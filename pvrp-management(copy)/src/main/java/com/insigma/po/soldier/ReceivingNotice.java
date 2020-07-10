package com.insigma.po.soldier;

public class ReceivingNotice {
    private Integer rnId;

    private Integer year;

    private Integer num;

    private String unitName;

    private String idcard;

    private String name;

    private String startTime;

    private String endTime;

    private String createTime;
    
    private Integer sbiId;
    
    private String regionalName;
    
    private String filePath;
    
    private Integer ybId;
    
    private Integer personnelType;

    private Integer days;
    
    public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(Integer personnelType) {
		this.personnelType = personnelType;
	}

	public Integer getYbId() {
		return ybId;
	}

	public void setYbId(Integer ybId) {
		this.ybId = ybId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public Integer getRnId() {
        return rnId;
    }

    public void setRnId(Integer rnId) {
        this.rnId = rnId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}