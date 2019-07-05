package com.pinyougou.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.es.dao.ItemDao;
import com.pinyougou.es.service.ItemService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao dao;

    @Autowired
    private TbItemMapper mapper;

//    @Override
//    public void ImportDataToEs() {
//        TbItem tbItem = new TbItem();
//        tbItem.setStatus("1");
//        List<TbItem> select = mapper.select(tbItem);
//        for (TbItem item : select) {
//            String spec = item.getSpec();
//            if (StringUtils.isNoneBlank(spec)){
//                Map map = JSON.parseObject(spec, Map.class);
//                tbItem.setSpecMap(map);
//            }
//        }
//
//
//
//        dao.saveAll(select);
//    }


    public void ImportDataToEs() {
        //1.使用dao 根据条件查询数据库的中(tb_item)的数据
        //select * from tb_item where status=1
        TbItem condition = new TbItem();
        condition.setStatus("1");//状态为正常的数据
        List<TbItem> itemList = mapper.select(condition);

        //循环遍历集合  获取里面的规格的数据 字符串 {"网络":"移动4G","机身内存":"16G"}
        for (TbItem tbItem : itemList) {
            String spec = tbItem.getSpec(); //{"网络":"移动4G","机身内存":"16G"}
            //转成json对象（Map对象）
            Map map = JSON.parseObject(spec, Map.class);
            //map对象设置 规格的属性中specMap
            tbItem.setSpecMap(map);
        }
        //2.使用es的dao 保存数据到es服务器中
        dao.saveAll(itemList);
    }
}
