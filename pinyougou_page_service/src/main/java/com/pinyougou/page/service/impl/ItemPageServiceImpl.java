package com.pinyougou.page.service.impl;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public class ItemPageServiceImpl implements ItemPageService {


    @Value("${pageDir}")
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

    private void genHtml(String templateName, TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }
}
