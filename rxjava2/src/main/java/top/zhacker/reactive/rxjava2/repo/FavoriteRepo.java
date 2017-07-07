package top.zhacker.reactive.rxjava2.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import top.zhacker.reactive.rxjava2.model.Favorite;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午11:56
 * Desc 文件描述
 */
@Repository
public interface FavoriteRepo extends CrudRepository<Favorite, Long> {
    
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByProductId(Long productId);
}
