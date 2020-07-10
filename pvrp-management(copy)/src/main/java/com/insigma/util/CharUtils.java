package com.insigma.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SysRegional;

public class CharUtils {
	/**
	 * 替换字符串中的所有汉字以外的字符
	 * @param str
	 * @return
	 */
	public static String strReplaceAll(String str){
		return str.replaceAll("[^(\\u4e00-\\u9fa5)]","");
	}
	/**
	 * 替换字符串中的所有数字以外的字符
	 * @param str
	 * @return
	 */
	public static String numberReplaceAll(String str){
		return str.replaceAll("[^(0-9)]", "");
	}
	
	/**
	 * 替换字符串中的所有的空格
	 * @param str
	 * @return
	 */
	public static String replaceAllNull(String str){
		String newStr = "";
		if(!StringUtils.isEmpty(str)){
			newStr=str.replaceAll("\\s", "");
		}
		return newStr;
	}
	
	
	
	/**
	 * 判断档案材料是否缺失，如传入的值为 材料缺失则返回true
	 * @param materialsStatus
	 * @return
	 */
	public static boolean checkMaterialsStatus(String materialsStatus){
		boolean flag = false;
		if(materialsStatus!=null && materialsStatus!="" && materialsStatus.equals("材料缺失")){
			flag=true;
		}
		return flag;
	}
	
	public static boolean materialsEquery(String materialsStatus){
		boolean flag = false;
		if(!StringUtils.isEmpty(materialsStatus)){
			if(materialsStatus.equals("1")){
				flag=true;
			}
		}
		return flag;
	}
	
	/**
	 * 解析传入的值为是或否 返回1或0
	 * @param str
	 * @return
	 */
	public static String isOrNo(String str){
		String isorno = "0";
		if("是".equals(str)){
			isorno="1";
		}
		return isorno;
	}
	
	public static String getDisplayName(HttpServletRequest request){
		String displayName="";
		try {
			displayName= URLDecoder.decode(request.getHeader("displayName"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return displayName;
	}
	
	/**
	 * 校验值是否有做变更操作,与原值比较不匹配则返回true
	 * @param oldParam
	 * @param newParam
	 * @return
	 */
	public static boolean validator(String oldParam,String newParam){
		boolean flag = false;
		
		if(!StringUtils.isEmpty(oldParam)){
			if(!oldParam.equals(newParam)){
				flag=true;
			}
		}
		return flag;
	}
	
	public static String checkRegional(String pesetPlace){
		String[] area1 = {"上城区","下城区","江干区","拱墅区","西湖区","滨江区","萧山区","余杭区","富阳区","临安区","桐庐县","淳安县","建德市","市本级"};
		String[] area2 = {"海曙区","江北区","北仑区","镇海区","鄞州区","奉化区","象山县","宁海县","余姚市","慈溪市","市本级"};
		String[] area3 = {"鹿城区","龙湾区","瓯海区","洞头区","永嘉县","平阳县","苍南县","文成县","泰顺县","瑞安市","乐清市","龙港市","市本级"};
		String[] area4 = {"南湖区","秀洲区","嘉善县","海盐县","海宁市","平湖市","桐乡市","市本级"};
		String[] area5 = {"吴兴区","南浔区","德清县","长兴县","安吉县","市本级"};
		String[] area6 = {"越城区","柯桥区","上虞区","新昌县","诸暨市","嵊州市","市本级"};
		String[] area7 = {"婺城区","金东区","武义县","浦江县","磐安县","兰溪市","义乌市","东阳市","永康市","市本级"};
		String[] area8 = {"柯城区","衢江区","常山县","开化县","龙游县","江山市","市本级"};
		String[] area9 = {"定海区","普陀区","岱山县","嵊泗县","市本级"};
		String[] area10 = {"椒江区","黄岩区","路桥区","三门县","天台县","仙居县","温岭市","临海市","玉环市","市本级"};
		String[] area11 = {"莲都区","青田县","缙云县","遂昌县","松阳县","云和县","庆元县","景宁畲族自治县","龙泉市","市本级"};
		
		Map<Integer, String[]> map = new HashMap<>();
		map.put(1, area1);
		map.put(2, area2);
		map.put(3, area3);
		map.put(4, area4);
		map.put(5, area5);
		map.put(6, area6);
		map.put(7, area7);
		map.put(8, area8);
		map.put(9, area9);
		map.put(10, area10);
		map.put(11, area11);
		
		String result = "-1";
		if(pesetPlace.startsWith("浙江省")){
			String tmp=pesetPlace.substring(pesetPlace.indexOf("浙江省")+3);
			
			String[] parentArea = {"杭州市","宁波市","温州市","嘉兴市","湖州市","绍兴市","金华市","衢州市","舟山市","台州市","丽水市"};
			for (int i = 0; i < parentArea.length; i++) {
				if(tmp.startsWith(parentArea[i])){
					if(tmp.length()<4){
						result="浙江省"+parentArea[i]+"市本级";
					}else{
						tmp=tmp.substring(tmp.indexOf(parentArea[i])+3,tmp.length()-1);
						String[] area=map.get(i+1);
						for (int j = 0; j < area.length; j++) {
							System.out.println(area[j]+"=="+tmp+area[j].startsWith(tmp));
							if(area[j].startsWith(tmp)){
								result="浙江省"+parentArea[i]+area[j];
								break;
							}
						}
						if("-1".equals(result)){
							result="浙江省"+parentArea[i]+"市本级";
						}
					}
					break;
				}
			}
		}
		return result;
	}
	
	public static String stringToDouble(String score){
		if(!StringUtils.isEmpty(score)){
			double f = Double.valueOf(score.toString());
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1+"";
		}
		return null;
	}
	
	public static void main(String[] args) {
//		System.out.println(checkRegional(strReplaceAll("浙江省杭州市钱塘新区")));
//		System.out.println(replaceAllNull("浙江  省杭州 "));
		
//		DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        decimalFormat.setRoundingMode(RoundingMode.UP); // 进一法
//        double v =71.19;
//        System.out.println(decimalFormat.format(v));  
//		double f = 70.5999999994;
//		BigDecimal b = new BigDecimal(f);
//		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		
//        System.out.println(stringToDouble(""));
//        String test = "";
//		String[] tmp = test.split("\\.");
//		if(tmp.length==2){
//			System.out.println(test);
//		}else if(tmp.length==3){
//			System.out.println(tmp[0]+"."+tmp[1]);
//		}else{
//			System.out.println("--");
//		}
//		String registerDate="2020年5月4日至6月4日";
//		System.out.println(registerDate.replaceAll("[^(0-9)]", "-"));
//		
//		try {
//			System.out.println(digitUppercase("9325468.14"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}  
		
//		Map<String, String> map = new HashMap<>();
//		map.put("a", "a");
//		System.out.println(map.get("b"));
		
//		Date date=new   Date();//取时间
//		System.out.println(date.toString());
//	    Calendar   calendar   =   new   GregorianCalendar(); 
//	    calendar.setTime(date); 
//	    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
//	    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
//	    System.out.println(String.format("%04d", 25));
//		System.out.println( (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1))));
		NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        int num1 = 3;
        
		int num2 = 6;
        String result = nt.format((float) num1 / (float) num2 );
        
		System.out.println("num1和num2的百分比为:" + result + "%");
        
	}
	
	/**
	 * 结果集各参数编码转换成对应的中文名称
	 * @param plist 参数集合
	 * @param srList 行政区划集合
	 * @param list 需要转换的list
	 */
	public static void codeToName(List<SysParamInfo> plist,List<SysRegional> srList,List<SoldierBasicInfo> list){
		List<SysParamInfo> rlist= null;
		List<SysRegional> regional= null;
		for (int i = 0; i < list.size(); i++) {
//			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			if(!StringUtils.isEmpty(list.get(i).getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", list.get(i).getSex() , "");
				if(rlist.size()>0){
					list.get(i).setSex(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getBigUnitName())){
				rlist=  ParamUtils.checkList(plist, "bigUnitName", list.get(i).getBigUnitName() , "");
				if(rlist.size()>0){
					list.get(i).setBigUnitName(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getNation())){
				rlist=  ParamUtils.checkList(plist, "nation", list.get(i).getNation() , "");
				if(rlist.size()>0){
					list.get(i).setNation(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getSoldiersMilitaryRank())){
				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", list.get(i).getSoldiersMilitaryRank() , "");
				if(rlist.size()>0){
					list.get(i).setSoldiersMilitaryRank(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getMarriageStatus())){
				rlist=  ParamUtils.checkList(plist, "marriageStatus", list.get(i).getMarriageStatus() , "");
				if(rlist.size()>0){
					list.get(i).setMarriageStatus(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getResetCategory())){
				rlist=  ParamUtils.checkList(plist, "resetCategory", list.get(i).getResetCategory() , "");
				if(rlist.size()>0){
					list.get(i).setResetCategory(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getDomicile())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getDomicile());
				if(regional.size()>0){
					list.get(i).setDomicile(regional.get(0).getRegionalName());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getResetPlace())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getResetPlace());
				if(regional.size()>0){
					list.get(i).setResetPlace(regional.get(0).getRegionalName());
				}
			}
			if(StringUtils.isEmpty(list.get(i).getNoticeStatus()+"")){
				list.get(i).setNoticeStatus(0);
			}
			if(StringUtils.isEmpty(list.get(i).getRegisterStatus()+"")){
				list.get(i).setRegisterStatus(0);
			}
		}
    }
	
	/** 
     * 处理的最大数字达千万亿位 精确到分 
     * 数字金额转换中文大写金额
     * @return 
     */  
    public static String digitUppercase(String num) throws Exception{  
        String fraction[] = {"角", "分"};  
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };  
        /** 
         *      仟        佰        拾         ' ' 
         ' '    $4        $3        $2         $1 
         万     $8        $7        $6         $5 
         亿     $12       $11       $10        $9 
         */  
        String unit1[] = {"", "拾", "佰", "仟"};//把钱数分成段,每四个一段,实际上得到的是一个二维数组  
        String unit2[] = {"元", "万", "亿","万亿"}; //把钱数分成段,每四个一段,实际上得到的是一个二维数组  
        BigDecimal bigDecimal =  new BigDecimal(num);  
        bigDecimal=bigDecimal.multiply(new BigDecimal(100));  
//        Double bigDecimal = new Double(name*100);     存在精度问题 eg：145296.8  
        String strVal = String.valueOf(bigDecimal.toBigInteger());  
        String head = strVal.substring(0,strVal.length()-2);         //整数部分  
        String end = strVal.substring(strVal.length()-2);              //小数部分  
        String endMoney="";  
        String headMoney = "";  
        if("00".equals(end)){  
            endMoney = "整";  
        }else{  
            if(!end.substring(0,1).equals("0")){  
                endMoney+=digit[Integer.valueOf(end.substring(0,1))]+"角";  
            }else if(end.substring(0,1).equals("0") && !end.substring(1,2).equals("0")){  
                endMoney+= "零";  
            }  
            if(!end.substring(1,2).equals("0")){  
                endMoney+=digit[Integer.valueOf(end.substring(1,2))]+"分";  
            }  
        }  
        char[] chars = head.toCharArray();  
        Map<String,Boolean> map = new HashMap<String,Boolean>();//段位置是否已出现zero  
        boolean zeroKeepFlag = false;//0连续出现标志  
        int vidxtemp = 0;  
        for(int i=0;i<chars.length;i++){  
            int idx = (chars.length-1-i)%4;//段内位置  unit1  
            int vidx = (chars.length-1-i)/4;//段位置 unit2  
            String s = digit[Integer.valueOf(String.valueOf(chars[i]))];  
            if(!"零".equals(s)){  
                headMoney += s +unit1[idx]+unit2[vidx];  
                zeroKeepFlag = false;  
            }else if(i==chars.length-1 || map.get("zero"+vidx)!=null){  
                headMoney += "" ;  
            }else{  
                headMoney += s;  
                zeroKeepFlag = true;  
                map.put("zero"+vidx,true);//该段位已经出现0；  
            }  
            if(vidxtemp!=vidx || i==chars.length-1){  
                headMoney = headMoney.replaceAll(unit2[vidx],"");  
                headMoney+=unit2[vidx];  
            }  
            if(zeroKeepFlag && (chars.length-1-i)%4==0){  
                headMoney = headMoney.replaceAll("零","");  
            }  
        }  
        return headMoney+endMoney;  
    }
    
    
    public static void nullToEmpty(SoldierBasicInfo vo){
        Field[] field = vo.getClass().getDeclaredFields();
        for (int j = 0; j < field.length; j++) {     //遍历所有属性
            String name = field[j].getName();    //获取属性的名字
            //将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            String type = field[j].getGenericType().toString();    //获取属性的类型
            if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                try {
                    Method mGet = vo.getClass().getMethod("get" + name);
                    String value = (String) mGet.invoke(vo);    //调用getter方法获取属性值
                    if (value == null || "".equals(value)) {
                        Method mSet = vo.getClass().getMethod("set" + name, new Class[]{String.class});
                        mSet.invoke(vo, new Object[]{null});
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
