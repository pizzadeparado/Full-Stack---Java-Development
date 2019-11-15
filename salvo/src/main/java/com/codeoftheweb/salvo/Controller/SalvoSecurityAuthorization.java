package com.codeoftheweb.salvo.controller;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
class SalvoSecurityAuthorization extends WebSecurityConfigurerAdapter {

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
