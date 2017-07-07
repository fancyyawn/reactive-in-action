package top.zhacker.reactive.rxjava2.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午2:34
 * Desc 文件描述
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "orders")
public class Order implements Serializable{
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long buyerId;
    
    private Long productId;
    
    private String productJson;
    
    private Integer amount;
    
    private Integer price;
    
    private Integer fee;
}
