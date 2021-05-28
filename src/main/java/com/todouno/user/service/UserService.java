package com.todouno.user.service;

import com.todouno.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UserService.
 */
public interface UserService {

  public Mono<User> findById(String id);

  public Flux<User> findAll();

  public Mono<User> save(User user);

}