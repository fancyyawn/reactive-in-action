package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.reactive.rxjava2.model.Order;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午2:37
 * Desc 文件描述
 */
@Repository
public interface OrderRepo extends PagingAndSortingRepository<Order, Long>{
    
}
