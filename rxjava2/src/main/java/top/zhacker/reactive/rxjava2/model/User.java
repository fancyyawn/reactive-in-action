package top.zhacker.reactive.rxjava2.model;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2017/6/24 下午5:05
 * Desc 文件描述
 */
@Entity
@Data
@Accessors(chain = true)
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private Date createdAt;
    private Date updatedAt;
    
    public static final User NULLABLE = new User().setId(0L);
}
