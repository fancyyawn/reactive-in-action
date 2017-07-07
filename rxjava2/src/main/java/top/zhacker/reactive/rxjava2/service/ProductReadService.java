package top.zhacker.reactive.rxjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Single;
import io.reactivex.annotations.SchedulerSupport;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.common.SingleEx;
import top.zhacker.reactive.rxjava2.model.Product;
import top.zhacker.reactive.rxjava2.repo.ProductRepo;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午2:43
 * Desc 文件描述
 */
@Service
@Slf4j
public class ProductReadService {
    
    @Setter
    @Autowired
    private ProductRepo productRepo;
    
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<Product> findProductById(Long productId){
        return SingleEx.fromMethod(productRepo::findOne, productId);
    }
}
