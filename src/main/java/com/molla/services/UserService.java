package com.molla.services;

import com.molla.exciptions.UserNotFoundException;
import com.molla.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    public List<User> listAll();

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword);

    public User save(User user);

    public boolean isEmailUnique(Integer id, String email);

    public User get(Integer id) throws UserNotFoundException;

    public void delete(Integer id) throws UserNotFoundException;

    public void updateUserEnabledStatus(Integer id, boolean enabled);

    public User getByEmail(String email);

    public User updateAccount(User userInForm);
}
