package top.zhacker.reactive.rxjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.model.Order;
import top.zhacker.reactive.rxjava2.repo.OrderRepo;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午2:38
 * Desc 文件描述
 */
@Slf4j
@Service
public class OrderWriteService {
    
    private final OrderRepo orderRepo;
    
    private final UserReadService userReadService;
    
    private final ProductReadService productReadService;
    
    @Autowired
    public OrderWriteService(OrderRepo orderRepo, UserReadService userReadService, ProductReadService productReadService) {
        this.orderRepo = orderRepo;
        this.userReadService = userReadService;
        this.productReadService = productReadService;
    }
    
    public Single<Order> createOrder(Order order){
        return Single.zip(
                userReadService.findById(order.getBuyerId()).subscribeOn(Schedulers.io()),
                productReadService.findProductById(order.getProductId()).subscribeOn(Schedulers.io()),
                (user, product)-> orderRepo.save(order)
        );
    }
    
}
