package com.todouno.user.service;

import static org.mockito.Mockito.when;

import com.todouno.user.dao.UserRepository;
import com.todouno.user.model.User;
import com.todouno.user.service.impl.UserServiceImpl;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

  @TestConfiguration
  static class ProductServiceTestContextConfiguration {

    @Bean
    public UserService userService() {
      return new UserServiceImpl();
    }
  }

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  @Qualifier("jasyptStringEncryptor")
  private StringEncryptor stringEncryptor;

  @Mock
  private User user1;

  @Mock
  private User user2;

  @Mock
  private User user3;


  @BeforeEach
  void setUp() {

    user1 = User.builder().id("1").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
        "123456789").build();

    user2 = User.builder().id("2").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
        "123456789").build();

    user3 = User.builder().id("3").username("test").password("aaa").email("aaa@aa.a").phoneNumber(
        "123456789").build();

  }

  @Test
  public void findAll() {
    when(userRepository.findAll())
        .thenReturn(Flux.just(user1, user2, user3));
    when(stringEncryptor.decrypt(user1.getPassword()))
        .thenReturn("aaa");

    Flux<User> found = userService.findAll();

    assertResults(found, user1, user2, user3);
  }

  @Test
  public void whenValidId_thenProductShouldBeFound() {
    when(userRepository.findById(user1.getId()))
        .thenReturn(Mono.just(user1));

    when(stringEncryptor.decrypt(user1.getPassword()))
        .thenReturn("aaa");

    Mono<User> found = userService.findById(user1.getId());

    assertResults(found, user1);
  }

  @Test
  public void save() {
    when(userRepository.save(user1)).thenReturn(Mono.just(user1));

    when(stringEncryptor.encrypt(user1.getPassword()))
        .thenReturn("aaa");

    Mono<User> saved = userService.save(user1);

    assertResults(saved, user1);
  }



  private void assertResults(Publisher<User> publisher, User... expectedClientTypes) {
    StepVerifier
        .create(publisher)
        .expectNext(expectedClientTypes)
        .verifyComplete();
  }

}