package com.todouno.user.controller;


import static org.mockito.Mockito.when;

import com.todouno.user.dao.UserRepository;
import com.todouno.user.model.User;
import com.todouno.user.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UserController.class)
@Import(UserController.class)
public class UserControllerTest {

  @Mock
  private List<User> expectedUserList;

  @Mock
  private User expectedUser;

  @BeforeEach
  void setUp() {

    expectedUser =
        User.builder().id("1").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
            "123456789").build();

    expectedUserList = Arrays.asList(
        User.builder().id("1").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
            "123456789").build(),
        User.builder().id("2").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
            "123456789").build(),
        User.builder().id("3").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
            "123456789").build()
    );
  }

  @MockBean
  protected UserService service;

  @MockBean
  UserRepository repository;

  @Autowired
  private WebTestClient webClient;

  @Test
  void getAll() {
    when(service.findAll()).thenReturn(Flux.fromIterable(expectedUserList));

    webClient.get().uri("/user/findAll")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(User.class)
        .isEqualTo(expectedUserList);
  }

  @Test
  void getById_whenExists_returnCorrect() {
    when(service.findById(expectedUser.getId()))
        .thenReturn(Mono.just(expectedUser));

    webClient.get()
        .uri("/user/find/{numId}", expectedUser.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(User.class)
        .isEqualTo(expectedUser);
  }

  @Test
  void getById_whenNotExists_returnNotFound() {
    String id = "-1";
    when(service.findById(id))
        .thenReturn(Mono.empty());

    webClient.get()
        .uri("/user/find/{numId}", id)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void save() {
    when(service.save(expectedUser)).thenReturn(Mono.just(expectedUser));

    webClient.post()
        .uri("/user/save").body(Mono.just(expectedUser), User.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(User.class)
        .isEqualTo(expectedUser);
  }

  @Test
  void saveFail() {
    when(service.save(expectedUser)).thenReturn(Mono.error(new Exception()));

    webClient.post()
        .uri("/user/save").body(Mono.just(expectedUser), User.class)
        .exchange()
        .expectStatus()
        .isEqualTo(500);
  }

}