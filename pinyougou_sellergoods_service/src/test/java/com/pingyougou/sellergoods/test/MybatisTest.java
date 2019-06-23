package com.pingyougou.sellergoods.test;

import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
@RunWith(SpringRunner.class)
public class MybatisTest {

    @Autowired
    private TbBrandMapper mapper;


    @Test
    public void test() {
        List<TbBrand> tbBrands = mapper.selectByExample(null);
        for (TbBrand tbBrand : tbBrands) {
            System.out.println(tbBrand.getId()+":"+tbBrand.getName());
        }
    }

    @Test
    public void test2() {
        TbBrand tbBrand = new TbBrand();
        tbBrand.setId(99L);
        tbBrand.setName("kkkkkkkkkkkkkkkk");
        mapper.insert(tbBrand);
    }

    @Test
    public void test03() {
        TbBrand tbBrand = new TbBrand();
        tbBrand.setId(99L);
        tbBrand.setName("老铁不留");
        mapper.updateByPrimaryKey(tbBrand);
    }

    @Test
    public void test04() {
        mapper.deleteByPrimaryKey(99L);
    }
}
