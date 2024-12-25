package com.kun.plugin.publicexamtraining.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtils {


    public static Set<Class<?>> scanClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = PackageUtils.class.getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    classes.addAll(findClasses(new File(resource.getFile()), packageName));
                } else if (resource.getProtocol().equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                    classes.addAll(findClasses(jarURLConnection.getJarFile(), path));
                }
            }
        } catch (IOException e) {
            LogUtils.LOG.error("Failed to scan classes in package: " + packageName, e);
        }
        return classes;
    }

    private static Set<Class<?>> findClasses(File directory, String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (Exception ignored) {
                        LogUtils.LOG.error("Failed to load class: " + className);
                    }
                }
            }
        }
        return classes;
    }

    private static Set<Class<?>> findClasses(JarFile jarFile, String path) {
        Set<Class<?>> classes = new HashSet<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                String className = name.replace('/', '.').substring(0, name.length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (Exception ignored) {
                    LogUtils.LOG.error("Failed to load class: " + className);
                }
            }
        }
        return classes;
    }



}
