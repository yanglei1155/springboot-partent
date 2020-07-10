package com.insigma.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 *       
 * 类描述：   获取服务器IP地址
 * 创建人：liuwm   
 * 创建时间：2020年3月10日 上午10:37:03   
 * @version
 */
public class InetAddressInUse {
	public static String getIp(){
		String ipStr="";
		try {
			InetAddress ip4 = Inet4Address.getLocalHost();
			ipStr=ip4.getHostAddress();
			System.out.println(ipStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipStr;
	}
}
