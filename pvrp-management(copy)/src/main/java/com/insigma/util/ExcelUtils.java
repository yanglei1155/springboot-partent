package com.insigma.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import tk.mybatis.mapper.util.StringUtil;

public class ExcelUtils {
	/**
	 * 
	 * @param file
	 * @param ignoreRows 第几行开始读取
	 * @return
	 */
	public List<String[][]> getExcelData(MultipartFile file,int ignoreRows) {
		List<String[][]> list= new ArrayList<>();;
		try {
			list = readExcelToArrayList(file.getOriginalFilename(), file.getInputStream(), ignoreRows);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	} 
	
	private static List<String[][]> readExcelToArrayList(String fileName, 
			InputStream inputStream, int ignoreRows) throws IOException {
		if (isExcel2003(fileName)) {
			return readExcel2003ToArrayList(inputStream, ignoreRows);
		}
		if (isExcel2007(fileName)) {
			return readExcel2007ToArrayList(inputStream, ignoreRows);
		}
		return null;
	}
	public static boolean isExcel2003(String fileName) {
		return fileName.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isExcel2007(String fileName) {
		return fileName.matches("^.+\\.(?i)(xlsx)$");
	}
	
	private static List<String[][]> readExcel2003ToArrayList(InputStream inputStream, int ignoreRows)
			throws IOException {
		List<String[][]> result = new ArrayList<>();

		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		HSSFCell cell = null;
		workbook.getNumberOfSheets();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			if (ignoreRows > sheet.getLastRowNum()) {
				ignoreRows = sheet.getLastRowNum();
			}
			int newRowNum = sheet.getLastRowNum() + 1 - ignoreRows;
			String[][] rowResult = new String[newRowNum][1];
			int newRowIndex = 0;
			for (int rownumber = ignoreRows; rownumber <= sheet.getLastRowNum(); rownumber++) {
				HSSFRow row = sheet.getRow(rownumber);
				if (row != null){
					cell = row.getCell(0);
					if (cell != null) {
						String[] colResult = new String[row.getLastCellNum()];
						for (int colnumber = 0; colnumber < row.getLastCellNum(); colnumber++) {
							String value = "";
							cell = row.getCell(colnumber);

							if (cell != null) {
								switch (cell.getCellType()) {
								case 1:
									value = cell.getStringCellValue();
									break;
								case 0:
									if (HSSFDateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										if (date != null) {
											value = new SimpleDateFormat("yyyy-MM-dd").format(date);
										} else {
											value = "";
										}
									} else {
//										value = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toString();
										cell.setCellType(Cell.CELL_TYPE_STRING);
			                            // 防止把1 取成1.0
			                            value = cell.getStringCellValue().trim();
									}
									break;

								case 2:
									if (!"".equals(cell.getStringCellValue())) {
										value = cell.getStringCellValue().trim();
									} else {
										value = String.valueOf(cell.getNumericCellValue()).trim();
									}
									break;
								case 3:
									break;
								case 5:
									value = "";
									break;
								case 4:
									value = cell.getBooleanCellValue() ? "Y" : "N";
									break;
								default:
									value = "";
								}
							}
							if (colnumber == 0) {
								"".equals(value.trim());
							}
							colResult[colnumber] = rightTrim(value);
						}
						rowResult[newRowIndex] = colResult;
						newRowIndex++;
					}
				}
			}
			if (rowResult != null) {
				result.add(rowResult);
			}
		}
		inputStream.close();
		return result;
	}
	
	
	private static List<String[][]> readExcel2007ToArrayList(InputStream inputStream, int ignoreRows)
			throws IOException {
		List<String[][]> result = new ArrayList<>();

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFCell cell = null;
		workbook.getNumberOfSheets();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (ignoreRows > sheet.getLastRowNum()) {
				ignoreRows = sheet.getLastRowNum();
			}
			int newRowNum = sheet.getLastRowNum() + 1 - ignoreRows;
			String[][] rowResult = new String[newRowNum][1];
			int newRowIndex = 0;

			for (int rownumber = ignoreRows; rownumber <= sheet.getLastRowNum(); rownumber++) {
				XSSFRow row = sheet.getRow(rownumber);
				try {
					if (row != null){
						String[] colResult = new String[row.getLastCellNum()];
						for (int colnumber = 0; colnumber < row.getLastCellNum(); colnumber++) {
							String value = "";
							if(colnumber == 0) {
								cell = row.getCell(colnumber);
								cell.setCellType(Cell.CELL_TYPE_STRING);
								String val = cell.getStringCellValue();
								if(StringUtil.isEmpty(val)) {
									continue;
								}
							}
							cell = row.getCell(colnumber);
							if (cell != null) {
								switch (cell.getCellType()) {
								case 1:
									value = cell.getStringCellValue();
									break;
								case 0:
									if (HSSFDateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										if (date != null) {
											value = new SimpleDateFormat("yyyy-MM-dd").format(date);
										} else {
											value = "";
										}
									} else {
//										value = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toString();
										cell.setCellType(Cell.CELL_TYPE_STRING);
			                            // 防止把1 取成1.0
			                            value = cell.getStringCellValue().trim();
									}
									break;
								case 2:
									if (!"".equals(cell.getStringCellValue())) {
										value = cell.getStringCellValue();
									} else {
										value = String.valueOf(cell.getNumericCellValue());
									}
									break;
								case 3:
									break;
								case 5:
									value = "";
									break;
								case 4:
									value = cell.getBooleanCellValue() ? "Y" : "N";
									break;
								default:
									value = "";
								}
							}
							if (colnumber == 0) {
								"".equals(value.trim());
							}
							colResult[colnumber] = rightTrim(value);
						}
						rowResult[newRowIndex] = colResult;
						newRowIndex++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(row);
				}
			}
			if (rowResult != null) {
				result.add(rowResult);
			}
		}
		inputStream.close();
		return result;
	}
	
	private static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != ' ') {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}
	
	
	
//	public static void exportExcel(HttpServletResponse response, String fileName, ExcelData data) throws Exception {
//        // 告诉浏览器用什么软件可以打开此文件
//        response.setHeader("content-Type", "application/vnd.ms-excel");
//        // 下载文件的默认名称
//        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
//        exportExcel(data, response.getOutputStream());
//    }
	
	/**
	 * 导出excel表
	 * @param request
	 * @param response
	 * @param fileName
	 * @param workbook
	 * @throws IOException
	 */
	public static void responseDownloadWorkbook(HttpServletRequest request, HttpServletResponse response, String fileName, Workbook workbook) throws IOException {
//        String agent = request.getHeader("User-Agent").toUpperCase();

//        String encodedfileName = "";
//        if (agent.indexOf("MSIE") != -1 || agent.indexOf("TRIDENT") != -1) { // IE
//            encodedfileName = URLEncoder.encode(fileName, "utf-8");
//        } else if (agent.indexOf("CHROME") != -1 || agent.indexOf("FIREFOX") != -1) { // 谷歌或火狐
//            encodedfileName = new String(fileName.getBytes("utf-8"), "utf-8");
//        } else {
//            encodedfileName = new String(fileName.getBytes("utf-8"), "utf-8");
//        }
        
        
        // 设置输出的格式
        response.setContentType("application/excel");
        response.setHeader("Content-disposition","attachment;filename=" +  fileName +";filename*=utf-8''"+ URLEncoder.encode(fileName,"UTF-8"));
        // 取出流中的数据
        ServletOutputStream os = response.getOutputStream();
        workbook.write(os);
        os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});// UTF-8
        os.flush();
        os.close();
    }
	
	/**
	 * 创建excel文件，用于下载导出excel表
	 * @param path
	 * @param fileName
	 * @param workbook
	 * @return
	 */
	public static boolean createExcelFile(String path,String fileName, Workbook workbook) {
		boolean flag = false;
		try {
			File filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			File file = new File(path+fileName);
			
			OutputStream tempout = new FileOutputStream(file);
			workbook.write(tempout);
			tempout.flush();
			tempout.close();
			if(file.exists()){
				flag=true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
      
		return flag;
	}
	
	
	
	
	public static String getCellValueByCell(Cell cell) {
		String value="0";
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "0";
        }
        if (cell != null) {
			switch (cell.getCellType()) {
			case 1:
				value = cell.getStringCellValue();
				break;
			case 0:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					if (date != null) {
						value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					} else {
						value = "0";
					}
				} else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
                    // 防止把1 取成1.0
                    value = cell.getStringCellValue();
				}
				break;

			case 2:
				if (!"".equals(cell.getStringCellValue())) {
					value = cell.getStringCellValue();
				} else {
					value = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case 3:
				break;
			case 5:
				value = "0";
				break;
			case 4:
				value = cell.getBooleanCellValue() ? "Y" : "N";
				break;
			default:
				value = "0";
			}
		}
        return value;
    }
	
	/**
	 * 判断人员excel表中的职级
	 * @param officerLvName
	 * @return
	 */
	public static String checkOfficerLvName(String officerLvName){
		if(officerLvName.indexOf("正营")!=-1){
			officerLvName="正营";
		}
		if(officerLvName.indexOf("正科")!=-1){
			officerLvName="正营";
		}
		if(officerLvName.indexOf("技术10级")!=-1){
			officerLvName="正营";
		}
		if(officerLvName.indexOf("副营")!=-1){
			officerLvName="副营";
		}
		if(officerLvName.indexOf("副科")!=-1){
			officerLvName="副营";
		}
		if(officerLvName.indexOf("技术11级")!=-1){
			officerLvName="副营";
		}
		
		if(officerLvName.indexOf("正连")!=-1){
			officerLvName="正连";
		}
		if(officerLvName.indexOf("一级")!=-1){
			officerLvName="正连";
		}
		if(officerLvName.indexOf("技术12级")!=-1){
			officerLvName="正连";
		}
		if(officerLvName.indexOf("副连")!=-1){
			officerLvName="副连";
		}
		if(officerLvName.indexOf("二级")!=-1){
			officerLvName="副连";
		}
		if(officerLvName.indexOf("技术13级")!=-1){
			officerLvName="副连";
		}
		if(officerLvName.indexOf("正排")!=-1){
			officerLvName="正排";
		}
		if(officerLvName.indexOf("办事员")!=-1){
			officerLvName="正排";
		}
		if(officerLvName.indexOf("技术14级")!=-1){
			officerLvName="正排";
		}
		return officerLvName;
	}
	
	/***
	 * 校验导入的Excel文件标题行是否为标准行
	 */
	public static String verificationStudentExcelHeadLine(String[] rows, String[] columnName) {
		List<String> list1 = Arrays.asList(rows);
		List<String> list = new ArrayList<>();
		for (String t : columnName) {
			if (!list1.contains(t)) {
				list.add(t);
			}
		}
		String result = "";
		for (String integer : list) {
			result += integer + "、";
		}
		return result;
	}


	public static void main(String[] args) {
		String[] a = {"序号", "姓名", "身份证号", "性别","大单位","性别","军(警)衔","出生年月（格式要求：yyyy.mm）",
				"入伍年月（格式要求：yyyy.mm）","婚姻状况","入伍所在地","拟安置地","联系电话","安置类别","原部别","民族","退伍时间（格式要求：yyyy.mm.dd）",
				"现服役时间(格式：X年X个月)","易地安置原因","配偶姓名","政治面貌","入党/入团时间（格式要求：yyyy.mm）","文化程度",
				"是否烈士子女","伤残性质","伤残等级","退役士兵档案分",
				"是否属于艰苦地区","艰边地区(如是艰边地区请填写具体地点及年限)","立功及表彰情况","处分情况"}; 
		String[] b = {"序号", "姓名", "身份证号", "性别","大单位","性别","军(警)衔","出生年月（格式要求：yyyy.mm）",
				"入伍年月（格式要求：yyyy.mm）","婚姻状况","入伍所在地","拟安置地","联系电话","安置类别","原部别","民族","退伍时间（格式要求：yyyy.mm.dd）",
				"现服役时间(格式：X年X个月)","易地安置原因","配偶姓名","政治面貌","入党/入团时间（格式要求：yyyy.mm）","文化程度",
				"是否烈士子女","伤残性质","伤残等级","退役士兵档案分",
				"是否属于艰苦地区","立功及表彰情况","处分情况"}; 
		String result = verificationStudentExcelHeadLine(a,b);
		System.out.println(result);
	}
}
