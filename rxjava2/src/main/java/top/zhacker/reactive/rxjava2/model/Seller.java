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
 * Time 2017/7/6 下午3:13
 * Desc 文件描述
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sellers")
public class Seller {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long userId;
    
    private Long shopId;
    
    private String alipayAccount;
    
    private String bankAccount;
    
    @Transient
    private User user;
}
