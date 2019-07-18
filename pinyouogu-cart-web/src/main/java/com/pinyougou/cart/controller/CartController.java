package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.utils.CookieUtil;
import com.pinyougou.entity.Cart;
import com.pinyougou.entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response){
        //获取用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        //如果是匿名用户则从cookie取
        if ("anonymousUser".equals(name)){
            //

            String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
            if (StringUtils.isEmpty(cartListString)){
                cartListString = "[]";
            }
            List<Cart> cartList = JSON.parseArray(cartListString, Cart.class);

            return cartList;
        }else {
            //不是匿名用户,则从缓存当中取
            List<Cart> cartListFromRedis = cartService.findCartListFromRedis(name);
            if (cartListFromRedis == null) {
                cartListFromRedis = new ArrayList<>();
            }

            String cartListFromCookieString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
            if (StringUtils.isEmpty(cartListFromCookieString)){
                cartListFromCookieString = "[]";
            }

            List<Cart> cartListFromCookie = JSON.parseArray(cartListFromCookieString, Cart.class);
            if (cartListFromCookie.size() > 0){
                List<Cart> mergeCartList = cartService.mergeCartList(cartListFromCookie, cartListFromRedis);
                cartService.saveCartListToRedis(name,mergeCartList);
                CookieUtil.deleteCookie(request, response,"cartList");
                return mergeCartList;
            }


            return cartListFromRedis;
        }

    }


    /**
     * 添加商品到购物车列表
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
//    @CrossOrigin(origins = "http://localhost:9094",allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9094");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            //获取用户名
            String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
            //未登录则放入cookie
            if ("anonymousUser".equals(name)){
                List<Cart> cartList = findCartList(request,response);
                cartList = cartService.addGoodsToCartList(cartList, itemId, num);
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList),3600*24,"UTF-8");
                return new Result(true,"添加成功");
            }else {
                //已支付放入缓存redis
                List<Cart> cartListFromRedis = cartService.findCartListFromRedis(name);
                List<Cart> carts = cartService.addGoodsToCartList(cartListFromRedis, itemId, num);
                cartService.saveCartListToRedis(name,carts);
                return new Result(true,"添加成功");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

}
