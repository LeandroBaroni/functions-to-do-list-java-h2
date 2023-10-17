package com.leandrobaroni2103.ToDoList.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, UUID> {
  User findByName(String name);
  User findByUsername(String username);
}
