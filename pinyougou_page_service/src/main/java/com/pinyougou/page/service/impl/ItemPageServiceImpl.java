package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {


    @Value("${PageDir}")
    private String pageDir;

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;


    @Override
    public void genItemHtml(Long goodsId) {
        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
        genHtml("Item.ftl",tbGoods,tbGoodsDesc);
    }

    @Override
    public void deleteById(Long[] longs) {
        try {
            for (Long aLong : longs) {
                FileUtils.forceDelete(new File(pageDir + aLong + ".html"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genHtml(String templateName, TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {

        Writer out = null;
        try {
            Configuration configuration = configurer.getConfiguration();
            Template template = configuration.getTemplate(templateName);
            TbItemCat tbItemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
            TbItemCat tbItemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
            TbItemCat tbItemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());



            Map map = new HashMap();
            map.put("tbGoods",tbGoods);
            map.put("tbGoodsDesc",tbGoodsDesc);
            map.put("tbItemCat1",tbItemCat1.getName());
            map.put("tbItemCat2",tbItemCat2.getName());
            map.put("tbItemCat3",tbItemCat3.getName());

            Example example = new Example(TbItem.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("goodsId",tbGoods.getId());
            criteria.andEqualTo("status",1);
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            map.put("skuList",itemList);

            //解决输出HTML乱码
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pageDir + tbGoods.getId() + ".html"), "UTF-8"));
            //out = new FileWriter(new File(pageDir + tbGoods.getId() + ".html"));
            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }






    }
}
