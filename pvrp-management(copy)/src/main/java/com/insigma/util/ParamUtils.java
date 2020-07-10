package com.insigma.util;

import java.util.*;
import java.util.stream.Collectors;

import com.insigma.dto.ArchivesReportVo;
import com.insigma.po.SysOrg;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SysRegional;

public class ParamUtils {
	/**
	 * 处理查询出的参数列表，根据不同的参数获取到对应的值
	 * @param plist  参数集合
	 * @param groupKey 参数组key
	 * @param paramKey 参数key
	 * @param paramValue 参数value
	 * @return
	 */
	public static List<SysParamInfo> checkList(List<SysParamInfo> plist,String groupKey,String paramKey,String paramValue){
		List<SysParamInfo> pvo = plist.stream().filter(s-> groupKey.equals(s.getGroupKey()) && (paramKey.equals(s.getParamKey())||paramValue.equals(s.getParamValue()))).collect(Collectors.toList());
        return pvo;
    }
	
	/**
	 * 根据传入的行政区划编码或名称从list集合中获取对应的数据
	 * @param plist
	 * @param regionalName
	 * @param regionalCode
	 * @return
	 */
	public static List<SysRegional> getRegional(List<SysRegional> plist,String regionalName,String regionalCode){
		List<SysRegional> pvo = plist.stream().filter(s-> regionalName.equals(s.getRegionalName())||regionalCode.equals(s.getRegionalCode())).collect(Collectors.toList());
        return pvo;
    }
	/**
	 * 去掉list中重复元素
	 * @param list
	 * @return
	 */
   public static List<String>distinctList(List<String>list){
	   List<String> collect = list.stream().distinct().filter(string->!string.isEmpty()).collect(Collectors.toList());
	   return collect;

   }

	/**
	 * 比较 字符串数组值是否和list相同
	 * @param arrs
	 * @param list
	 * @return
	 */
   public static boolean checkArrayWithList(String[]arrs,List<String> list){
    	List<String>arrList=new ArrayList<>();
   	    for(int i=0;i<arrs.length;i++){
   	    	arrList.add(arrs[i]);
		}
   	    return   arrList.stream().sorted().collect(Collectors.joining()).equals(list.stream().sorted().collect(Collectors.joining()));
   }

	/**
	 * 获取无重复字符串
	 * @return
	 */
   public static String getUUID(){
	   String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	   return  uuid;
   }
	public static void main(String[] args) {
		SoldierBasicInfo vo = new SoldierBasicInfo();
		vo.setPhone("123");
		SoldierBasicInfo vo1 = new SoldierBasicInfo();
		vo1.setPhone("1234");
		SoldierBasicInfo vo2 = new SoldierBasicInfo();
		vo2.setPhone("123");
		List<SoldierBasicInfo> list = new ArrayList<SoldierBasicInfo>();
		list.add(vo);
		list.add(vo1);
		list.add(vo2);
//		List<SoldierBasicInfo> pvo = list.stream().co.collect(Collectors.toList());
//		List<SoldierBasicInfo> pvo = list.stream().filter(s-> s.getPhone()).collect(Collectors.toList());
        
//		Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//		System.out.println(collect);
		SoldierBasicInfo newvo = null;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getPhone().equals("123")){
				newvo=list.get(i);
				System.out.println(i+"--"+newvo.getPhone());
			}
			
		}
		
		
//		list.stream().map(e -> {
//            return true ? e.getPhone() : e.getPhone();
//        }).collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
//        .entrySet().stream() // 所有 entry 对应的 Stream
//        .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
//        .map(entry -> entry.getKey()) // 获得 entry 的键（重复元素）对应的 Stream
//        .collect(Collectors.toList()); // 转化为 List
		
//		System.out.println(list);
	}
}
