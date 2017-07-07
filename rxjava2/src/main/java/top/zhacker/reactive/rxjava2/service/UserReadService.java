package top.zhacker.reactive.rxjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.common.SingleEx;
import top.zhacker.reactive.rxjava2.model.Buyer;
import top.zhacker.reactive.rxjava2.model.Seller;
import top.zhacker.reactive.rxjava2.model.User;
import top.zhacker.reactive.rxjava2.repo.BuyerRepo;
import top.zhacker.reactive.rxjava2.repo.SellerRepo;
import top.zhacker.reactive.rxjava2.repo.UserRepo;

import static io.reactivex.Observable.fromCallable;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午5:11
 * Desc 文件描述
 */
@Slf4j
@Service
public class UserReadService {
    
    private final UserRepo userRepo;
    
    private final BuyerRepo buyerRepo;
    
    private final SellerRepo sellerRepo;
    
    @Autowired
    public UserReadService(UserRepo userRepo, BuyerRepo buyerRepo, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.buyerRepo = buyerRepo;
        this.sellerRepo = sellerRepo;
    }
    
    public Single<Buyer> findBuyerByUserId(Long userId){
        return Single.zip(
                SingleEx.fromMethod(buyerRepo::findByUserId, userId).subscribeOn(Schedulers.io()),
                SingleEx.fromMethod(userRepo::findOne, userId).subscribeOn(Schedulers.io()),
                (buyer, user) -> { return buyer.setUser(user); }
        );
    }
    
    public Single<Seller> findSellerByUserId(Long userId){
        return Single.zip(
                SingleEx.fromMethod(sellerRepo::findByUserId, userId).subscribeOn(Schedulers.io()),
                SingleEx.fromMethod(userRepo::findOne, userId).subscribeOn(Schedulers.io()),
                (seller, user) -> { return seller.setUser(user); }
        );
    }
    
    public Single<User> findById(Long userId){
        return SingleEx.fromMethod(userRepo::findOne, userId);
    }
    
    /**
     * 根据名称、手机号、邮箱等来查找
     * @param token
     * @param password
     * @return
     */
    public Observable<User> loginParallel(String token, String password){
        return Observable.merge(
                    findUserByTokenAsync(token, userRepo::findByName),
                    findUserByTokenAsync(token, userRepo::findByMobile),
                    findUserByTokenAsync(token, userRepo::findByEmail))
                .filter(user -> user.getPassword().equals(password))
                .doOnNext(user -> user.setPassword(null));
    }
    
    public Observable<User> loginSequential(String token, String password){
        return Observable.concat(
                    findUserByTokenAsync(token, userRepo::findByName),
                    findUserByTokenAsync(token, userRepo::findByMobile),
                    findUserByTokenAsync(token, userRepo::findByEmail))
                .filter(user -> {
                    boolean passwordRight = user.getPassword().equals(password);
                    if(! passwordRight){
                        log.info("fail to login for user={}, because password error", user.getName());
                    }
                    return passwordRight;
                })
                .doOnNext(user -> user.setPassword(null));
    }
    
    public Observable<User> findUserByTokenAsync(String token, Function<String, User> finder){
        return fromCallable(()-> findUserByToken(token, finder))
                .filter(user -> ! user.equals(User.NULLABLE))
                .subscribeOn(Schedulers.io());
    }
    
    public User findUserByToken(String token, Function<String, User> finder){
        return  Optional.ofNullable(finder.apply(token)).orElse(User.NULLABLE);
    }
    
    public Observable<User> list(Integer pageNo, Integer pageSize){
        return Observable.defer(
                ()-> Observable.fromIterable(userRepo.findAll())
        );
    }
}
