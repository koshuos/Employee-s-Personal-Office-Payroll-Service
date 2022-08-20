package com.internal.service;


import com.internal.model.auth.User;

public interface UserDetailsDao {

  User findUserByUsername(String username);

}
