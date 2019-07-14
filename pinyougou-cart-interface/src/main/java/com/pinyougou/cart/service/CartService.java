package com.pinyougou.cart.service;

import com.pinyougou.entity.Cart;

import java.util.List;

public interface CartService {

    /**
     * 向购物车添加订单
     * @param cartList 订单列表
     * @param itemId 添加物品id
     * @param num 添加数量
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

}
