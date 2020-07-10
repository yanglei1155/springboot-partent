package com.insigma.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.insigma.po.soldier.SoldierBasicInfo;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: caic
 * @Date: 16:15 2019/1/7
 * @Description: 表单元素值拷贝到实体类
 */
public class JavaBeanUtils {
    /***
     * 将map拷贝到实体类
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map map, Class<T> clazz) {
        try {
            T bean = clazz.newInstance();
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object o) {
                    if (o == null) {
                        return null;
                    }
                    if (!(o instanceof String)) {
                        throw new ConversionException("只支持字符串转换 !");
                    }
                    String str = (String) o;
                    if (str.trim().equals("")) {
                        return null;
                    }
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                    try {
                        String pattern = "^[0-9]+$";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(str);
                        if(m.matches()){
                            return Date.from(Instant.ofEpochMilli(Long.parseLong(str)));
                        }else{
                            return sd.parse(str.replace("Z", " UTC"));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, java.util.Date.class);
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 表单数据拷贝到实体类
     * @param pageData
     * @param formName
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T pageElementToBean(JSONObject pageData, String formName, Class<T> clazz){
        JSONObject target = pageData.getJSONObject(formName);
        return pageElementToBean(target,clazz);
    }

    /***
     * 表单数据拷贝到实体类
     * @param pageData
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T pageElementToBean(JSONObject pageData, Class<T> clazz){
        Map map=new HashMap();
        if(pageData!=null){
            for(String s:pageData.keySet()){
            	String value = pageData.getString(s);
                if(value!=null){
                    map.put(s,value);
                }
            }
        }
        return toBean(map,clazz);
    }

    public static void main(String[] args) {
//    	Map<String, String> rmap = new HashMap<String, String>();
//		rmap.put("year", DateUtils.getStringYearDate());
//		rmap.put("num", "1");
////		String msg=	JSONUtils.valueToString(rmap);
//		net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(rmap);
//		System.out.println(jsonobject.toString());
    	
    	SoldierBasicInfo a1 = new SoldierBasicInfo();
    	a1.setIdcard("111");
    	SoldierBasicInfo a2 = new SoldierBasicInfo();
    	a2.setIdcard("222");
    	SoldierBasicInfo b1 = new SoldierBasicInfo();
    	b1.setIdcard("333");
    	SoldierBasicInfo b2 = new SoldierBasicInfo();
    	b2.setIdcard("222");
    	
    	List<SoldierBasicInfo> list1 = new ArrayList<SoldierBasicInfo>();
    	List<SoldierBasicInfo> list2 = new ArrayList<SoldierBasicInfo>();
    	
    	list1.add(a1);
    	list1.add(a2);
    	list2.add(b1);
    	list2.add(b2);
    	
//    	list1.retainAll(list2);
    	list1.removeAll(list2);
    	
    	for (SoldierBasicInfo vo:list1) {
			System.out.println("姓名:" + vo.getIdcard());
		}
    }
    
    /**
     * 实体类转json字符串，处理null值字段消失问题
     * @param obj
     * @return
     */
    public static String beanToJsonStr(Object obj){
    	//SerializerFeature.WriteMapNullValue 设置返回对象中的值为null时会消失问题
    	String jsonStr = JSONObject.toJSONString(obj,SerializerFeature.WriteMapNullValue); 
		return jsonStr;
    }
    
}
