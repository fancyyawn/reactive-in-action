package top.zhacker.reactive.rxjava2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import top.zhacker.reactive.rxjava2.model.Product;
import top.zhacker.reactive.rxjava2.service.FavoriteReadService;
import top.zhacker.reactive.rxjava2.service.UserReadService;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午11:37
 * Desc 文件描述
 */
@RestController
@RequestMapping("/v1/products")
public class Favorites {
    
    private final FavoriteReadService favoriteReadService;
    
    private final UserReadService userReadService;
    
    @Autowired
    public Favorites(FavoriteReadService favoriteReadService, UserReadService userReadService) {
        this.favoriteReadService = favoriteReadService;
        this.userReadService = userReadService;
    }
    
    @RequestMapping("/best")
    public List<Product> bestProductFor(Long userId){
        return userReadService.findById(userId)
                .flatMapObservable(favoriteReadService::bestProductFor)
                .toList().blockingGet();
    }
}
