package top.zhacker.reactive.rxjava2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午11:51
 * Desc 文件描述
 */
@Entity
@Data
@Accessors(chain = true)
@Table(name = "favorites")
public class Favorite {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long userId;
    
    private Long productId;
    
    private Integer stars;
    
    public enum Stars{
        ZERO,ONE,TWO,THREE,FOUR,FIVE
    }
}
