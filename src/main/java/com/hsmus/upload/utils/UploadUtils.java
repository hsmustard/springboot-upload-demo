package com.hsmus.upload.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * @author Mustard <jianzhang@dgut.edu.cn>
 * @date 2021/1/22
 * @time 14:53
 */
@Component
public class UploadUtils {


    private static List<String> allowExt;

    private static Long allowMaxSize;

    private static final String staticPath = "/static/";

    public static void setAllowExt(List<String> ext) {
        allowExt = ext;
    }

    public static void setAllowMaxSize(Long size) {
        allowMaxSize = size;
    }

    /**
     * 校验上传文件的合法性
     * 使用前务必执行setAllowExt,setAllowMaxSize
     *
     * @param file
     * @return 校验结果
     */
    public static Boolean check(MultipartFile file) {
        if (file == null || file.isEmpty()) return false;
        String ext = FileUtil.extName(file.getOriginalFilename());
        return file.getSize() <= allowMaxSize && allowExt.contains(ext);
    }

    public static String getPath(String uploadPath) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }

        File fileUploadPath = new File(path.getAbsolutePath() + staticPath + uploadPath);
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }
        return fileUploadPath.getAbsolutePath() + File.separator;
    }

    public static String getFilePath(String uploadPath, String fileName) throws FileNotFoundException {
        return getPath(uploadPath) + fileName;
    }


    public static String getFilePath(String uploadPath, MultipartFile file, FileNameGenerator generator) throws FileNotFoundException {
        return getPath(uploadPath) + generator.generator(file);
    }

    public static String getFilePath(String uploadPath, MultipartFile file) throws FileNotFoundException {
        return getFilePath(uploadPath, file, (file1 -> DigestUtil.md5Hex(FileUtil.extName(file1.getOriginalFilename()) + new Date().getTime()) +
                '.' + FileUtil.extName(file1.getOriginalFilename())));
    }

    /**
     * 文件名生成器
     */
    interface FileNameGenerator {
        /**
         * 生成文件名
         *
         * @param file 文件
         * @return 文件名
         */
        String generator(MultipartFile file);
    }
}
