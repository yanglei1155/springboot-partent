package com.insigma.po;


public class ResultVo  {
	private int statusCode;
	private String  message;
	private String  list="";
	private String arrList="";
	
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getArrList() {
		return arrList;
	}
	public void setArrList(String arrList) {
		this.arrList = arrList;
	}
	public String toString(){
		if("".equals(this.list)){
			this.list="[]";
		}
		if("".equals(this.arrList)){
			this.arrList="[]";
		}
		return "{\"statusCode\":\""+this.statusCode+"\",\"message\":\""+this.message+"\",\"list\":"+this.list+",\"arrList\":"+this.arrList+"}";
	}
}
