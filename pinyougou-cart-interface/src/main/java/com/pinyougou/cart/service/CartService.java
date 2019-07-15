package com.pinyougou.cart.service;

import com.pinyougou.entity.Cart;

import java.util.List;

public interface CartService {


    public List<Cart> mergeCartList(List<Cart> cookieList,List<Cart> redisList);

    /**
     * 向购物车添加订单
     * @param cartList 订单列表
     * @param itemId 添加物品id
     * @param num 添加数量
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);


    /**
     * 从redis中取出购物车
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到购物车
     * @param username
     * @param cartList
     */
    public void saveCartListToRedis(String username,List<Cart> cartList);


}
