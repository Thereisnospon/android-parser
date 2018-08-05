package com.thereisnospon.util.parse.util;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by yzr on 2018/7/1.
 */
public class ZipUtil {

    public static File zip(String srcPath, String dstDir) throws Exception {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new Exception(srcPath + " not exist!");
        }
        File dstFile = new File(dstDir);
        if (!dstFile.exists()) {
            throw new Exception(dstDir + " not exist");
        } else if (!dstFile.isDirectory()) {
            throw new Exception(dstDir + " not dir");
        }
        File dstZip = new File(dstFile, srcFile.getName() + ".zip");
        if (dstZip.exists()) {
            dstZip = new File(dstFile, srcFile.getName() + System.currentTimeMillis() + ".zip");
        }
        File cDir = dstZip;
        dstZip = new File(dstZip.getAbsolutePath() + "bak");
        try (FileOutputStream fos = new FileOutputStream(dstZip)) {
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte buf[] = new byte[1024];
            writeEntry(zos, "", srcFile, buf);
            zos.flush();
            zos.close();
        }
        if (dstZip.renameTo(cDir)) {
            return cDir;
        } else {
            return dstZip;
        }
    }

    private static void writeEntry(ZipOutputStream zos, String parent, File file, byte[] buf) throws Exception {
        if (file.isDirectory()) {
            String name = parent + file.getName() + "/";
            ZipEntry entry = new ZipEntry(name);
            zos.putNextEntry(entry);
            File files[] = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    writeEntry(zos, name, f, buf);
                }
            }
        } else {
            String name = parent + file.getName();
            ZipEntry entry = new ZipEntry(name);
            zos.putNextEntry(entry);
            int len;
            try (FileInputStream fis = new FileInputStream(file)) {
                BufferedInputStream bis = new BufferedInputStream(fis);
                while ((len = bis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
            }
        }
    }

    private static String stream2Text(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "utf-8");
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine() + "\n");
        }
        return sb.toString();
    }

    public static InputStream getEntryInputStream(String srcZipFilePath, String entry) {
        File srcZipFile = new File(srcZipFilePath);
        if (!srcZipFile.exists()) {
            return null;
        }
        try {
            ZipFile zipFile = new ZipFile(srcZipFilePath);
            return zipFile.getInputStream(zipFile.getEntry(entry));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ZipEntry> allEntry(String srcZipFilePath) {
        File srcZipFile = new File(srcZipFilePath);
        if (!srcZipFile.exists()) {
            return Collections.emptyList();
        }
        List<ZipEntry> entries = new ArrayList<ZipEntry>();
        try (FileInputStream fis = new FileInputStream(srcZipFile)) {
            ZipInputStream zipInputStream = new ZipInputStream(fis);
            ZipEntry entry = null;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                entries.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }

    public static File unzip(String srcZipFilePath, String dstDirPath) throws Exception {
        File srcZipFile = new File(srcZipFilePath);
        if (!srcZipFile.exists()) {
            throw new Exception(srcZipFilePath + " not exist!");
        }
        File dstDir = new File(dstDirPath);
        if (!dstDir.exists()) {
            throw new Exception(dstDirPath + " not exist!");
        } else if (!dstDir.isDirectory()) {
            throw new Exception(dstDirPath + " not dir!");
        }
        ZipFile zipFile = new ZipFile(srcZipFilePath);
        File toDir = new File(dstDir, srcZipFile.getName());
        if (toDir.exists()) {
            toDir = new File(dstDir, srcZipFile.getName() + System.currentTimeMillis());
        }
        File cDir = toDir;
        toDir = new File(cDir.getAbsolutePath() + "bak");
        if (!toDir.exists()) {
            if (!toDir.mkdir()) {
                throw new Exception(toDir.getAbsolutePath() + " mkdir error!");
            }
        } else {

        }
        byte buf[] = new byte[1024];
        int len = 0;
        for (Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries(); entryEnumeration.hasMoreElements(); ) {
            ZipEntry zipEntry = entryEnumeration.nextElement();
            String name = zipEntry.getName();
            File zipEntryFile = new File(toDir, name);
            if (zipEntry.isDirectory()) {
                if (!zipEntryFile.mkdir()) {
                    throw new Exception(zipEntryFile.getAbsolutePath() + " mkdir error");
                }
            } else {
                InputStream zeis = zipFile.getInputStream(zipEntry);
                FileOutputStream zeos = new FileOutputStream(zipEntryFile);
                while ((len = zeis.read(buf, 0, 1024)) > 0) {
                    zeos.write(buf, 0, len);
                }
                zeis.close();
                zeos.flush();
                zeos.close();
            }
        }
        if(toDir.renameTo(cDir)){
            return cDir;
        }else{
            return toDir;
        }
    }
}
