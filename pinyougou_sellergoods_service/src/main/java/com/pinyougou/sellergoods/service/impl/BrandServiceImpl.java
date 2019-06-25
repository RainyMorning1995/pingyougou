package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
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
    public PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand tbBrand) {
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        if (tbBrand != null) {
            if (StringUtils.isNotBlank(tbBrand.getName())){
                criteria.andLike("name","%"+tbBrand.getName()+"%");
            }
            if (StringUtils.isNoneBlank(tbBrand.getFirstChar())){
                criteria.andLike("firstChar","%"+tbBrand.getFirstChar()+"%");
            }
        }
        PageHelper.startPage(pageNo,pageSize);
        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(example);
        PageInfo<TbBrand> pageInfo = new PageInfo<>(tbBrands);
        String s = JSON.toJSONString(pageInfo);
        PageInfo<TbBrand> pageInfo1 = JSON.parseObject(s, PageInfo.class);
        return pageInfo1;
    }

    @Override
    public void add(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    @Override
    public void update(TbBrand tbBrand) {
        tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        tbBrandMapper.deleteByExample(example);
    }


}
