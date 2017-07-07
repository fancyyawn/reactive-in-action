package top.zhacker.reactive.rxjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.model.Product;
import top.zhacker.reactive.rxjava2.model.User;
import top.zhacker.reactive.rxjava2.repo.ProductRepo;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午12:29
 * Desc 文件描述
 */
@Service
@Slf4j
public class FavoriteReadService {
    
    private final ProductRepo productRepo;
    
    @Autowired
    public FavoriteReadService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    
    public Observable<Product> bestProductFor(User user){
        return findFromFavorites(user)
                .onErrorResumeNext(bestSeller(user))
                .onErrorResumeNext(failProduct());
    }
    
    Observable<Product> findFromFavorites(User user){
        if(Objects.equals(user.getId(), 1L) || Objects.equals(user.getId(), 2L)){
            return Observable.error(new RuntimeException("user.invalid"));
        }else {
            return Observable.just(new Product().setId(1L), new Product().setId(2L));
        }
    }
    
    
    public Observable<Product> bestSeller(User user){
        if(Objects.equals(user.getId(), 1L)){
            return Observable.error(new RuntimeException("user.invalid"));
        }else {
            return Observable.just(new Product().setId(3L), new Product().setId(4L));
        }
    }
    
    public Observable<Product> failProduct(){
        return Observable.just(new Product().setId(-1L));
    }
}
