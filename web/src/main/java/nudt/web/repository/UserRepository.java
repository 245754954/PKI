package nudt.web.repository;

import nudt.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//通过继承JpaRepository来完成对数据库的操作的
//第一个参数是数据库表对应的实体类，第二个是该数据库表的主键类型
//如果使用JPA来操作数据库，通过继承JpaRepository来完成常见的增删改查
public interface UserRepository extends JpaRepository<User,Integer> {

}
