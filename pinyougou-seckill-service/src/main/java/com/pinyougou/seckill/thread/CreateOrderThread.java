package com.pinyougou.seckill.thread;

import com.pinyougou.SysConstants;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.pojo.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

public class CreateOrderThread {

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;


    @Async
    public void handleOrder() {
        try {
            System.out.println("模拟订单开始处理------" + Thread.currentThread().getName());
            Thread.sleep(10000);
            System.out.println("模拟订单处理结束------" + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SysConstants.SEC_KILL_USER_ORDER_LIST).rightPop();
        if (seckillStatus != null) {
            //从nosql数据库中获取商品
            TbSeckillGoods killgoods = (TbSeckillGoods) redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).get(seckillStatus.getGoodsId());
            //将这个商品的库存减少
            killgoods.setStockCount(killgoods.getStockCount() - 1);//减少

            redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).put(seckillStatus.getGoodsId(), killgoods);

            if (killgoods.getStockCount() <= 0) {//如果已经被秒光
                seckillGoodsMapper.updateByPrimaryKey(killgoods);//同步到数据库
                redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).delete(seckillStatus.getGoodsId());//将redis中的该商品清除掉
            }
            //创建订单
            long orderId = idWorker.nextId();

            TbSeckillOrder seckillOrder = new TbSeckillOrder();

            seckillOrder.setId(orderId);//设置订单的ID 这个就是out_trade_no
            seckillOrder.setCreateTime(new Date());//创建时间
            seckillOrder.setMoney(killgoods.getCostPrice());//秒杀价格  价格
            seckillOrder.setSeckillId(seckillStatus.getGoodsId());//秒杀商品的ID
            seckillOrder.setSellerId(killgoods.getSellerId());
            seckillOrder.setUserId(seckillStatus.getUserId());//设置用户ID
            seckillOrder.setStatus("0");//状态 未支付
            //将构建的订单保存到redis中
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).put(seckillStatus.getUserId(), seckillOrder);
            redisTemplate.boundHashOps(SysConstants.SEC_USER_QUEUE_FLAG_KEY).delete(seckillStatus.getUserId());
        }
    }



}
