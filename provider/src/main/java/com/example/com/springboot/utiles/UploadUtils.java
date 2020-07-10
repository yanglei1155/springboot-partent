package com.example.com.springboot.utiles;
import java.util.UUID;

public class UploadUtils {
	public static String getUuIdName(String uploadName) {
		int lastIndexOf = uploadName.lastIndexOf(".");//获得.的索引
		String name=uploadName.substring(lastIndexOf);//截取文件后缀名
		String newName=UUID.randomUUID().toString().replace("-","")+name;//组装新的随机名
		return newName;
	}
	public static String getPath(String uploadName) {
		int code1 = uploadName.hashCode();
		int d1 =code1 & 0xf;//得到一级目录
		int code2= code1>>>4;//向后移四位
		int d2=code2 & 0xf;//得到二级目录
		return "/"+d1+"/"+d2;
	}
	public static void main(String[] args) {
		System.out.println(getUuIdName("a.txt"));
		System.out.println(getPath("a.txt"));
	}
}
