package top.zhacker.reactive.rxjava2.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2017/6/25 上午12:23
 * Desc 文件描述
 */
@Entity
@Data
@Accessors(chain = true)
@Table(name = "products")
public class Product implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long categoryId;
    
    private Long shopId;
    
    private String name;
    private String mainImage;
    private String details;
    private Integer type;
    private Integer status;
    
    private Date createdAt;
    private Date updatedAt;
    
    public static final Product NULLABLE = new Product().setId(0L);
}
