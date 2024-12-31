package com.appsdeveloperblog.estore.data;

import com.appsdeveloperblog.estore.model.User;

public interface UserRepository {

    boolean save(User user);
}
