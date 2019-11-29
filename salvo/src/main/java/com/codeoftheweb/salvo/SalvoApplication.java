package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
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
																		ShipRepository shipRepository,
																		SalvoRepository salvoRepository,
																		ScoreRepository scoreRepository) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));

			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4));

			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date2.toInstant().plusSeconds(3600));
			Date date4 = Date.from(date3.toInstant().plusSeconds(3600));
			Date date5 = Date.from(date4.toInstant().plusSeconds(3600));
			Date date6 = Date.from(date5.toInstant().plusSeconds(3600));

			Game game1 = new Game (date1);
			Game game2 = new Game (date2);
			Game game3 = new Game (date3);
			Game game4 = new Game (date4);
			Game game5 = new Game (date5);
			Game game6 = new Game (date6);

			gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6));

			GamePlayer gamePlayer1 = new GamePlayer (player1, game1, date1);
			GamePlayer gamePlayer2 = new GamePlayer (player2, game1, date1);
			GamePlayer gamePlayer3 = new GamePlayer (player1, game2, date2);
			GamePlayer gamePlayer4 = new GamePlayer (player3, game2, date2);
			GamePlayer gamePlayer5 = new GamePlayer (player1, game3, date3);
			GamePlayer gamePlayer6 = new GamePlayer (player4, game3, date3);
			GamePlayer gamePlayer7 = new GamePlayer (player2, game4, date4);
			GamePlayer gamePlayer8 = new GamePlayer (player3, game4, date4);
			GamePlayer gamePlayer9 = new GamePlayer (player2, game5, date5);
			GamePlayer gamePlayer10 = new GamePlayer (player4, game5, date5);
			GamePlayer gamePlayer11 = new GamePlayer (player3, game6, date6);
			GamePlayer gamePlayer12 = new GamePlayer (player4, game6, date6);

			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1,gamePlayer2,gamePlayer3,gamePlayer4,gamePlayer5,gamePlayer6,gamePlayer7,gamePlayer8,gamePlayer9,gamePlayer10,gamePlayer11,gamePlayer12));

			Set<String> shipLocation1 = new HashSet<>(Arrays.asList("H2", "H3", "H4"));
			Set<String> shipLocation2 = new HashSet<>(Arrays.asList("E1", "F1", "G1"));
			Set<String> shipLocation3 = new HashSet<>(Arrays.asList("B4", "B5"));
			Set<String> shipLocation4 = new HashSet<>(Arrays.asList("B5", "C5", "D5"));
			Set<String> shipLocation5 = new HashSet<>(Arrays.asList("F1", "F2"));
			Set<String> shipLocation6 = new HashSet<>(Arrays.asList("B5", "C5", "D5"));
			Set<String> shipLocation7 = new HashSet<>(Arrays.asList("C6", "C7"));
			Set<String> shipLocation8 = new HashSet<>(Arrays.asList("A2", "A3", "A4"));
			Set<String> shipLocation9 = new HashSet<>(Arrays.asList("G6", "H6"));

			Ship ship1 = new Ship(gamePlayer1,"Destroyer",shipLocation1);
			Ship ship2 = new Ship(gamePlayer2,"Submarine",shipLocation2);
			Ship ship3 = new Ship(gamePlayer3,"Patrol Boat",shipLocation3);
			Ship ship4 = new Ship(gamePlayer4,"Destroyer",shipLocation4);
			Ship ship5 = new Ship(gamePlayer5,"Patrol Boat",shipLocation5);
			Ship ship6 = new Ship(gamePlayer6,"Destroyer",shipLocation6);
			Ship ship7 = new Ship(gamePlayer7,"Patrol Boat",shipLocation7);
			Ship ship8 = new Ship(gamePlayer8,"Submarine",shipLocation8);
			Ship ship9 = new Ship(gamePlayer9,"Patrol Boat",shipLocation9);

			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6,ship7,ship8,ship9));

			Set<String> salvoLocation1 = new HashSet<>(Arrays.asList("B5", "C5", "F1"));
			Set<String> salvoLocation2 = new HashSet<>(Arrays.asList("F2", "D5"));
			Set<String> salvoLocation3 = new HashSet<>(Arrays.asList("A2", "A4", "G6"));
			Set<String> salvoLocation4 = new HashSet<>(Arrays.asList("A3", "H6"));
			Set<String> salvoLocation5 = new HashSet<>(Arrays.asList("G6", "H6", "A4"));
			Set<String> salvoLocation6 = new HashSet<>(Arrays.asList("A2", "A3", "D8"));
			Set<String> salvoLocation7 = new HashSet<>(Arrays.asList("A3", "A4", "F7"));
			Set<String> salvoLocation8 = new HashSet<>(Arrays.asList("A2", "G6", "H6"));
			Set<String> salvoLocation9 = new HashSet<>(Arrays.asList("A1", "A2", "A3"));
			Set<String> salvoLocation10 = new HashSet<>(Arrays.asList("G6", "G7", "G8"));

			Salvo salvo1 = new Salvo(gamePlayer1, 1, salvoLocation1);
			Salvo salvo2 = new Salvo(gamePlayer1, 2, salvoLocation2);
			Salvo salvo3 = new Salvo(gamePlayer2, 1, salvoLocation3);
			Salvo salvo4 = new Salvo(gamePlayer2, 2, salvoLocation4);
			Salvo salvo5 = new Salvo(gamePlayer3, 1, salvoLocation5);
			Salvo salvo6 = new Salvo(gamePlayer3, 2, salvoLocation6);
			Salvo salvo7 = new Salvo(gamePlayer4, 1, salvoLocation7);
			Salvo salvo8 = new Salvo(gamePlayer4, 2, salvoLocation8);
			Salvo salvo9 = new Salvo(gamePlayer5, 1, salvoLocation9);
			Salvo salvo10 = new Salvo(gamePlayer5, 2, salvoLocation10);

			salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5,salvo6,salvo7,salvo8,salvo9,salvo10));

			Score score1 = new Score(game1, player1, 1, date1);
			Score score2 = new Score(game2, player1, 0.5, date2);
			Score score3 = new Score(game3, player2, 1, date3);
			Score score4 = new Score(game4, player2, 0.5, date4);
			Score score5 = new Score(game5, player4, 0, date5);
			Score score6 = new Score(game6, player3, 0, date6);

			scoreRepository.saveAll(Arrays.asList(score1,score2,score3,score4,score5,score6));
		};
	}
}