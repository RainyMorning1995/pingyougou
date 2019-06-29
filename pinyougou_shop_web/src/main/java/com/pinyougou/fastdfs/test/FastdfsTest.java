package com.pinyougou.fastdfs.test;

import com.pinyougou.FastDFSClient;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;

public class FastdfsTest {

    @Test
    public void uploadFastdfs() throws Exception{
        ClientGlobal.init("classpath:config/fastdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(connection, null);
        String[] strings = storageClient.upload_file("F:\\JavaWorkPlace\\pingyougou\\pinyougou_shop_web\\src\\main\\java\\com\\pinyougou\\fastdfs\\test\\test001.jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }

    }


   @Test
    public void test02() throws Exception{
       try {
           FastDFSClient fastDFSClient = new FastDFSClient("classpath: config/fastdfs_client.conf");
           String png = fastDFSClient.uploadFile("F:\\JavaWorkPlace\\pingyougou\\pinyougou_shop_web\\src\\main\\webapp\\img\\ad.jpg", "jpg");
           System.out.println(png);
       } catch (Exception e) {
           e.printStackTrace();
       }



    }

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

