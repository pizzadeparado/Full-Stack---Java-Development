package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
																		GameRepository gameRepository,
																		GamePlayerRepository gamePlayerRepository,
																		ShipRepository shipRepository,
																		SalvoRepository salvoRepository,
																		ScoreRepository scoreRepository) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			Player player5 = new Player("laura.palmer@twinpeaks.com", passwordEncoder().encode("redroom"));

			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4,player5));

			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date2.toInstant().plusSeconds(3600));
			Date date4 = Date.from(date3.toInstant().plusSeconds(3600));
			Date date5 = Date.from(date4.toInstant().plusSeconds(3600));
			Date date6 = Date.from(date5.toInstant().plusSeconds(3600));
			Date date7 = Date.from(date6.toInstant().plusSeconds(3600));
			Date date8 = Date.from(date7.toInstant().plusSeconds(3600));
			Date date9 = Date.from(date8.toInstant().plusSeconds(3600));
			Date date10 = Date.from(date9.toInstant().plusSeconds(3600));

			Game game1 = new Game (date1);
			Game game2 = new Game (date2);
			Game game3 = new Game (date3);
			Game game4 = new Game (date4);
			Game game5 = new Game (date5);
			Game game6 = new Game (date6);
			Game game7 = new Game (date7);
			Game game8 = new Game (date8);
			Game game9 = new Game (date9);
			Game game10 = new Game (date10);

			gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6,game7,game8,game9,game10));

			GamePlayer gamePlayer1 = new GamePlayer (player1, game1, date1);
			GamePlayer gamePlayer2 = new GamePlayer (player2, game1, date1);
			GamePlayer gamePlayer3 = new GamePlayer (player1, game2, date2);
			GamePlayer gamePlayer4 = new GamePlayer (player3, game2, date2);
			GamePlayer gamePlayer5 = new GamePlayer (player1, game3, date3);
			GamePlayer gamePlayer6 = new GamePlayer (player4, game3, date3);
			GamePlayer gamePlayer7 = new GamePlayer (player1, game4, date4);
			GamePlayer gamePlayer8 = new GamePlayer (player5, game4, date4);
			GamePlayer gamePlayer9 = new GamePlayer (player2, game5, date5);
			GamePlayer gamePlayer10 = new GamePlayer (player3, game5, date5);
			GamePlayer gamePlayer11 = new GamePlayer (player2, game6, date6);
			GamePlayer gamePlayer12 = new GamePlayer (player4, game6, date6);
			GamePlayer gamePlayer13 = new GamePlayer (player2, game7, date7);
			GamePlayer gamePlayer14 = new GamePlayer (player5, game7, date7);
			GamePlayer gamePlayer15 = new GamePlayer (player3, game8, date8);
			GamePlayer gamePlayer16 = new GamePlayer (player4, game8, date8);
			GamePlayer gamePlayer17 = new GamePlayer (player3, game9, date9);
			GamePlayer gamePlayer18 = new GamePlayer (player5, game9, date9);
			GamePlayer gamePlayer19 = new GamePlayer (player4, game10, date10);
			GamePlayer gamePlayer20 = new GamePlayer (player5, game10, date10);

			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1,gamePlayer2,gamePlayer3,gamePlayer4,gamePlayer5,gamePlayer6,gamePlayer7,gamePlayer8,gamePlayer9,gamePlayer10,gamePlayer11,gamePlayer12,gamePlayer13,gamePlayer14,gamePlayer15,gamePlayer16,gamePlayer17,gamePlayer18,gamePlayer19,gamePlayer20));

			List<String> shipL1 = new ArrayList<>(new HashSet<>(Arrays.asList("A1", "A2", "A3", "A4", "A5")));
			List<String> shipL2 = new ArrayList<>(new HashSet<>(Arrays.asList("B1", "B2", "B3", "B4")));
			List<String> shipL3 = new ArrayList<>(new HashSet<>(Arrays.asList("C1", "C2", "C3")));
			List<String> shipL4 = new ArrayList<>(new HashSet<>(Arrays.asList("D1", "D2", "D3")));
			List<String> shipL5 = new ArrayList<>(new HashSet<>(Arrays.asList("E1", "E2")));

			Ship ship1 = new Ship(gamePlayer1,"Carrier",shipL1);
			Ship ship2 = new Ship(gamePlayer2,"Battleship",shipL2);
			Ship ship3 = new Ship(gamePlayer3,"Submarine",shipL3);
			Ship ship4 = new Ship(gamePlayer4,"Destroyer",shipL4);
			Ship ship5 = new Ship(gamePlayer5,"Patrol Boat",shipL5);

			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5));

			Salvo salvo1 = new Salvo(1, Arrays.asList("A1", "A2", "A3"), gamePlayer1);
			Salvo salvo2 = new Salvo(1, Arrays.asList("B5", "B6"), gamePlayer2);
			Salvo salvo3 = new Salvo(1, Arrays.asList("H2", "H3", "H4"), gamePlayer3);
			Salvo salvo4 = new Salvo(1, Arrays.asList("C1", "C2", "C3"), gamePlayer4);
			Salvo salvo5 = new Salvo(1, Arrays.asList("D4", "D5", "D6"), gamePlayer5);

			salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5));
		};
	}
}

//-------------------- web security authentication --------------------//

@Configuration
class WebSecurityAuthentication extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("User" + inputName + "unknown.");
			}
		});
	}
}

//-------------------- web security authorization --------------------//

@EnableWebSecurity
@Configuration
class WebSecurityAuthorization extends WebSecurityConfigurerAdapter {
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/*",
																			"/web/**",
																			"/api/games",
																			"/api/players",
																			"/api/leaderboard",
																			"/api/login",
																			"/api/logout").permitAll()
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.antMatchers("/api/**",
																			"/web/game.html**").hasAuthority("USER")
				.anyRequest().denyAll();

		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

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