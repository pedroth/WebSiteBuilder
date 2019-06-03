package com.pedroth.utils;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public final class FilesCrawler {
    /**
     * default constructor.
     */
    private FilesCrawler() {
        // private constructor to disallow instance creation
    }

    /**
     * @param directoryName
     * @return Map of all filesPathsByFileName under a directory
     */
    public static Map<String, String> listFilesRecursively(String directoryName) {
        File directory = new File(directoryName);
        Map<String, String> filePathByFileName = new HashMap<String, String>();
        Stack<File> stack = new Stack<>();
        stack.add(directory);
        while (!stack.isEmpty()) {
            File f = stack.pop();
            if (f.isFile()) {
                filePathByFileName.put(f.getAbsolutePath(), f.getName());
            } else if (f.isDirectory()) {
                File[] files = f.listFiles();
                Collections.addAll(stack, files);
            }
        }
        return filePathByFileName;
    }

    /**
     * @param directoryName
     * @return Map of all filesPathsByFileName under a directory
     */
    public static List<String> listFilesWithExtension(String directoryName, String extension) {
        List<String> ans = new ArrayList<>();
        Map<String, String> filesMap = FilesCrawler.listFilesRecursively(directoryName);
        for (Map.Entry<String, String> entry : filesMap.entrySet()) {
            String address = entry.getKey();
            if (isExtension(address, extension)) {
                ans.add(address);
            }
        }
        return ans;
    }

    public static boolean isExtension(String fileName, String extension) {
        String[] s = fileName.split("\\.");
        return extension.equals(s[s.length - 1]);
    }

    public static String getExtension(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }

    public static void createDirs(String address) {
        File dirs = new File(address);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
    }

    public static String[] getDirs(String path) {
        File file = new File(path);
        return file.list((current, name) -> new File(current, name).isDirectory());
    }


    public static void applyFiles(String dirPath, Consumer<File> lambda) {
        File dirs = new File(dirPath);
        if (!dirs.isDirectory()) {
            return;
        }
        for (File file : dirs.listFiles()) {
            if (!file.isDirectory()) {
                lambda.accept(file);
            }
        }
    }
}
