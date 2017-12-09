package org.warp.picalculator;

public class TestJNI {
   static {
      // picalculatornative.dll on Windows or libpicalculatornative.so on Linux
      System.loadLibrary("picalculatornative"); 
   }
   private native void sayHello();
 
   public static void main(String[] args) {
      // invoke the native method
      new TestJNI().sayHello();  
   }
}