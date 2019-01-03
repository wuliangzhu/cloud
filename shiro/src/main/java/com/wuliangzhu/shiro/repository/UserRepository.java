package com.wuliangzhu.shiro.repository;

import com.wuliangzhu.shiro.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * 用来对User进行CRUD
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from User u where u.userName = :userName")
    User findUserByUserName(@Param("userName") String userName);
}
