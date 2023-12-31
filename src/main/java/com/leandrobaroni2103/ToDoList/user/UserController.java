package com.leandrobaroni2103.ToDoList.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody User user) {

    User existUser = this.userRepository.findByName(user.getName());
    if (existUser != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
    }

    var cryptedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
    user.setPassword(cryptedPassword);

    User userCreated = this.userRepository.save(user);

    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
