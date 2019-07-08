package com.pinyougou;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("F:\\JavaWorkPlace\\pingyougou\\itheima-freemarker\\src\\main\\resources\\template"));

        configuration.setDefaultEncoding("utf-8");

        Template template = configuration.getTemplate("demo.ftl");
        Map map = new HashMap<>();


        map.put("date",new Date());
        map.put("point",1001011101);


        FileWriter out = new FileWriter(new File("F:\\JavaWorkPlace\\pingyougou\\itheima-freemarker\\src\\main\\resources\\html\\test.html"));
        template.process(map,out);
        out.close();


    }
}
