package com.pinyougou;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("F:\\JavaWorkPlace\\pingyougou\\itheima-freemarker\\src\\main\\resources\\template"));

        configuration.setDefaultEncoding("utf-8");

        Template template = configuration.getTemplate("demo.ftl");
        Map map = new HashMap<>();

        List goodsList=new ArrayList();
        Map goods1=new HashMap();
        goods1.put("name", "苹果");
        goods1.put("price", 5.8);
        Map goods2=new HashMap();
        goods2.put("name", "香蕉");
        goods2.put("price", 2.5);
        Map goods3=new HashMap();
        goods3.put("name", "橘子");
        goods3.put("price", 3.2);
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        map.put("goodsList", goodsList);


        FileWriter out = new FileWriter(new File("F:\\JavaWorkPlace\\pingyougou\\itheima-freemarker\\src\\main\\resources\\html\\test.html"));
        template.process(map,out);
        out.close();


    }
}
