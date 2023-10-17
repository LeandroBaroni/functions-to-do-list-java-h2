package com.leandrobaroni2103.ToDoList.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.leandrobaroni2103.ToDoList.user.IUserRepository;
import com.leandrobaroni2103.ToDoList.user.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  private IUserRepository _user;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String servletPath = request.getServletPath();
    if (servletPath.startsWith("/tasks/")) {
      String authorization = request.getHeader("Authorization");

      String authEnconded = authorization.substring("Basic".length()).trim();
      byte[] authDecoded = Base64.getDecoder().decode(authEnconded);
      String authStringed = new String(authDecoded);
      String[] credentials = authStringed.split(":");

      if (credentials.length > 0) {
        String username = credentials[0];
        String password = credentials[1];

        User user = this._user.findByUsername(username);
        if (user == null) {
          response.sendError(401);
        } else {
          Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
          if (!result.verified) {
            response.sendError(401);
            return;
          }
          request.setAttribute("userId", user.getId());
          chain.doFilter(request, response);
        }
      }
    } else {
      chain.doFilter(request, response);
    }
  }
}
