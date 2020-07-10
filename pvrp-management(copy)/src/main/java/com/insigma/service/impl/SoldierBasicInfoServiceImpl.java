package com.insigma.service.impl;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.insigma.po.ResultVo;
import com.insigma.po.SysOrg;
import com.insigma.po.soldier.*;
import com.insigma.service.SoldierResettlementStisticsServcie;
import com.insigma.service.SysRegionalService;
import com.insigma.service.sysuser.SysOrgService;
import com.insigma.util.NewExcelUtils;
import com.insigma.util.ParamUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.insigma.mapper.SoldierBasicInfoMapper;
import com.insigma.service.SoldierBasicInfoService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SoldierBasicInfoServiceImpl implements SoldierBasicInfoService {
	@Autowired
	private SoldierBasicInfoMapper mapper;

	@Autowired
	private SoldierResettlementStisticsServcie stisticsServcie;

	@Autowired
	private SysRegionalService regionalService;

	@Override
	public int deleteByPrimaryKey(Integer sbiId) {
		return mapper.deleteByPrimaryKey(sbiId);
	}

	@Override
    @Transactional
	public int insert(SoldierBasicInfo record) {
		int ret=0;
		try {
			ret=mapper.insert(record);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

	@Override
	public SoldierBasicInfo selectByPrimaryKey(Integer sbiId) {
		return mapper.selectByPrimaryKey(sbiId);
	}

//	@Override
//	public List<SoldierBasicInfo> selectAll(SoldierBasicInfo record) {
//		return mapper.selectAll(record);
//	}

	@Override
	@Transactional
	public int updateByPrimaryKey(SoldierBasicInfo record) {
		int ret=0;
		try {
			ret=mapper.updateByPrimaryKey(record);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

//	@Override
//	public List<SoldierBasicInfo> selectTroopsName(@Param("troopsName")String troopsName) {
//		return mapper.selectTroopsName(troopsName);
//	}

//	@Override
//	public List<SoldierBasicInfo> selectExpInfo(SoldierBasicInfo record) {
//		return mapper.selectExpInfo(record);
//	}

//	@Override
//	public List<SoldierBasicInfo> queryAuditData(SoldierBasicInfo record) {
//		return mapper.queryAuditData(record);
//	}

//	@Override
//	public List<SoldierBasicInfo> querySporadicAuditData(SoldierBasicInfo record) {
//		return mapper.querySporadicAuditData(record);
//	}

//	@Override
//	public List<SoldierBasicInfo> queryArchivesDetailed(SoldierBasicInfo record) {
//		return mapper.queryArchivesDetailed(record);
//	}

//	@Override
//	public List<SoldierBasicInfo> querySoldierBasicInfo(SoldierBasicInfo record) {
//		return mapper.querySoldierBasicInfo(record);
//	}

//	@Override
//	public List<SoldierBasicInfoTotal> querySoldierBasicInfoTreeNodes(Map<String, String> map) {
//		return mapper.querySoldierBasicInfoTreeNodes(map);
//	}

	@Override
	public List<SoldierBasicInfoStatic> queryArchivesReport(SoldierBasicInfoStatic record) {
		return mapper.queryArchivesReport(record);
	}

	@Override
	public void insertList(List<SoldierBasicInfo> list) {
		try {
			mapper.insertList(list);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException();
		}
	}

//	@Override
//	public void updateList(List<SoldierBasicInfo> list) {
//		try {
//			mapper.updateList(list);
//		} catch (Exception e) {
//			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			throw new RuntimeException();
//		}
//	}

	@Override
	public List<SoldierBasicInfo> queryPrintReceivingNotice(SoldierBasicInfo record) {
		return mapper.queryPrintReceivingNotice(record);
	}

	@Override
	public List<SoldierBasicInfo> queryCheckData(SoldierBasicInfo record) {
		// TODO Auto-generated method stub
		return mapper.queryCheckData(record);
	}

	@Override
	public int updateArchivesAudit(SoldierBasicInfo record) {
		int ret=0;
		try {
			ret=mapper.updateArchivesAudit(record);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

	@Override
	public void updateArchivesList(List<SoldierBasicInfo> list) {
		// TODO Auto-generated method stub
		mapper.updateArchivesList(list);
	}

	@Override
	public List<SoldierBasicInfoTotal> querySoldierBasicInfoTreeNodes(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SoldierBasicInfo> querySoldierList(SoldierBasicInfo po) {
		// TODO Auto-generated method stub
		return mapper.querySoldierList(po);
	}

	@Override
	public SoldierBasicInfo checkSoldierBasicInfo(String idcard) {
		// TODO Auto-generated method stub
		return mapper.checkSoldierBasicInfo(idcard);
	}

	@Override
	public List<AreaCascadeData> queryAreaCascadeData(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryAreaCascadeData(map);
	}

	@Override
	public void updateSql(String sql) {
		// TODO Auto-generated method stub
		mapper.updateSql(sql);
	}

	@Override
	public void updateListSql(List<String> list) {
		// TODO Auto-generated method stub
		mapper.updateListSql(list);
	}

	@Override
	public List<SoldierBasicInfo> getSoldierListByWhere(Map<String, String> map) {
		return mapper.getSoldierListByWhere(map);
	}

	@Override
	public SoldierBasicInfo getCountByWhere(Map<String, String> map) {
		return  mapper.getCountByWhere(map);
	}

	@Override
	public ResultVo readReceiveExcel(String type, MultipartFile file) {
		 ResultVo resultVo=new ResultVo();
		if(type.equals("1")){
		 resultVo = readExcelOfCenter(type,file);
		}
		if (type.equals("2")){
            resultVo=readExcelOfProvinceEnterprise(type,file);
		}
		if(type.equals("3")){
			resultVo=readExcelOfProvinceCause(type,file);
		}
		return  resultVo;
	}
   //读取央企数据
	public ResultVo readExcelOfCenter(String type,MultipartFile file){
		ResultVo resultVo=new ResultVo();
		List<SysRegional> fullNameList = regionalService.getFullNameOfAllArea();
		try {
			String []head={"省份","中央企业","接收单位","中央下达计划数","合计","安置地区","实际接收安置数","推出岗位后退役士兵未选择（计划未使用）"};;
			//获取指定行的全部列数据（这里我们指定第5行获取第5行标题）
			List<String> headerList = NewExcelUtils.getHeader(file,5,-1, 8);
			//判断导入文件的头标题是否正确
			if(!ParamUtils.checkArrayWithList(head,headerList)){
				resultVo.setStatusCode(-1);
				resultVo.setMessage("excel导入标题不正确");
				return  resultVo;
			}
			List<List<String>> excelData = NewExcelUtils.getExcelData(6, 1, 7,0, 5,-1,-1, file,type);
			List<SoldierResettlementStistics>stisticsList=new ArrayList<>();
			for(List<String> list:excelData){
				SoldierResettlementStistics stistics=new SoldierResettlementStistics();
				String regionalName = getFullName(list.get(3), fullNameList);
				stistics.setType(type);
				stistics.setId(ParamUtils.getUUID());
				stistics.setParentUnit(list.get(0));
				stistics.setReceiveUnit(list.get(1));
				stistics.setReleasePlanNum(list.get(2));
				stistics.setResettlementPlace(regionalName);
				stistics.setActualReceiveNum(list.get(4));
				stisticsList.add(stistics);
			}
			stisticsServcie.insertSoldierResettlementStistics(stisticsList);
		}catch (Exception e){
		    e.printStackTrace();
			 resultVo.setStatusCode(-2);
			resultVo.setMessage("excel导入失败");
			return  resultVo;
		}
		resultVo.setStatusCode(200);
		resultVo.setMessage("excel导入成功");
		return  resultVo;
	}
	//读取省企数据
	public ResultVo readExcelOfProvinceEnterprise(String type,MultipartFile file){
		ResultVo resultVo=new ResultVo();
		try {
			String []head={"序号","设区市","省属企业及接收单位名称","杭州","宁波","温州","湖州","嘉兴","绍兴","金华","衢州","舟山","台州","丽水","备注"};
			//获取指定行的全部列数据（这里我们指定第3行获取第3行标题）
			List<String> headerList = NewExcelUtils.getHeader(file,3,5, 15);
			headerList= ParamUtils.distinctList(headerList);
			//判断导入文件的头标题是否正确
			if(!ParamUtils.checkArrayWithList(head,headerList)){
				resultVo.setStatusCode(-1);
				resultVo.setMessage("excel导入标题不正确");
				return  resultVo;
			}
			List<List<String>> excelData = NewExcelUtils.getExcelData(6, 1, 15,2, -1,3,4, file,type);
			List<SoldierResettlementStistics>stisticsList=new ArrayList<>();
			for(List<String> list:excelData){
				SoldierResettlementStistics stistics=new SoldierResettlementStistics();
				stistics.setId(ParamUtils.getUUID());
				stistics.setType(type);
				stistics.setParentUnit(list.get(0));
				stistics.setReceiveUnit(list.get(1));
				stistics.setReleasePlanNum(list.get(2));
				stistics.setResettlementPlace(list.get(3));
				stisticsList.add(stistics);
			}
			stisticsServcie.insertSoldierResettlementStistics(stisticsList);
		}catch (Exception e){
			e.printStackTrace();
			resultVo.setStatusCode(-2);
			resultVo.setMessage("excel导入失败");
			return  resultVo;
		}
		resultVo.setStatusCode(200);
		resultVo.setMessage("excel导入成功");
		return  resultVo;
	}
	//读取省事业数据
	public ResultVo readExcelOfProvinceCause(String type,MultipartFile file){
		ResultVo resultVo=new ResultVo();
		try {
			String []head={"序号","设区市","杭州","宁波","温州","湖州","嘉兴","绍兴","金华","衢州","舟山","台州","丽水","合计"};
			//获取指定行的全部列数据（这里我们指定第2行获取第2行标题）
			List<String> headerList = NewExcelUtils.getHeader(file,2,-1, 14);
			//判断导入文件的头标题是否正确
			if(!ParamUtils.checkArrayWithList(head,headerList)){
				resultVo.setStatusCode(-1);
				resultVo.setMessage("excel导入标题不正确");
				return  resultVo;
			}
			List<List<String>> excelData = NewExcelUtils.getExcelData(3, 1, 15,2, -1,2,3, file,type);
			List<SoldierResettlementStistics>stisticsList=new ArrayList<>();
			for(List<String> list:excelData){
				SoldierResettlementStistics stistics=new SoldierResettlementStistics();
				stistics.setType(type);
				stistics.setId(ParamUtils.getUUID());
				stistics.setReceiveUnit(list.get(0));
				stistics.setReleasePlanNum(list.get(1));
				stistics.setResettlementPlace(list.get(2));
				stisticsList.add(stistics);
			}
			stisticsServcie.insertSoldierResettlementStistics(stisticsList);
		}catch (Exception e){
			e.printStackTrace();
			resultVo.setStatusCode(-2);
			resultVo.setMessage("excel导入失败");
			return  resultVo;
		}
		resultVo.setStatusCode(200);
		resultVo.setMessage("excel导入成功");
		return  resultVo;
	}

	/**
	 * 获取 机构全名
	 * @param short_name 机构名
	 * @param list 浙江省机构全名
	 * @return
	 */
	public String getFullName(String short_name,List<SysRegional>list){
		String regionalName=null;
		for(SysRegional sysRegional:list){
			regionalName=sysRegional.getRegionalName();
			String[] split = regionalName.split(",");
			String province=split[0];
			String city=split[1];
			String area =split[2];
			String province2=province.replace("省","");
			String city2=city.replace("市","");
			String area2=area.replace("区","");
			//去除市本级中的市
			area2=area2.replace("市","");
			String newCity=province+city;
			String newArea=city+area;
			String newAreaWithProvince=province+area;
			String newAreaFullName=province+city+area;
			String newCity2=province2+city2;
			String newArea2=city2+area2;
			String newArea3=province2+area2;
			String newAreaFullName2=province2+city2+area2;
			String place=short_name;
			place=place.replace("省","");
			place=place.replace("市","");
			place=place.replace("区","");
			if(city2.contains(place)){
				regionalName=newCity;
				return  regionalName;
			}
			if(area2.contains(place)){
				regionalName=newAreaFullName;
				return  regionalName;
			}
			if(!area2.contains(place)&&!city2.contains(place)){
				if(newCity2.contains(place)){
					regionalName=newCity;
					return  regionalName;
				}
				if(newArea2.contains(place)){
					regionalName=newAreaFullName;
					return  regionalName;
				}
				if(newArea3.contains(place)){
					regionalName=newAreaFullName;
					return regionalName;
				}
				if(!newArea2.contains(place)&&!newCity2.contains(place)){
					if(newAreaFullName2.contains(place)){
						regionalName=newAreaFullName;
						return  regionalName;
					}
				}
			}else {
                continue;
			}
		}

		return  null;
	}
}
