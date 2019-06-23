package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectAll();
    }

    @Override
    public PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbBrand> tbBrands = tbBrandMapper.selectAll();
        PageInfo<TbBrand> pageInfo = new PageInfo<>(tbBrands);
        String s = JSON.toJSONString(pageInfo);
        PageInfo<TbBrand> pageInfo1 = JSON.parseObject(s, PageInfo.class);
        return pageInfo1;
    }


}
