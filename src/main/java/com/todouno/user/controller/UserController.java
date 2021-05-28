package com.todouno.user.controller;


import com.todouno.user.model.User;
import com.todouno.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UserController.
 */
@Api(tags = "User API", value = "CRUD operations for users")
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @ApiOperation(value = "Service used to return all users")
  @GetMapping("/findAll")
  public Flux<User> findAll() {
    return userService.findAll();
  }

  /**
   * FIND A USER.
   */
  @ApiOperation(value = "Service used to find a user by id")
  @GetMapping("/find/{id}")
  public Mono<ResponseEntity<User>> findById(@PathVariable("id") String id) {
    return userService.findById(id).map(c -> ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(c))
        .defaultIfEmpty(ResponseEntity
            .notFound()
            .build()
        );
  }

  /**
   * SAVE A USER.
   */
  @ApiOperation(value = "Service used to create users")
  @PostMapping("/save")
  public Mono<ResponseEntity<User>> create(@Valid @RequestBody User user) {
    return userService.save(user)
        .map(c -> ResponseEntity
            .created(URI.create("/users".concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON)
            .body(c));
  }

}