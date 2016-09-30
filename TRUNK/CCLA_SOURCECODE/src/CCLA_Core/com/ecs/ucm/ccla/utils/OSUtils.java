package com.ecs.ucm.ccla.utils;

public final class OSUtils {
	
   private static String OS = null;


   public static String getOsName() {
      if(OS == null) { OS = System.getProperty("os.name"); }
      return OS;
   }
   
   public static boolean isWindows() {
      return getOsName().startsWith("Windows");
   }

   public static boolean isUnixOrLinux() {
	   return ( getOsName().equals("Linux") ||
			   getOsName().equals("Unix"));
   }

   public static boolean isSolaris() {
	   return getOsName().startsWith("Solaris");
   }

   
}
