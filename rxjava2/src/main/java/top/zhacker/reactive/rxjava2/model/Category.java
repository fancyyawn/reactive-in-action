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
 * Time 2017/6/25 上午12:27
 * Desc 文件描述
 */
@Entity
@Data
@Accessors(chain = true)
@Table(name = "categories")
public class Category implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long parentId;
    
    
}
