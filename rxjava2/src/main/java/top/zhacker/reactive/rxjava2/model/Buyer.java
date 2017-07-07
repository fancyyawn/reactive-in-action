package top.zhacker.reactive.rxjava2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2017/7/6 下午2:53
 * Desc 文件描述
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "buyers")
public class Buyer {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long userId;
    
    private String address;
    
    private String avatar;
    
    private String birthday;
    
    @Transient
    private User user;
}
