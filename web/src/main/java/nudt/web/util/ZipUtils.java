package nudt.web.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtils {
    /**
     * 根据给定密码压缩文件(s)到指定目录
     *
     * @param destFileName 压缩文件存放绝对路径 e.g.:D:/upload/zip/demo.zip
     * @param passwd 密码(可为空)
     * @param files 单个文件或文件数组
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     */
    public static String compress(String destFileName, String passwd, File... files) {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(destFileName);
            for (File file : files) {
                zipFile.addFile(file, parameters);
            }
            return destFileName;
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据给定密码压缩文件(s)到指定位置
     *
     * @param destFileName 压缩文件存放绝对路径 e.g.:D:/upload/zip/demo.zip
     * @param passwd 密码(可为空)
     * @param filePaths 单个文件路径或文件路径数组
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     */
    public static String compress(String destFileName, String passwd, String... filePaths) {
        int size = filePaths.length;
        File[] files = new File[size];
        for (int i = 0; i < size; i++) {
            files[i] = new File(filePaths[i]);
        }
        return compress(destFileName, passwd, files);
    }

    /**
     * 根据给定密码压缩文件(s)到指定位置
     *
     * @param destFileName 压缩文件存放绝对路径 e.g.:D:/upload/zip/demo.zip
     * @param passwd 密码(可为空)
     * @param folder 文件夹路径
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     */
    public static String compressFolder(String destFileName, String passwd, String folder) {
        File folderParam = new File(folder);
        if (folderParam.isDirectory()) {
            File[] files = folderParam.listFiles();
            return compress(destFileName, passwd, files);
        }
        return null;
    }

    /**
     * 根据所给密码解压zip压缩包到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zipFile zip压缩包绝对路径
     * @param dest 指定解压文件夹位置
     * @param passwd 密码(可为空)
     * @return 解压后的文件数组
     * @throws ZipException
     */
    @SuppressWarnings("unchecked")
    public static File[] deCompress(File zipFile, String dest, String passwd) throws ZipException {
        //1.判断指定目录是否存在
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        //2.初始化zip工具
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset("UTF-8");
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        //3.判断是否已加密
        if (zFile.isEncrypted()) {
            zFile.setPassword(passwd.toCharArray());
        }
        //4.解压所有文件
        zFile.extractAll(dest);
        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for(FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir,fileHeader.getFileName()));
            }
        }
        File [] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }
    /**
     * 解压无密码的zip压缩包到指定目录
     * @param zipFile zip压缩包
     * @param dest 指定解压文件夹位置
     * @return 解压后的文件数组
     * @throws ZipException
     */
    public static File[] deCompress(File zipFile, String dest){
        try {
            return deCompress(zipFile, dest, null);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据所给密码解压zip压缩包到指定目录
     * @param zipFilePath zip压缩包绝对路径
     * @param dest 指定解压文件夹位置
     * @param passwd 压缩包密码
     * @return 解压后的所有文件数组
     */
    public static File[] deCompress(String zipFilePath, String dest, String passwd){
        try {
            return deCompress(new File(zipFilePath), dest, passwd);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 无密码解压压缩包到指定目录
     * @param zipFilePath zip压缩包绝对路径
     * @param dest 指定解压文件夹位置
     * @return 解压后的所有文件数组
     */
    public static File[] deCompress(String zipFilePath, String dest){
        try {
            return deCompress(new File(zipFilePath), dest, null);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        String folder = "D:\\upload\\backup\\down\\dezip\\";
//        compress("D:/upload/zip/测试.zip", "123456", folder);

        //deCompress("D:/upload/zip/测试.zip", "D:/upload/zip/unzip", "123456");

    }
}