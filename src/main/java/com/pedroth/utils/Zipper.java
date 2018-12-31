package com.pedroth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The type Zipper.
 * Based on https://www.mkyong.com/java/how-to-compress-files-in-zip-format/
 */
public class Zipper {

    /**
     * Zip it.
     *
     * @param sourceFolder the source folder
     * @param zipFile      the zip file
     */
    public static void zipIt(String sourceFolder, String zipFile) {

        byte[] buffer = new byte[1024];

        List<String> fileList = generateFileList(new File(sourceFolder));
        try (
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            System.out.println("Output to Zip : " + zipFile);
            for (String file : fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static List<String> generateFileList(File node) {
        List<String> fileList = new ArrayList<>();
        Stack<File> stack = new Stack<>();
        stack.push(node);
        while (!stack.empty()) {
            File f = stack.pop();
            if (f.isFile()) {
                fileList.add(f.getPath());
            }
            if (f.isDirectory()) {
                String[] subNote = node.list();
                for (String filename : subNote) {
                    stack.push(new File(f, filename));
                }
            }
        }
        return fileList;
    }

    private static String generateZipEntry(String sourceFolder, String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}
