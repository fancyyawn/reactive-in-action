package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.reactive.rxjava2.model.Product;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午12:25
 * Desc 文件描述
 */
@Repository
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {
    
}
