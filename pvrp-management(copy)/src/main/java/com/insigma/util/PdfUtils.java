package com.insigma.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import com.insigma.po.CurriculumVitae;
import com.insigma.po.soldier.ReceivingNotice;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by wangpeng on 2018/02/01.
 */
public class PdfUtils {
	/**
	 * 利用模板生成pdf
	 * 
	 * @param templatePath
	 *            模板路径
	 * @param newPDFPath
	 *            生成的新文件路径
	 * @param o
	 */
	public static void pdfout(InputStream templatePath, String newPDFPath,
			Map<String, Object> o) {
		
		String fonts =  "d:/epsoft/jz/simsun.ttc,1";
		String os = System.getProperty("os.name");
		if (!os.toLowerCase().startsWith("win")) {
			fonts = "/var/epsoft/jz/simsun.ttc,1";
		}
		
		PdfReader reader=null;
		FileOutputStream out=null;
		ByteArrayOutputStream bos=null;
		PdfStamper stamper=null;
		try {
//			byte[] st1 = new byte[inputStream.available()];
//			inputStream.read(st1);
//			BaseFont bf = BaseFont.createFont("simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, BaseFont.NOT_CACHED,
//			st1, st1);
			BaseFont bf = BaseFont.createFont(fonts, BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			Font FontChinese = new Font(bf, 16, Font.NORMAL);
			out = new FileOutputStream(newPDFPath);// 输出流
			reader = new PdfReader(templatePath);// 读取pdf模板
			bos = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, bos);
			AcroFields form = stamper.getAcroFields();
			// 文字类的内容处理
			Map<String, String> datamap = (Map<String, String>) o.get("datamap");
			form.addSubstitutionFont(bf);
			for (String key : datamap.keySet()) {
				String value = datamap.get(key);
				form.setField(key, value);
			}
			
			stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
			stamper.close();
			Document doc = new Document();
			PdfCopy copy = new PdfCopy(doc, out);
			doc.open();
			PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
			copy.addPage(importPage);
			doc.close();
		} catch (IOException e) {
			System.out.println(e);
		} catch (DocumentException e) {
			System.out.println(e);
		} finally {
			try {
				if(stamper!=null){
					stamper.close();
				}
			} catch (DocumentException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				bos.close();
				reader.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
    /**
     * 合并多个pdf
     * @param files
     * @param newfile
     * @return
     */
	public static boolean mergePdfFiles(List<String> list, String newfile) {
		File file = new File(newfile);
		if(file.exists()){
			file.delete();
		}
		boolean retValue = false;
		Document document = null;
		FileOutputStream fos =null;
		try {
			PdfReader reader1 = new PdfReader(list.get(0));
			document = new Document(reader1.getPageSize(1));
			fos = new FileOutputStream(newfile);
			
			PdfCopy copy = new PdfCopy(document, fos );
			document.open();
			for (int i = 0; i < list.size(); i++) {
				PdfReader reader = new PdfReader(list.get(i));
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
				reader.close();
			}
			reader1.close();
			retValue = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return retValue;
	}
	
	public static void delPdf(List<String> list){
		for (int i = 0; i < list.size(); i++) {
			File file = new File(list.get(i));
			if(file.exists()){
				file.delete();
			}
		}
	}
	
	public static void main(String[] args) {
//		List<String> list = new ArrayList<String>();
//		list.add("D:\\epsoft\\jz\\pdfpath\\15005716335.pdf");
//		list.add("D:\\epsoft\\jz\\pdfpath\\15005716336.pdf");
//		list.add("D:\\epsoft\\jz\\pdfpath\\15005716337.pdf");
		String savepath = "D:\\epsoft\\jz\\pdfpath\\bbbb.pdf";  
//		mergePdfFiles(list, savepath);  
//		delPdf(list);
		
//		String param1="(2020)安字第(1)号";
//		String param2="xxx部队(部队集中移交机关):";
//		String param3="张三";
//		String param4="430481202001010101";
//		String param5="2020年3月6日";
//		String param6="4月6日";
//		String param7="杭州市拱墅区";
//		String param8="2020年3月6日";
//		String param9 = "(2020)安字第(1)号(骑缝章)";
		
		String param1="张三";
		String param2="2020年3月6日";
		String param3="2020年3月6日";
		String param4="2020年3月6日";
		String param5="杭州市";
		String param6="2020年3月6日";
		
		Map<String, String> map = new HashMap();
		map.put("param1", param1);
		map.put("param2", param2);
		map.put("param3", param3);
		map.put("param4", param4);
		map.put("param5", param5);
		map.put("param6", param6);
//		map.put("param7", param7);
//		map.put("param8", param8);
//		map.put("param9", param1);
//		map.put("param10", param2);
//		map.put("param11", param3);
//		map.put("param12", param4);
//		map.put("param13", param5);
//		map.put("param14", param6);
//		map.put("param15", param7);
//		map.put("param16", param8);
//		map.put("param17", param1);
//		map.put("param18", param2);
//		map.put("param19", param3);
//		map.put("param20", param4);
//		map.put("param21", param5);
//		map.put("param22", param6);
//		map.put("param23", param7);
//		map.put("param24", param8);
//		map.put("param25", param9);
//		map.put("param26", param9);
		Map<String, Object> o = new HashMap();
		o.put("datamap", map);
		long t1=System.currentTimeMillis();
		ResourcePath rp = new ResourcePath();
		InputStream inputStream=null;
		try {
			inputStream=rp.getResourcePath("放弃安置待遇通知书模板.pdf").getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 pdfout(inputStream,"D:\\epsoft\\浙江军转\\二期\\test\\"+t1+".pdf",o);
//		ReceivingNotice vo = new ReceivingNotice();
//		vo.setName("asdfsd");
//		try {
//			long t1=System.currentTimeMillis();
//			createReceivingNoticePdf("D:\\epsoft\\浙江军转\\二期\\test\\"+t1+".pdf", vo);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
		
//        Map<String,Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("text1", "2019年");
//        dataMap.put("text2", "省委办公厅人事处：");
//        dataMap.put("text3", "正团职干部(机关)5名、一般干部(机关)3名");
//        dataMap.put("text4", "9月30日");
//        dataMap.put("text5", "10月底");
//        dataMap.put("text6", "浙军转组〔2016〕1号");
//        dataMap.put("text7", "7天内");
//        dataMap.put("text8", "http://192.168.200.14:81/#/login");
//        dataMap.put("text9", "swbgt");
//        dataMap.put("text10", "000000");
//        dataMap.put("text11", "10月8日");
//        dataMap.put("text12", "张三");
//        dataMap.put("text13", "88888888");
//        dataMap.put("text14", "18890251234");
//        dataMap.put("text15", "2019年9月18日");
//        Configuration configuration = new Configuration();
//        configuration.setDefaultEncoding("utf-8");
//        
//        try {
//			configuration.setDirectoryForTemplateLoading(new File("D:/epsoft/浙江军转/"));
//			
//			//输出文档路径及名称
//			File outFile = new File("D:/报销信息导出.doc");
//
//			//以utf-8的编码读取ftl文件
//			Template template = configuration.getTemplate("办公模板.ftl", "utf-8");
//			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
//			template.process(dataMap, out);
//			out.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

    /**
	 * 表格各种属性综合使用
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void createTablePdf(String filePath,CurriculumVitae vo) throws IOException, DocumentException {
		System.out.println("------------开始生成PDF-------------");
		Document document = new Document();
		long t1=System.currentTimeMillis();
		// 创建PdfWriter对象
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		// 打开文档
		document.open();
 
		// 添加表格，7列
		PdfPTable table = new PdfPTable(7);
		//// 设置表格宽度比例为%100
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(500);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 70, 70, 70, 70,70, 70,80 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(5f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(5f);
		// 设置表格默认为无边框
		table.getDefaultCell().setBorder(0);
		PdfContentByte cb = writer.getDirectContent();
 
		String fonts = "D:/epsoft/jz/simsun.ttc,1";
		String os = System.getProperty("os.name");
		if (!os.toLowerCase().startsWith("win")) {
			fonts = "/var/epsoft/jz/simsun.ttc,1";
		}
		BaseFont bfChinese  = BaseFont.createFont(fonts, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		Font titlefont = new Font(bfChinese, 18, Font.NORMAL);
		Font keyfont = new Font(bfChinese, 12, Font.NORMAL);
		Font valuefont = new Font(bfChinese, 10, Font.NORMAL);
		
		//----------------------标题
		PdfPCell title = new PdfPCell(new Paragraph("进省直安置简历",titlefont));
		// 设置占用列数
		title.setColspan(7);
		// 设置高度
		title.setFixedHeight(30);
		// 设置无边框
		title.setBorder(Rectangle.NO_BORDER);
		// 设置内容水平居中显示
		title.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(title);
		
		//----------------------姓名
		PdfPCell name_key = new PdfPCell(new Paragraph("姓名",keyfont));
		// 边框颜色
		name_key.setBorderColor(BaseColor.BLACK);
		// 设置高度
		name_key.setFixedHeight(20);
		// 设置内容水平居中显示
		name_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		name_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(name_key);
		
		PdfPCell name_value = new PdfPCell(new Paragraph(vo.getName(),valuefont));
		// 边框颜色
		name_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		name_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		name_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(name_value);
		
		//----------------------性别
		PdfPCell sex_key = new PdfPCell(new Paragraph("性别",keyfont));
		// 边框颜色
		sex_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		sex_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		sex_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(sex_key);
		
		PdfPCell sex_value = new PdfPCell(new Paragraph(vo.getSex(),valuefont));
		// 边框颜色
		sex_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		sex_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		sex_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(sex_value);
		
		//----------------------出生日期
		PdfPCell birthday_key = new PdfPCell(new Paragraph("出生日期",keyfont));
		// 边框颜色
		birthday_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		birthday_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		birthday_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(birthday_key);
		
		PdfPCell birthday_value = new PdfPCell(new Paragraph(vo.getBirthday(),valuefont));
		// 边框颜色
		birthday_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		birthday_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		birthday_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(birthday_value);				
		
		
		// 在表格添加图片
		Image cellimg = Image.getInstance(vo.getPhotoPath());
		cellimg.setAlignment(Image.ALIGN_CENTER);
		cellimg.scaleAbsolute(185,240);
		PdfPCell photo = new PdfPCell(cellimg, true);
		// 设置高度
		photo.setFixedHeight(90);
		photo.setBorderColor(BaseColor.BLACK);
		// 设置跨两行
		photo.setRowspan(4);
		// 设置距单元格顶部的距离
		photo.setPaddingTop(5);
		// 设置距单元格左边的距离
		photo.setPaddingLeft(10);
		// 设置距单元格右边的距离
		photo.setPaddingRight(10);
		// 设置距单元格底部的距离
		photo.setPaddingBottom(5);
		// 设置垂直居中
		photo.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(photo);				
		
		
		//----------------------籍贯
		PdfPCell address_key = new PdfPCell(new Paragraph("籍贯",keyfont));
		// 设置高度
		address_key.setFixedHeight(20);
		// 边框颜色
		address_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		address_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		address_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(address_key);
		
		PdfPCell address_value = new PdfPCell(new Paragraph(vo.getAddress(),valuefont));
		// 边框颜色
		address_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		address_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		address_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(address_value);			
		
		
		//----------------------职级
		PdfPCell officer_lv_key = new PdfPCell(new Paragraph("职级",keyfont));
		// 边框颜色
		officer_lv_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		officer_lv_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		officer_lv_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(officer_lv_key);
		
		PdfPCell officer_lv_value = new PdfPCell(new Paragraph(vo.getOfficerLvName(),valuefont));
		// 边框颜色
		officer_lv_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		officer_lv_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		officer_lv_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(officer_lv_value);				
		
		//----------------------职级时间
		PdfPCell officer_date_key = new PdfPCell(new Paragraph("职级时间",keyfont));
		// 边框颜色
		officer_date_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		officer_date_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		officer_date_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(officer_date_key);
		
		PdfPCell officer_date_value = new PdfPCell(new Paragraph(vo.getOfficerDate(),valuefont));
		// 边框颜色
		officer_date_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		officer_date_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		officer_date_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(officer_date_value);		
		
		//----------------------学历
		PdfPCell bachelor_degree_key = new PdfPCell(new Paragraph("学历",keyfont));
		// 设置高度
		bachelor_degree_key.setFixedHeight(20);
		// 边框颜色
		bachelor_degree_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		bachelor_degree_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		bachelor_degree_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(bachelor_degree_key);
		
		PdfPCell bachelor_degree_value = new PdfPCell(new Paragraph(vo.getBachelorDegree(),valuefont));
		// 边框颜色
		bachelor_degree_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		bachelor_degree_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		bachelor_degree_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(bachelor_degree_value);				
		
		//----------------------学历专业
		PdfPCell mojar_key = new PdfPCell(new Paragraph("学历专业",keyfont));
		// 边框颜色
		mojar_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		mojar_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		mojar_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(mojar_key);
		
		PdfPCell mojar_value = new PdfPCell(new Paragraph(vo.getMojar(),valuefont));
		// 边框颜色
		mojar_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		mojar_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		mojar_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(mojar_value);				
		
		//----------------------联系方式
		PdfPCell phone_key = new PdfPCell(new Paragraph("联系方式",keyfont));
		// 边框颜色
		phone_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		phone_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		phone_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(phone_key);
		
		PdfPCell phone_value = new PdfPCell(new Paragraph(vo.getPhone(),valuefont));
		// 边框颜色
		phone_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		phone_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		phone_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(phone_value);		
		
		
		//----------------------原单位
		PdfPCell up_company_name_key = new PdfPCell(new Paragraph("原单位",keyfont));
		// 设置高度
		up_company_name_key.setFixedHeight(20);
		// 边框颜色
		up_company_name_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		up_company_name_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		up_company_name_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(up_company_name_key);
		
		PdfPCell up_company_name_value = new PdfPCell(new Paragraph(vo.getUpCompanyName(),valuefont));
		// 设置占用列数
		up_company_name_value.setColspan(5);
		// 边框颜色
		up_company_name_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		up_company_name_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		up_company_name_value.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(up_company_name_value);		
		 	
		
		//----------------------教育背景
		PdfPCell educational_bg_key = new PdfPCell(new Paragraph("教育背景",keyfont));
		// 设置高度
		educational_bg_key.setFixedHeight(40);
		// 边框颜色
		educational_bg_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		educational_bg_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		educational_bg_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(educational_bg_key);
		
		PdfPCell educational_bg_value = new PdfPCell(new Paragraph(vo.getEducationalBg(),valuefont));
		// 设置占用列数
		educational_bg_value.setColspan(6);
		// 边框颜色
		educational_bg_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		educational_bg_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		educational_bg_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(educational_bg_value);		
		
		
		//----------------------工作经历
		PdfPCell work_experience_key = new PdfPCell(new Paragraph("工作经历",keyfont));
		// 设置高度
		work_experience_key.setFixedHeight(40);
		// 边框颜色
		work_experience_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		work_experience_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		work_experience_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(work_experience_key);
		
		PdfPCell work_experience_value = new PdfPCell(new Paragraph(vo.getWorkExperience(),valuefont));
		// 设置占用列数
		work_experience_value.setColspan(6);
		// 边框颜色
		work_experience_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		work_experience_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		work_experience_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(work_experience_value);		
		
		
		//----------------------家庭成员
		PdfPCell families_member_key = new PdfPCell(new Paragraph("家庭成员",keyfont));
		// 设置高度
		families_member_key.setFixedHeight(40);
		// 边框颜色
		families_member_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		families_member_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		families_member_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(families_member_key);
		
		PdfPCell families_member_value = new PdfPCell(new Paragraph(vo.getFamiliesMember(),valuefont));
		// 设置占用列数
		families_member_value.setColspan(6);
		// 边框颜色
		families_member_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		families_member_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		families_member_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(families_member_value);		
		
		//----------------------专业对口
		PdfPCell zydk_key = new PdfPCell(new Paragraph("专业对口",keyfont));
		// 设置高度
		zydk_key.setFixedHeight(40);
		// 边框颜色
		zydk_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		zydk_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		zydk_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(zydk_key);
		
		PdfPCell zydk_value = new PdfPCell(new Paragraph(vo.getZydk(),valuefont));
		// 设置占用列数
		zydk_value.setColspan(6);
		// 边框颜色
		zydk_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		zydk_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		zydk_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(zydk_value);		
		
		
		//----------------------原单位工作介绍
		PdfPCell ydwgzjs_key = new PdfPCell(new Paragraph("原单位工作介绍",keyfont));
		// 设置高度
		ydwgzjs_key.setFixedHeight(40);
		// 边框颜色
		ydwgzjs_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		ydwgzjs_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		ydwgzjs_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(ydwgzjs_key);
		
		PdfPCell ydwgzjs_value = new PdfPCell(new Paragraph(vo.getYdwgzjs(),valuefont));
		// 设置占用列数
		ydwgzjs_value.setColspan(6);
		// 边框颜色
		ydwgzjs_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		ydwgzjs_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		ydwgzjs_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(ydwgzjs_value);		
		
		//----------------------个人能力特长介绍
		PdfPCell personal_ability_key = new PdfPCell(new Paragraph("个人能力特长介绍",keyfont));
		// 设置高度
		personal_ability_key.setFixedHeight(40);
		// 边框颜色
		personal_ability_key.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		personal_ability_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		personal_ability_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(personal_ability_key);
		
		PdfPCell personal_ability_value = new PdfPCell(new Paragraph(vo.getPersonalAbility(),valuefont));
		// 设置占用列数
		personal_ability_value.setColspan(6);
		// 边框颜色
		personal_ability_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		personal_ability_value.setHorizontalAlignment(Element.ALIGN_LEFT);
		// 设置垂直居中
//		personal_ability_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(personal_ability_value);			
		
		
		// 添加表格到文档
		document.add(table);
		// 关闭文档
		document.close();
		System.out.println("------------PDF生成完成-------------");
	}	
	
	
	
	
	/**
	 * 表格各种属性综合使用
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void createReceivingNoticePdf(String filePath,ReceivingNotice vo) throws IOException, DocumentException {
		System.out.println("------------开始生成PDF-------------");
		
//		Document document = new Document();
		//实现A4纸页面 并且横向显示（不设置则为纵向）
		Document document = new Document(PageSize.A4.rotate());
		
		
		long t1=System.currentTimeMillis();
		// 创建PdfWriter对象
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		// 打开文档
		document.open();
 
		// 添加表格，7列
		PdfPTable table = new PdfPTable(5);
		//// 设置表格宽度比例为%100
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(810);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 270,10, 270,10,270 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(5f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(5f);
		// 设置表格默认为无边框
		table.getDefaultCell().setBorder(1);
		PdfContentByte cb = writer.getDirectContent();
 
		String fonts = "D:/epsoft/jz/simsun.ttc,1";
		String os = System.getProperty("os.name");
		if (!os.toLowerCase().startsWith("win")) {
			fonts = "/var/epsoft/jz/simsun.ttc,1";
		}
		BaseFont bfChinese  = BaseFont.createFont(fonts, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		Font titlefont = new Font(bfChinese, 18, Font.NORMAL);
		Font keyfont = new Font(bfChinese, 12, Font.NORMAL);
		Font valuefont = new Font(bfChinese, 10, Font.NORMAL);
		
		//----------------------标题
		PdfPCell title = new PdfPCell(new Paragraph("进省直安置简历",titlefont));
		// 设置占用列数
		title.setColspan(3);
		// 设置高度
		title.setFixedHeight(30);
		// 设置无边框
		title.setBorder(Rectangle.NO_BORDER);
		// 设置内容水平居中显示
		title.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(title);
		
		//----------------------姓名
		PdfPCell name_key = new PdfPCell(new Paragraph("姓名11111",keyfont));
		// 边框颜色
		name_key.setBorderColor(BaseColor.BLACK);
		// 设置高度
		name_key.setFixedHeight(20);
		// 设置内容水平居中显示
		name_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		name_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(name_key);
		
		PdfPCell aa = new PdfPCell(new Paragraph("进省直安置简历",titlefont));
		// 设置占用列数
		aa.setRowspan(3);
		// 设置高度
		aa.setFixedHeight(30);
		// 设置无边框
		aa.setBorder(Rectangle.NO_BORDER);
		// 设置内容水平居中显示
		aa.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		aa.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(aa);
		
		PdfPCell name_value = new PdfPCell(new Paragraph(vo.getName(),valuefont));
		// 边框颜色
		name_value.setBorderColor(BaseColor.BLACK);
		// 设置内容水平居中显示
		name_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		name_value.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(name_value);
		
		//----------------------姓名
		PdfPCell sex_key = new PdfPCell(new Paragraph("姓名122",keyfont));
		// 边框颜色
		sex_key.setBorderColor(BaseColor.BLACK);
		// 设置高度
		sex_key.setFixedHeight(20);
		// 设置内容水平居中显示
		sex_key.setHorizontalAlignment(Element.ALIGN_CENTER);
		// 设置垂直居中
		sex_key.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(sex_key);				
		
		
		// 添加表格到文档
		document.add(table);
		// 关闭文档
		document.close();
		System.out.println("------------PDF生成完成-------------");
	}	
}
