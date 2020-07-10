package com.insigma.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;

import com.insigma.po.soldier.FlexibleEmploymentInfo;
import com.insigma.po.soldier.ReceivingNotice;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 类描述： 根据ftl模板生成word文件 创建人：刘伟民 创建时间：2020年3月19日 上午10:31:59
 * 
 * @version
 */
public class WordUtils {
	/**
	 * 根据模板生成word温昂
	 * 
	 * @param outFilePath
	 *            输出文档路径
	 * @param outFileName
	 *            输出文档名称
	 * @param moedelName
	 *            模板名称
	 * @param map
	 *            数据内容
	 */
	public Boolean createWord(String outFilePath, String outFileName, String moedelName, Map<String, String> map){
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
		configuration.setDefaultEncoding("utf-8");

		try {
			System.out.println("------开始生成word------");
			File filepath = new File(outFilePath);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			// 输出文档路径及名称
			File outFile = new File(outFilePath + outFileName);

			// 以utf-8的编码读取ftl文件
			configuration.setClassForTemplateLoading(this.getClass(), "/templates/print");
			Template template = configuration.getTemplate(moedelName);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
			template.process(map, out);
			out.flush();
			out.close();
			System.out.println("------生成word完成------");
		} catch (Exception e) {
			e.printStackTrace();
			return  false;
		}
		return  true;
	}

	/**
	 * 接收安置通知书数据解析
	 * 
	 * @param vo
	 * @param rootPath
	 * @return
	 */
	public ReceivingNotice addMap(ReceivingNotice vo, String rootPath) {
		
		String newString = "1"+String.format("%04d", vo.getNum());
		
		String param1 = "（" + vo.getYear() + "）安字第（" + newString + "）号";

		String[] startTime = vo.getStartTime().split("月");
		String param6 = startTime[0];
		String param7 = startTime[1].substring(0, startTime[1].length() - 1);

		String[] endTime = vo.getEndTime().split("月");
		String param8 = endTime[0];
		String param9 = endTime[1].substring(0, endTime[1].length() - 1);
		String personnelType = "退役士兵";
		String param13="部";
		String param14="原部队";
		if (vo.getPersonnelType() == 2) {
			personnelType = "退出消防员";
			param13="单位";
			param14="原单位";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("param1", param1);
		map.put("param2", vo.getUnitName());
		map.put("param3", vo.getName());
		map.put("param4", vo.getIdcard());
		map.put("param5", vo.getYear() + "");
		map.put("param6", param6);
		map.put("param7", param7);
		map.put("param8", param8);
		map.put("param9", param9);
		map.put("param10", vo.getRegionalName());
		map.put("param11", DateUtils.getCnCurrentDate());
		map.put("param12", personnelType);
		map.put("param13", param13);
		map.put("param14", param14);

//		String dir1=vo.getRegionalName().substring(0,3);
//		String dir2=vo.getRegionalName().substring(3,6);
		
		String unitName="接收安置通知书";
//		if(vo.getUnitName().endsWith("消防救援总队")){
//			unitName="消防救援总队";
//		}
		
		String path = "接收安置通知书word/" + DateUtils.getStringCurrentDate() + "/"+unitName+"/";
		FileUtils.createFilePath(rootPath + path);
		
		createWord(rootPath + path, newString + ".doc", "接收安置通知书模板.ftl", map);

		vo.setFilePath(path + newString + ".doc");
		return vo;
	}

	/**
	 * 功能:压缩多个文件成一个zip文件
	 * 
	 * @param fileList
	 *            待压缩文件
	 * @param zipfile
	 *            压缩后文件
	 * @param rootPath
	 *            文件根目录
	 */
	public static void fileToZip(List<String> fileList, String zipfile, String rootPath) {
		byte[] buf = new byte[1024];
		try {
			File newZip = new File(rootPath + zipfile);
			// ZipOutputStream类：完成文件或文件夹的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(newZip));
			for (int i = 0; i < fileList.size(); i++) {
				File file = new File(rootPath + fileList.get(i));
				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 压缩成ZIP 方法1
	 * 
	 * @param srcDir
	 *            压缩文件夹路径
	 * @param out
	 *            压缩文件输出流
	 * @param KeepDirStructure
	 *            是否保留原来的目录结构,true:保留目录结构;
	 *            false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {

		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 递归压缩方法
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param zos
	 *            zip输出流
	 * @param name
	 *            压缩后的名称
	 * @param KeepDirStructure
	 *            是否保留原来的目录结构,true:保留目录结构;
	 *            false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
			throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			FileOutputStream fos1 = new FileOutputStream(new File("D:\\epsoft\\jz\\接收安置通知书word\\mytest01.zip"));
			toZip("D:\\epsoft\\jz\\接收安置通知书word\\20200612",fos1,true);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
//		List<String> list = new ArrayList<String>();
//		list.add("D:\\epsoft\\jz\\接收安置通知书word\\20200417\\11010119900307985X.doc");
//		list.add("D:\\epsoft\\jz\\接收安置通知书word\\20200417\\33032719810807735X.doc");

		// fileToZip(list,"D:\\epsoft\\jz\\接收安置通知书word\\20200417\\20200417.zip");
		// Map<String,String> dataMap = new HashMap<String, String>();
		// dataMap.put("param1", "张三丰");
		// dataMap.put("param2", "2020");
		// dataMap.put("param3", "4");
		// dataMap.put("param4", "24");
		// dataMap.put("param5", "2020");
		// dataMap.put("param6", "3");
		// dataMap.put("param7", "14");
		// dataMap.put("param8", "2020");
		// dataMap.put("param9", "4");
		// dataMap.put("param10", "28");
		// dataMap.put("param11", "杭州市");
		// dataMap.put("param12", "2020年4月28日");
		// dataMap.put("param13", "☑");
		// dataMap.put("param14", "□");
		WordUtils wu = new WordUtils();
		long t = System.currentTimeMillis();
		// wu.createWord("E:\\epsoft\\退役军人\\二期\\测试模板\\test\\"+t+".doc","放弃安置待遇告知书模板.ftl",dataMap);

		// wu.createWord("E:\\epsoft\\退役军人\\二期\\测试模板\\test\\"+t+".doc","量化评分表模板.ftl",map());
	}

}
