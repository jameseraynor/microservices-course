package com.appsdeveloperblog.estore.data;

import com.appsdeveloperblog.estore.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {

  Map<String, User> users = new HashMap<String, User>() {};

  @Override
  public boolean save(User user) {
    boolean isSuccess = true;

    if (!users.containsKey(user.getId())) {
      users.put(user.getId(), user);
    } else {
      isSuccess = false;
    }
    return isSuccess;
  }
}
