package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.utils.CookieUtil;
import com.pinyougou.entity.Cart;
import com.pinyougou.entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    /**
     * 获取购物车列表
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request){
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (StringUtils.isEmpty(cartListString)){
            cartListString = "[]";
        }
        List<Cart> cartList = JSON.parseArray(cartListString, Cart.class);

        return cartList;
    }


    /**
     * 添加商品到购物车列表
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        try {
            List<Cart> cartList = findCartList(request);
            CookieUtil.setCookie(request,response,"cartList",JSON.toJSONString(cartList),3600*24);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

}
