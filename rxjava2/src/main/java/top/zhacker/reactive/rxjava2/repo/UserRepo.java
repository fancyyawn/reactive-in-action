package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.reactive.rxjava2.model.User;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午5:07
 * Desc 文件描述
 */
@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    
    User findByName(String name);
    
    User findByMobile(String mobile);
    
    User findByEmail(String email);
}
