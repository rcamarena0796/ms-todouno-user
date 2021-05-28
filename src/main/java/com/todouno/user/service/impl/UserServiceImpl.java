package com.todouno.user.service.impl;

import com.todouno.user.dao.UserRepository;
import com.todouno.user.model.User;
import com.todouno.user.service.UserService;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  @Qualifier("jasyptStringEncryptor")
  private StringEncryptor encryptor;

  @Override
  public Flux<User> findAll() {
    return userRepository.findAll()
        .map(user -> {
          user.setPassword(encryptor.decrypt(user.getPassword()));
          return user;
        });
  }

  @Override
  public Mono<User> findById(String id) {
    return userRepository.findById(id).map(user -> {
      user.setPassword(encryptor.decrypt(user.getPassword()));
      return user;
    }).switchIfEmpty(Mono.empty());
  }

  @Override
  public Mono<User> save(User user) {
    try {
      user.setPassword(encryptor.encrypt(user.getPassword()));
      return userRepository.save(user);

    } catch (Exception e) {
      return Mono.error(e);
    }
  }

}
