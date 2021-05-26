package org.geektimes.mybatis.spring.mapper;

import org.geektimes.mybatis.spring.entry.Users;

import java.util.List;


public interface UserMapper {

    List<Users> getUserByName(String name);

    List<Users> getAllUsers();
}
