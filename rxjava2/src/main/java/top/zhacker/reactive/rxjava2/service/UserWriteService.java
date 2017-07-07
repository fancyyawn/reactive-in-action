package top.zhacker.reactive.rxjava2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.model.User;
import top.zhacker.reactive.rxjava2.repo.UserRepo;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午10:41
 * Desc 文件描述
 */
@Service
@Slf4j
public class UserWriteService {
    
    private final UserRepo userRepo;
    
    @Autowired
    public UserWriteService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    
    public Observable<User> register(User user){
        return Observable.zip(
                checkAsync(user, this::checkName),
                checkAsync(user, this::checkMobile),
                checkAsync(user, this::checkEmail),
                (a, b, c)-> userRepo.save(user)
        );
    }
    
    Observable<User> checkAsync(User user, Function<User, User> checker){
        return Observable.fromCallable(()-> checker.apply(user)).subscribeOn(Schedulers.io());
    }
    
    User checkName(User user){
        if(user.getName() == null){
            throw new RuntimeException("user.name.required");
        }
        if(userRepo.findByName(user.getName()) != null){
            throw new RuntimeException("user.name.duplicated");
        }
        return user;
    }
    
    User checkMobile(User user){
        if(user.getMobile() == null){
            throw new RuntimeException("user.mobile.required");
        }
        if(userRepo.findByMobile(user.getMobile()) != null){
            throw new RuntimeException("user.mobile.duplicated");
        }
        return user;
    }
    
    User checkEmail(User user){
        
        if(user.getEmail() == null){
            return user;
        }
        if(userRepo.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("user.email.duplicated");
        }
        return user;
    }
}
