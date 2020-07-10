package com.example.com.springboot.utiles;

import javax.servlet.http.Cookie;

public class CookieUtile {
   public static Cookie findCookie(String name,Cookie[]cookies) {
	 if(cookies!=null) {
	   for(Cookie cookie:cookies) {
		   if(name.equals(cookie.getName())) {
		      return cookie;	   
		   }
		
	   }
     }
	   
	   return null;
   }
}
