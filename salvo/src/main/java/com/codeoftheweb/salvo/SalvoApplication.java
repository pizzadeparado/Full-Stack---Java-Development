package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
																		GameRepository gameRepository,
																		GamePlayerRepository gamePlayerRepository,
																		ScoreRepository scoreRepository) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));

			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4));

			Game game1 = new Game (LocalDateTime.now());
			Game game2 = new Game (LocalDateTime.now().plusHours(1));
			Game game3 = new Game (LocalDateTime.now().plusHours(2));
			Game game4 = new Game (LocalDateTime.now().plusHours(3));
			Game game5 = new Game (LocalDateTime.now().plusHours(4));
			Game game6 = new Game (LocalDateTime.now().plusHours(5));

			gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6));

			GamePlayer gamePlayer1 = new GamePlayer (game1, player1, LocalDateTime.now());
			GamePlayer gamePlayer2 = new GamePlayer (game1, player2, LocalDateTime.now());
			GamePlayer gamePlayer3 = new GamePlayer (game2, player1, LocalDateTime.now());
			GamePlayer gamePlayer4 = new GamePlayer (game2, player3, LocalDateTime.now());
			GamePlayer gamePlayer5 = new GamePlayer (game3, player1, LocalDateTime.now());
			GamePlayer gamePlayer6 = new GamePlayer (game3, player4, LocalDateTime.now());
			GamePlayer gamePlayer7 = new GamePlayer (game4, player2, LocalDateTime.now());
			GamePlayer gamePlayer8 = new GamePlayer (game4, player3, LocalDateTime.now());
			GamePlayer gamePlayer9 = new GamePlayer (game5, player2, LocalDateTime.now());
			GamePlayer gamePlayer10 = new GamePlayer (game5, player4, LocalDateTime.now());
			GamePlayer gamePlayer11 = new GamePlayer (game6, player3, LocalDateTime.now());
			GamePlayer gamePlayer12 = new GamePlayer (game6, player4, LocalDateTime.now());


			gamePlayer1.addShip(new Ship("destroyer", Arrays.asList("H2", "H3", "H4")));
			gamePlayer1.addShip(new Ship("submarine", Arrays.asList("E1", "F1", "G1", "H1")));
			gamePlayer1.addSalvo(new Salvo(1, Arrays.asList("B5", "C5")));
			gamePlayer1.addSalvo(new Salvo(2, Arrays.asList("F2", "D5")));

			gamePlayer2.addShip(new Ship("patrol boat", Arrays.asList("B4", "B5")));
			gamePlayer2.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer2.addSalvo(new Salvo(1, Arrays.asList("A2", "A4")));
			gamePlayer2.addSalvo(new Salvo(2, Arrays.asList("A3", "H6")));

			gamePlayer3.addShip(new Ship("patrol boat", Arrays.asList("F1", "F2")));
			gamePlayer3.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer3.addSalvo(new Salvo(1, Arrays.asList("G6", "H6")));
			gamePlayer3.addSalvo(new Salvo(2, Arrays.asList("B5", "C5")));

			gamePlayer4.addShip(new Ship("patrol boat", Arrays.asList("C6", "C7")));
			gamePlayer4.addShip(new Ship("submarine", Arrays.asList("A2", "A3", "A4", "A5")));
			gamePlayer4.addSalvo(new Salvo(1, Arrays.asList("C6", "C7")));
			gamePlayer4.addSalvo(new Salvo(2, Arrays.asList("A2", "A3")));

			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1, gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5, gamePlayer6, gamePlayer7, gamePlayer8, gamePlayer9, gamePlayer10, gamePlayer11, gamePlayer12));

			Score score1 = new Score(3, game1, player1, LocalDateTime.now());
			Score score2 = new Score(1, game2, player1, LocalDateTime.now());
			Score score3 = new Score(1, game3, player2, LocalDateTime.now());
			Score score4 = new Score(1, game4, player2, LocalDateTime.now());
			Score score5 = new Score(0, game5, player4, LocalDateTime.now());
			Score score6 = new Score(0, game6, player3, LocalDateTime.now());

			scoreRepository.saveAll(Arrays.asList(score1,score2,score3,score4,score5,score6));
		};
	}
}