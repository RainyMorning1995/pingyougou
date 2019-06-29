package com.pinyougou.upload.controller;

import com.pinyougou.FastDFSClient;

public class FastTest {
    public static void main(String[] args) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fastdfs_client.conf");
            String png = fastDFSClient.uploadFile("F:\\JavaWorkPlace\\pingyougou\\pinyougou_shop_web\\src\\main\\webapp\\img\\ad.jpg", "jpg");
            System.out.println(png);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
