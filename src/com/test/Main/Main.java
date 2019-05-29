package com.test.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Enum.Profil;

public class Main {

	public static void main(String[] args) {
		testMe();
		
	}
	
	public static void testMe() {
		Object s="2";
		String value="";
  	  if (s instanceof Integer) {
		  System.out.println("result is integer , value is :"+s);
	    } else if(s instanceof String) {
              value =s.toString();
    		  System.out.println("result is s , value is :"+s);
	    } else if(s instanceof Float) {
              value =String.valueOf(s);
    		  System.out.println("result is f , value is :"+s);
	    }else if(s instanceof Date) {
           DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
 		  System.out.println("result is d , value is :"+s);
          value = dateFormat.format(s);  

	    }
  	  
  	  System.out.println(value);
	}

}
