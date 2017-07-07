package top.zhacker.reactive.rxjava2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import top.zhacker.reactive.rxjava2.model.Buyer;
import top.zhacker.reactive.rxjava2.model.User;
import top.zhacker.reactive.rxjava2.service.UserReadService;
import top.zhacker.reactive.rxjava2.service.UserWriteService;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午8:16
 * Desc 文件描述
 */
@RestController
@RequestMapping("/v1/users")
@Slf4j
public class Users {
    
    private final UserReadService userReadService;
    
    private final UserWriteService userWriteService;
    
    @Autowired
    public Users(UserReadService userReadService, UserWriteService userWriteService) {
        this.userReadService = userReadService;
        this.userWriteService = userWriteService;
    }
    
    
    /**
     * http :8080/api/users/loginSequential password==1234 token==18667045537
     * @param token
     * @param password
     * @return
     */
    @RequestMapping("/loginParallel")
    public User loginParallel(String token, String password){
        return userReadService.loginParallel(token, password).blockingFirst(User.NULLABLE);
    }
    
    @RequestMapping("/loginSequential")
    public User loginSequential(String token, String password){
        return userReadService.loginSequential(token, password).blockingFirst(User.NULLABLE);
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User register(@RequestBody User user){
        return userWriteService.register(user)
                .doOnNext(response-> log.info("register successfully, resp={}", response))
                .flatMap(registerResp -> userReadService.loginSequential(user.getName(), user.getPassword()))
                .doOnNext(resp -> log.info("login successfully, resp={}", resp))
                .blockingFirst();
    }
    
    @RequestMapping("/list")
    public List<User> list(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize){
        
        return userReadService.list(pageNo, pageSize).toList().blockingGet();
    }
    
    @RequestMapping("/{id}")
    public Buyer findBuyer(@PathVariable("id") Long userId){
        return userReadService.findBuyerByUserId(userId).blockingGet();
    }
}
