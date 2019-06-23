package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface BrandService {

    List<TbBrand> findAll();

    PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize);
}
