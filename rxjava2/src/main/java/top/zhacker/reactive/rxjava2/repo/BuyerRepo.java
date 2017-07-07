package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.reactive.rxjava2.model.Buyer;


/**
 * Created by zhacker.
 * Time 2017/7/6 下午2:55
 * Desc 文件描述
 */
@Repository
public interface BuyerRepo extends PagingAndSortingRepository<Buyer, Long> {
    
    Buyer findByUserId(Long userId);
}
