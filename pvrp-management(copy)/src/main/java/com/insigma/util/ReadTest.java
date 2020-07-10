package com.insigma.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

import com.insigma.po.soldier.SysRegional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;

public class ReadTest {
	 public static String txt2String(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	    
	    public static void main(String[] args){
	        File file = new File("E:\\epsoft\\退役军人\\二期\\test.txt");
	        String str = txt2String(file);
	        
	        JSONArray array = JSONArray.fromObject(str);
	        for (int i = 0; i < array.size(); i++) {
	        	JSONObject jsonObject = JSONObject.fromObject(array.get(i));
				System.out.println(i+"---"+jsonObject.get("value")+""+jsonObject.get("label")+""+jsonObject.get("children"));
				String regionalcode = jsonObject.get("value").toString();
				String regionalname = jsonObject.get("value").toString();
				
				SysRegional sr = new SysRegional();
				sr.setRegionalCode(regionalcode);
				sr.setRegionalName(regionalname);
				sr.setStatus(1);
				sr.setCreateTime(new Date());
				sr.setModifyTime(new Date());
				sr.setParentId(0);
				
			}
	        
	        
//	        JSONArray json = JSONArray.parseArray(str);
//	        for (int i = 0; i < json.size(); i++) {
//				System.out.println(i+"---"+json.get(i));
//			}
	    }
}
