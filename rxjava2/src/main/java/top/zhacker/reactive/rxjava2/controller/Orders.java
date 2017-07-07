package top.zhacker.reactive.rxjava2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import top.zhacker.reactive.rxjava2.model.Order;
import top.zhacker.reactive.rxjava2.service.OrderWriteService;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午2:38
 * Desc 文件描述
 */
@RestController
@RequestMapping("/v1/orders")
public class Orders {
    
    private final OrderWriteService orderWriteService;
    
    @Autowired
    public Orders(OrderWriteService orderWriteService) {
        this.orderWriteService = orderWriteService;
    }
    
    
    @RequestMapping(method = RequestMethod.POST)
    public Order createOrder(@RequestBody Order order){
        return orderWriteService.createOrder(order).blockingGet();
    }
}
