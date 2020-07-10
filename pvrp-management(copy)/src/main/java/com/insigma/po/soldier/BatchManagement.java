package com.insigma.po.soldier;

/**
 *       
 * 类描述： 批次信息  
 * 创建人：liuwm   
 * 创建时间：2020年3月6日 上午11:22:28   
 * @version
 */
public class BatchManagement extends Base{
    private Integer ybId;

    private String batchName;

    private Integer year;

    private Integer status;

    private String operator;

    private String createTime;

    private String receivingFilePath;
    
    
    public String getReceivingFilePath() {
		return receivingFilePath;
	}

	public void setReceivingFilePath(String receivingFilePath) {
		this.receivingFilePath = receivingFilePath;
	}

	public Integer getYbId() {
        return ybId;
    }

    public void setYbId(Integer ybId) {
        this.ybId = ybId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName == null ? null : batchName.trim();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}