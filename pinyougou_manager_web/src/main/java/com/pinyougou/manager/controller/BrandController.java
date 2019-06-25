package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

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
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbBrand tbBrand
    ) {
        return brandService.findPage(pageNo, pageSize,tbBrand);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand){
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage("添加失败");
        try {
            brandService.add(tbBrand);
            result.setSuccess(true);
            result.setMessage("添加成功");
        }catch (Exception e){

        }
        return result;
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        Result result = new Result();
        result.setMessage("更新失败");
        result.setSuccess(false);
        try {
            brandService.update(tbBrand);
            result.setMessage("更新成功");
            result.setSuccess(true);
        } catch (Exception e) {

        }
        return result;
    }

    @RequestMapping("/findOne/{id}")
    public TbBrand findOne(@PathVariable(value = "id") Long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        Result result = new Result();
        result.setMessage("删除失败");
        result.setSuccess(false);
        try {
            brandService.delete(ids);
            result.setMessage("删除成功");
            result.setSuccess(true);
        } catch (Exception e) {

        }
        return result;
    }


}
