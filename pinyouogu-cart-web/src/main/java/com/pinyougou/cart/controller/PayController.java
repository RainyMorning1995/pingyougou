package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.entity.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map createNative(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        TbPayLog tbPayLog = orderService.searchPayLogFromRedis(userId);
        if (tbPayLog != null) {
//            IdWorker idWorker = new IdWorker(0, 1);
            return weixinPayService.createNative(tbPayLog.getOutTradeNo(),tbPayLog.getTotalFee()+"");
        }
        return null;
    }





    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int count = 0;
        while (true){

            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null){
                result = new Result(false,"支付出错");
                break;
            }

            if (map.get("trade_state").equals("SUCCESS")){
                result = new Result(true,"支付成功");
                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));
                break;
            }

            count++;
            if (count >= 10){
                result =  new Result(false,"超时");
                break;
            }

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;

    }
}
