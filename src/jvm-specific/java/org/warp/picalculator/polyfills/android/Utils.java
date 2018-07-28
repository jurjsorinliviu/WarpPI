package org.warp.picalculator.polyfills.android;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Utils {

    public static void printSystemResourcesUsage() {
        System.out.println("============");
        final OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (final Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (final Exception e) {
                    value = e;
                } // try
                boolean percent = false;
                boolean mb = false;
                final String displayName = method.getName();
                final String displayValue = value.toString();
                if (displayName.endsWith("CpuLoad")) {
                    percent = true;
                }
                if (displayName.endsWith("MemorySize")) {
                    mb = true;
                }
                final ObjectArrayList<String> arr = new ObjectArrayList<>();
                arr.add("getFreePhysicalMemorySize");
                arr.add("getProcessCpuLoad");
                arr.add("getSystemCpuLoad");
                arr.add("getTotalPhysicalMemorySize");
                if (arr.contains(displayName)) {
                    if (percent) {
                        try {
                            System.out.println(displayName + " = " + (((int) (Float.parseFloat(displayValue) * 10000f)) / 100f) + "%");
                        } catch (final Exception ex) {
                            System.out.println(displayName + " = " + displayValue);
                        }
                    } else if (mb) {
                        try {
                            System.out.println(displayName + " = " + (Long.parseLong(displayValue) / 1024L / 1024L) + " MB");
                        } catch (final Exception ex) {
                            System.out.println(displayName + " = " + displayValue);
                        }
                    } else {
                        System.out.println(displayName + " = " + displayValue);
                    }
                }
            } // if
        } // for
        System.out.println("============");
    }
}
