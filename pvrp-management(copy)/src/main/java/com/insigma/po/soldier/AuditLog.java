package com.insigma.po.soldier;

/**
 *       
 * 类描述：  审核档案操作日志
 * 创建人：liuwm   
 * 创建时间：2020年3月4日 下午5:28:07   
 * @version
 */
public class AuditLog {
    private Integer alId;

    private Integer sbiId;

    private String auditDept;

    private String auditResult;

    private String operator;

    private String auditTime;

    private String remark;
    
    private String returnRemark;

    
    public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}

	public Integer getAlId() {
        return alId;
    }

    public void setAlId(Integer alId) {
        this.alId = alId;
    }

    public Integer getSbiId() {
        return sbiId;
    }

    public void setSbiId(Integer sbiId) {
        this.sbiId = sbiId;
    }

    public String getAuditDept() {
        return auditDept;
    }

    public void setAuditDept(String auditDept) {
        this.auditDept = auditDept == null ? null : auditDept.trim();
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult == null ? null : auditResult.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}