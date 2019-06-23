package com.pingyougou.sellergoods.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class PageTest {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Test
    public void test() {
        PageHelper.startPage(1,3);
        List<TbBrand> tbBrands = tbBrandMapper.selectAll();
//        for (TbBrand tbBrand : tbBrands) {
//            System.out.println(tbBrand.getId()+":"+tbBrand.getName());
//        }
        PageInfo<TbBrand> tbBrandPageInfo = new PageInfo<>(tbBrands);
        System.out.println(tbBrandPageInfo);
        //System.out.println(tbBrandPageInfo.getList());
        for (TbBrand tbBrand : tbBrandPageInfo.getList()) {
            System.out.println(tbBrand.getId()+":"+tbBrand.getName());
        }

    }
}
