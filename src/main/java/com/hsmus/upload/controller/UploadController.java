package com.hsmus.upload.controller;

import com.hsmus.upload.entity.R;
import com.hsmus.upload.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mustard <jianzhang@dgut.edu.cn>
 * @date 2021/1/22
 * @time 14:36
 */
@CrossOrigin
@RestController
@RequestMapping("app/upload")
public class UploadController {

    @Value("${my.upload-path}")
    private String uploadPath;

    @Value("#{'${my.allow-ext}'.split(',')}")
    private List<String> allowExt;

    @Value("#{${my.allow-max-size}}")
    private String allowMaxSize;

    @GetMapping("")
    public R test() {
        return R.ok(uploadPath);
    }

    /**
     * 单文件上传
     * @param file
     * @return
     */
    @PostMapping("single")
    public R singleUpload(@RequestParam(value = "file", required = false) MultipartFile file) {
        UploadUtils.setAllowExt(allowExt);
        UploadUtils.setAllowMaxSize(Long.valueOf(allowMaxSize));
        try {
            String path = receiveUpload(file);
            return R.ok("success").put("data", path);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 多文件上传
     * @param files
     * @return
     */
    @PostMapping("multi")
    public R multiUpload(@RequestParam(value = "file", required = false) MultipartFile[] files) {
        UploadUtils.setAllowExt(allowExt);
        UploadUtils.setAllowMaxSize(Long.valueOf(allowMaxSize));
        if (files == null || files.length == 0) {
            return R.error("请上传文件");
        }
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String path = receiveUpload(file);
                paths.add(path);
            } catch (IOException e) {
                e.printStackTrace();
                return R.error(e.getMessage());
            }
        }
        return R.ok().put("data", paths);
    }

    private String receiveUpload(MultipartFile file) throws IOException {
        Boolean check = UploadUtils.check(file);
        if (check) {
            String path = UploadUtils.getFilePath(uploadPath, file);
            File dest = new File(path);
            file.transferTo(dest);
            return uploadPath + dest.getName();
        }
        return null;
    }

}
