package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Configuration
class WebSecurityAuthentication extends GlobalAuthenticationConfigurerAdapter {

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


@EnableWebSecurity
@Configuration
class WebSecurityAuthorization extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/web/*",
            "/web/**",
            "/api/login",
            "/api/logout",
            "/api/games",
            "/api/games/games/*",
            "/api/players",
            "/api/leaderboard").permitAll()
        .antMatchers("/api/**",
            "/web/game.html**").hasAuthority("USER")
        .antMatchers("/rest/**").hasAuthority("ADMIN")
        .anyRequest().denyAll();

    http.formLogin()
        .usernameParameter("username")
        .passwordParameter("password")
        .loginPage("/api/login");

    //Logs out, invalidates HTTP session and deletes cookies.
    http.logout()
        .logoutUrl("/api/logout")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");

    http.csrf().disable();

    http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

    http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
  }
}
