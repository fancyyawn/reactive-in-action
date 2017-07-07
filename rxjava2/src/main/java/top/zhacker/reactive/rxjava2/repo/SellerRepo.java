package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.reactive.rxjava2.model.Seller;


/**
 * Created by zhacker.
 * Time 2017/7/6 下午3:17
 * Desc 文件描述
 */
@Repository
public interface SellerRepo extends PagingAndSortingRepository<Seller, Long> {
    
    Seller findByUserId(Long userId);
}
