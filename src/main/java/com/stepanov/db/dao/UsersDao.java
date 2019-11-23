package com.stepanov.db.dao;

import com.stepanov.db.model.User;

import java.util.List;

public interface UsersDao extends CrudDao<User> {

    List<User> findAllByFirstName(String firstName);
}
