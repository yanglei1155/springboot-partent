package com.insigma.util;

import java.util.List;
import java.util.regex.Pattern;

import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SysRegional;

public class ValidatorUtils {
	/**  
     * 我国公民的身份证号码特点如下
     * 1.长度18位
     * 2.第1-17号只能为数字
     * 3.第18位只能是数字或者x
     * 4.第7-14位表示特有人的年月日信息
     * 请实现身份证号码合法性判断的函数，函数返回值：
     * 1.如果身份证合法返回0
     * 2.如果身份证长度不合法返回1
     * 3.如果第1-17位含有非数字的字符返回2
     * 4.如果第18位不是数字也不是x返回3
     * 5.如果身份证号的出生日期非法返回4
     */
    public static boolean validator(String id) {
        String str = "[1-9]{2}[0-9]{4}(19|20)[0-9]{2}"
                + "((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))"
                + "[0-9]{3}[0-9x]{1}";
        Pattern pattern = Pattern.compile(str);
        return pattern.matcher(id).matches();
    }
    
    public static void main(String[] args) {
	   System.out.println(validator("43048119900416032"));
    }
    
    /**
     * 修改数据前，校验关键字段值是否有做变更，有变更则返回提示信息
     * @param plist 参数表集合
     * @param srList 行政区划集合
     * @param vo 新数据
     * @param oldVo 原数据
     * @param flag 比对标识 1、比对所有；2、比对部分
     * @return
     */
    public static String validatorData(
    		List<SysParamInfo> plist,List<SysRegional> srList ,
    		SoldierBasicInfo vo,SoldierBasicInfo oldVo,int flag){
    	
    	String validatorMsg="";
		
		List<SysParamInfo> oldList= null;
		List<SysParamInfo> newList= null;
		if(CharUtils.validator(oldVo.getIdcard(), vo.getIdcard())){
			validatorMsg+="原身份证号【"+oldVo.getIdcard()+"】变更为【"+vo.getIdcard()+"】,";
		}
		if(CharUtils.validator(oldVo.getName(), vo.getName())){
			validatorMsg+="原姓名【"+oldVo.getName()+"】变更为【"+vo.getName()+"】,";
		}
		if(CharUtils.validator(oldVo.getSex(), vo.getSex())){
			oldList=  ParamUtils.checkList(plist, "sex",oldVo.getSex(), "");
			newList=  ParamUtils.checkList(plist, "sex",vo.getSex(), "");
			validatorMsg+="原性别【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
		}
		if(CharUtils.validator(oldVo.getBigUnitName(), vo.getBigUnitName())){
			oldList=  ParamUtils.checkList(plist, "bigUnitName",oldVo.getBigUnitName(), "");
			newList=  ParamUtils.checkList(plist, "bigUnitName",vo.getBigUnitName(), "");
			validatorMsg+="原大单位【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
		}
		if(CharUtils.validator(oldVo.getSoldiersMilitaryRank(), vo.getSoldiersMilitaryRank())){
			oldList=  ParamUtils.checkList(plist, "soldMilitRankCode",oldVo.getSoldiersMilitaryRank(), "");
			newList=  ParamUtils.checkList(plist, "soldMilitRankCode",vo.getSoldiersMilitaryRank(), "");
			validatorMsg+="原军(警)衔【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
		}
		if(CharUtils.validator(oldVo.getBirthday(), vo.getBirthday())){
			validatorMsg+="原出生年月【"+oldVo.getBirthday()+"】变更为【"+vo.getBirthday()+"】,";
		}
		if(CharUtils.validator(oldVo.getEnlistedDate(), vo.getEnlistedDate())){
			validatorMsg+="原入伍年月【"+oldVo.getBirthday()+"】变更为【"+vo.getBirthday()+"】,";
		}
		if(CharUtils.validator(oldVo.getMarriageStatus(), vo.getMarriageStatus())){
			oldList=  ParamUtils.checkList(plist, "marriageStatus", oldVo.getMarriageStatus(),"");
			newList=  ParamUtils.checkList(plist, "marriageStatus", vo.getMarriageStatus(), "");
			validatorMsg+="原婚姻状况【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
		}
		
		if(CharUtils.validator(oldVo.getDomicile(), vo.getDomicile())){
			List<SysRegional> oldDomicile=ParamUtils.getRegional(srList, "", oldVo.getDomicile());
			List<SysRegional> newDomicile=ParamUtils.getRegional(srList, "", vo.getDomicile());
			validatorMsg+="原入伍地【"+oldDomicile.get(0).getRegionalName()+"】变更为【"+newDomicile.get(0).getRegionalName()+"】,";
		}
		if(CharUtils.validator(oldVo.getResetPlace(), vo.getResetPlace())){
			List<SysRegional> oldResetPlace=ParamUtils.getRegional(srList, "", oldVo.getResetPlace());
			List<SysRegional> newResetPlace=ParamUtils.getRegional(srList, "", vo.getResetPlace());
			validatorMsg+="原拟安置地【"+oldResetPlace.get(0).getRegionalName()+"】变更为【"+newResetPlace.get(0).getRegionalName()+"】,";
		}
		if(flag==1){
			if(CharUtils.validator(oldVo.getResetCategory(), vo.getResetCategory())){
				oldList=  ParamUtils.checkList(plist, "resetCategory",oldVo.getResetCategory(), "");
				newList=  ParamUtils.checkList(plist, "resetCategory",vo.getResetCategory(), "");
				validatorMsg+="原安置类别【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
			}
		}
//		if(CharUtils.validator(oldVo.getPhone(), vo.getPhone())){
//			validatorMsg+="原手机号码【"+oldVo.getPhone()+"】变更为【"+vo.getPhone()+"】,";
//		}
//		if(flag==1){
//			if(CharUtils.validator(oldVo.getArchivesScore(), vo.getArchivesScore())){
//				validatorMsg+="原退役士兵档案分【"+oldVo.getArchivesScore()+"】变更为【"+vo.getArchivesScore()+"】,";
//			}
//			if(CharUtils.validator(oldVo.getRelocateReasonCode(), vo.getRelocateReasonCode())){
//				oldList=  ParamUtils.checkList(plist, "relocateReasonCode",oldVo.getRelocateReasonCode(), "");
//				newList=  ParamUtils.checkList(plist, "relocateReasonCode",vo.getRelocateReasonCode(), "");
//				validatorMsg+="原易地安置原因【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
//			}
//			if(CharUtils.validator(oldVo.getDisabilityCategoriesCode(), vo.getDisabilityCategoriesCode())){
//				oldList=  ParamUtils.checkList(plist, "disabilityCategoriesCode",oldVo.getDisabilityCategoriesCode(), "");
//				newList=  ParamUtils.checkList(plist, "disabilityCategoriesCode",vo.getDisabilityCategoriesCode(), "");
//				validatorMsg+="原伤残性质【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
//			}
//			if(CharUtils.validator(oldVo.getDisableGradeCode(), vo.getDisableGradeCode())){
//				oldList=  ParamUtils.checkList(plist, "disableGradeCode",oldVo.getDisableGradeCode(), "");
//				newList=  ParamUtils.checkList(plist, "disableGradeCode",vo.getDisableGradeCode(), "");
//				validatorMsg+="原伤残等级【"+oldList.get(0).getParamValue()+"】变更为【"+newList.get(0).getParamValue()+"】,";
//			}
//		}
		
    	return validatorMsg;
    }
}
