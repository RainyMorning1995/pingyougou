package com.pingyougou.sellergoods.test;

import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class CommonMapperTest {
    @Autowired
    private TbBrandMapper brandMapper;

    @Test
    public void test() {
        List<TbBrand> tbBrands = brandMapper.selectAll();
        for (TbBrand tbBrand : tbBrands) {
            System.out.println(tbBrand);
        }
        //System.out.println(brandMapper);
    }

    @Test
    public void test01() {
        TbBrand tbBrand = new TbBrand();
       // tbBrand.setId(99L);
        tbBrand.setName("FFFFFF");
       // tbBrand.setFirstChar("k");
//        brandMapper.insert(tbBrand);
//        System.out.println("===========");
//        TbBrand tbBrand1 = new TbBrand();
//        tbBrand.setName("HHHHHH");
//        tbBrand.setFirstChar("k");
        brandMapper.insertSelective(tbBrand);
    }

    @Test
    public void test02() {
        brandMapper.deleteByPrimaryKey(103L);
        System.out.println("===========");

        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(101L);
        ids.add(102L);
        criteria.andIn("id",ids);
        brandMapper.deleteByExample(example);
        System.out.println("==========");
        TbBrand tbBrand = new TbBrand();
        tbBrand.setId(104L);
        brandMapper.delete(tbBrand);
    }

    @Test
    public void test05() {
//        List<TbBrand> tbBrands = brandMapper.selectAll();
//        for (TbBrand tbBrand : tbBrands) {
//            System.out.println(tbBrand);
//        }
//        TbBrand tbBrand = new TbBrand();
//        tbBrand.setFirstChar("C");
//        tbBrand.setId(12L);
//        List<TbBrand> select = brandMapper.select(tbBrand);
//        for (TbBrand brand : select) {
//            System.out.println(brand);
//        }

        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        List<Long> ids = new ArrayList<>();
        ids.add(11L);
        ids.add(14L);
        criteria.andIn("id",ids);
        List<TbBrand> tbBrands = brandMapper.selectByExample(example);
        for (TbBrand tbBrand : tbBrands) {
            System.out.println(tbBrand);
        }

    }


}
