package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
class SalvoSecurityAuthentication extends GlobalAuthenticationConfigurerAdapter {

  @Autowired
  PlayerRepository playerRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(inputName-> {
      Player player = playerRepository.findByUserName(inputName);
      if (player != null) {
        if (player.isAdmin()) {
          return new User(player.getUserName(), player.getPassword(),
              AuthorityUtils.createAuthorityList("Admin"));
        } else {
          return new User(player.getUserName(), player.getPassword(),
              AuthorityUtils.createAuthorityList("User"));
        }
      } else {
        throw new UsernameNotFoundException("User" + inputName + "unknown.");
      }
    }).passwordEncoder(passwordEncoder());
  }
}