package org.warp.picalculator;

import java.io.IOException;

import cz.adamh.utils.NativeUtils;

public class TestJNI {
   static {
	    try {    
	        NativeUtils.loadLibraryFromJar("/picalculatornative.dll");   
	      } catch (IOException e) {    
	        e.printStackTrace(); // This is probably not the best way to handle exception :-)  
	      } 
   }
   private native void sayHello();
 
   public static void main(String[] args) {
      // invoke the native method
      new TestJNI().sayHello();  
   }
}