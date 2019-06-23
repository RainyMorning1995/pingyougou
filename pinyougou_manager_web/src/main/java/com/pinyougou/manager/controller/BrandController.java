package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

//    @RequestMapping("/findPage")
//    public PageInfo<TbBrand> findPage(
//        @RequestParam(required = true,defaultValue = "1",name = "pageNo")    Integer pageNo,
//        @RequestParam(required = true,defaultValue = "10",name = "pageSize")    Integer pageSize
//    ) {
//        PageInfo<TbBrand> page = brandService.findPage(pageNo, pageSize);
//        return page;
//    }

    @RequestMapping("/findPage")
    public PageInfo<TbBrand> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return brandService.findPage(pageNo, pageSize);
    }
}
