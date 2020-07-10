package com.insigma.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

public class FileUtils {
	public String upload(MultipartFile file,String filePath) throws IllegalStateException, IOException {
		String path="";
		String fileName ="";
		if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        fileName = file.getOriginalFilename();
        // 获取文件的后缀名
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 设置文件存储路径
        path = filePath + fileName;
        File dest = new File(path);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
        file.transferTo(dest);// 文件写入    
        return fileName;
	} 
	
	public String downloadFile(HttpServletRequest request, HttpServletResponse response, String filepath) throws UnsupportedEncodingException {
		// String fileName = "dalaoyang.jpeg";// 文件名
		// 设置文件路径
		File file = new File(filepath);
		String fileName = file.getName();
		if (file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));// 设置文件名
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "下载成功";
	}
	
	public static File[] getFilesByUnzip(String zipFile, String dest) {
		try {
			ZipFile zFile = new ZipFile(zipFile);
			zFile.setFileNameCharset("GBK");
			if (!zFile.isValidZipFile()) {
				throw new Exception("文件名称不合法");
			}
			File destFile = new File(dest);
			if (destFile.isDirectory() && !destFile.exists()) {
				destFile.mkdirs();
			}
			zFile.extractAll(dest);
			// 获取文件列表
			List  fileHeaders = zFile.getFileHeaders();
			List<File> extractFiles = new ArrayList<File>();
			if (fileHeaders != null && fileHeaders.size() > 0) {
				for (int i = 0; i < fileHeaders.size(); i++) {
					FileHeader header = (FileHeader) fileHeaders.get(i);
					if (header != null) {
						System.out.println(header.getFileName());
						extractFiles.add(new File(destFile, header.getFileName()));
					}
				}
			}
			File[] arr = new File[extractFiles.size()];
			return extractFiles.toArray(arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断文件夹是否存在，不存在则创建
	 * @param filePath
	 */
	public static void createFilePath(String filePath){
		File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
	}
}
