package com.todouno.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "USER")
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class User {

  @Id
  private String id;

  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "password is required")
  private String password;

  @Email(message = "email should be valid")
  private String email;

  @NotBlank(message = "phoneNumber is required")
  @Pattern(regexp="(^$|[0-9]{9})",message = "phoneNumber must be valid")
  private String phoneNumber;

}
