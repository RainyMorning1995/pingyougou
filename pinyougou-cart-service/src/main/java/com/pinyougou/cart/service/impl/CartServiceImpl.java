package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.entity.Cart;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        String sellerId = tbItem.getSellerId();
        Cart cart = findCardBySellerId(cartList,sellerId);
        if (cart != null) {
            //购物车有商家的id


            Long tbItemId = tbItem.getId();
            TbOrderItem tbOrderItem = findOrderItemByTbItemId(cart,tbItemId);
            if (tbOrderItem != null) {
                //购物车里有该商品订单
                tbOrderItem.setNum(tbOrderItem.getNum() + num);

                double v = tbOrderItem.getNum() * tbOrderItem.getPrice().doubleValue();
                tbOrderItem.setTotalFee(new BigDecimal(v));

                //如果订单商品数量为零，删除该商品
                if (tbOrderItem.getNum() == 0){
                    cart.getOrderItemList().remove(tbOrderItem);
                }

                //如果长度为零，那么用户没有购买该商家商品，删除该商家的对象
                if (cart.getOrderItemList().size() == 0){
                    cartList.remove(cart);
                }

            }else {
                //购物车里没有商品订单
                cart = new Cart();
                //购物车没有添加该商家id
                cart.setSellerId(sellerId);
                cart.setSellerName(tbItem.getSeller());
                List<TbOrderItem> orderItemList = new ArrayList<>();
                TbOrderItem tbOrderItemNew = new TbOrderItem();
                tbOrderItemNew.setGoodsId(tbItem.getGoodsId());
                tbOrderItemNew.setItemId(itemId);
                tbOrderItemNew.setPicPath(tbItem.getImage());
                tbOrderItemNew.setNum(num);
                tbOrderItemNew.setPrice(tbItem.getPrice());
                double v = num * tbItem.getPrice().doubleValue();
                tbOrderItemNew.setTotalFee(new BigDecimal(v));
                orderItemList.add(tbOrderItemNew);
                cart.setOrderItemList(orderItemList);
                cartList.add(cart);

            }

        }else {
            cart = new Cart();
            //购物车没有添加该商家id
            cart.setSellerId(sellerId);
            cart.setSellerName(tbItem.getSeller());
            List<TbOrderItem> orderItemList = new ArrayList<>();
            TbOrderItem tbOrderItemNew = new TbOrderItem();
            tbOrderItemNew.setGoodsId(tbItem.getGoodsId());
            tbOrderItemNew.setItemId(itemId);
            tbOrderItemNew.setPicPath(tbItem.getImage());
            tbOrderItemNew.setNum(num);
            tbOrderItemNew.setPrice(tbItem.getPrice());
            double v = num * tbItem.getPrice().doubleValue();
            tbOrderItemNew.setTotalFee(new BigDecimal(v));
            orderItemList.add(tbOrderItemNew);
            cart.setOrderItemList(orderItemList);
            cartList.add(cart);

        }


        return cartList;
    }

    /**
     *
     * @param cart
     * @param tbItemId
     * @return
     */
    private TbOrderItem findOrderItemByTbItemId(Cart cart, Long tbItemId) {
        List<TbOrderItem> orderItemList = cart.getOrderItemList();
        for (TbOrderItem tbOrderItem : orderItemList) {
            if (tbOrderItem.getItemId().longValue() == tbItemId) {
                return tbOrderItem;
            }
        }
            return null;
    }


    /**
     *
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart findCardBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }
}
