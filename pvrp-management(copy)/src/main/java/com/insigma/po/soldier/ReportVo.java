package com.insigma.po.soldier;

import java.util.List;

public class ReportVo {
	private String param2;
	private int num;
	private List<SoldierBasicInfo> list;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public List<SoldierBasicInfo> getList() {
		return list;
	}
	public void setList(List<SoldierBasicInfo> list) {
		this.list = list;
	}
}
