package com.insigma.po.soldier;


/**
 * 
 * 类描述： 士兵成绩管理 
 * 创建人：刘伟民   
 * 创建时间：2020年4月3日 上午10:45:31   
 * @version
 */
public class SoldierScore extends Base{
    private Integer sbiId;

    private Integer ybId;

    private String idcard;

    private String name;

    private String archivesScore;
    private String examScore;
    private String totalScore;
    private String batchName;
    
    private String regionalCode;
    
	public String getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	public String getRegionalCode() {
		return regionalCode;
	}
	public void setRegionalCode(String regionalCode) {
		this.regionalCode = regionalCode;
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
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
    
    
}