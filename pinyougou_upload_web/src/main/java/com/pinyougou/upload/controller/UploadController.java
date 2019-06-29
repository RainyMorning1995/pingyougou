package com.pinyougou.upload.controller;

import com.pinyougou.FastDFSClient;
import com.pinyougou.entity.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping("/uploadFile")
    @CrossOrigin(origins = {"http://localhost:9081","http://localhost:9082","http://localhost:9080"},allowCredentials = "true")
    public Result upload(@RequestParam(value = "file")MultipartFile file){

        try {
            String originalFilename = file.getOriginalFilename();
            String extend = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            byte[] bytes = file.getBytes();
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fastdfs_client.conf");
            String path = fastDFSClient.uploadFile(bytes, extend);
            String realPath="http://192.168.25.133/"+path;
            return new Result(true,realPath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }


    }


}
