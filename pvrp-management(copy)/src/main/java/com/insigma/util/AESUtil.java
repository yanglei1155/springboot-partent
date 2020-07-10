package com.insigma.util;
//
//import org.apache.commons.codec.binary.Base64;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.SecureRandom;
//import java.text.SimpleDateFormat;
//import java.util.Date;

public class AESUtil {
//	private static final String ALGORITHM = "AES"; 
//    
//	 public static final String KEY = "eptoarmy";
//	    
//	  /** 
//	   * 加密测试 
//	   */
//	  @Test
//	  public static String testEncrypt(String content) throws Exception { 
//	    //1、指定算法、获取Cipher对象 
//	    Cipher cipher = Cipher.getInstance(ALGORITHM);//算法是AES 
//	    //2、生成/读取用于加解密的密钥 
//	    SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
//	    //3、用指定的密钥初始化Cipher对象，指定是加密模式，还是解密模式 
//	    cipher.init(Cipher.ENCRYPT_MODE, secretKey); 
//	    //4、更新需要加密的内容 
//	    cipher.update(content.getBytes()); 
//	    //5、进行最终的加解密操作 
//	    byte[] result = cipher.doFinal();//加密后的字节数组 
//	    //也可以把4、5步组合到一起，但是如果保留了4步，同时又是如下这样使用的话，加密的内容将是之前update传递的内容和doFinal传递的内容的和。 
//	//   byte[] result = cipher.doFinal(content.getBytes()); 
//	    String base64Result =new String(Base64.encodeBase64(result));//对加密后的字节数组进行Base64编码 
//	    System.out.println("Result: " + base64Result); 
//	    return base64Result;
//	  } 
//	    
//	  /** 
//	   * 解密测试 
//	   */
//	  @Test
//	  public void testDecrpyt() throws Exception { 
//	    Cipher cipher = Cipher.getInstance(ALGORITHM); 
//	    SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
//	    cipher.init(Cipher.DECRYPT_MODE, secretKey); 
//	    String content = "pK9Xw4zqTMXYraDadSGJE3x/ftrDxIg2AM/acq0uixA=";//经过Base64加密的待解密的内容 
//	    byte[] encodedBytes = Base64.encodeBase64(content.getBytes()); 
//	    byte[] result = cipher.doFinal(encodedBytes);//对加密后的字节数组进行解密 
//	    System.out.println("Result: " + new String(result)); 
//	  } 
//	    
//
//
//	public static void main(String[] args) {
//		try {
//			String str = testEncrypt("admin");
//			System.out.println(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
