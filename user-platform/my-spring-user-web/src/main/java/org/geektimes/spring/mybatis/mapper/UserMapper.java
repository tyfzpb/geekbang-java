package org.geektimes.spring.mybatis.mapper;

import org.geektimes.spring.mybatis.entry.Users;

import java.util.List;

public interface UserMapper {

    List<Users> getUserByName(String name);

    List<Users> getAllUsers();
}
