package com.todouno.user.dao;


import com.todouno.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * UserRepository.
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
