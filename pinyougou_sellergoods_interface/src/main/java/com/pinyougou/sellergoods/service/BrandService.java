package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface BrandService {

    List<TbBrand> findAll();

    PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand tbBrand);

    void add(TbBrand tbBrand);

    void update(TbBrand tbBrand);

    TbBrand findOne(Long id);

    void delete(Long[] ids);
}
