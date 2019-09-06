package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
																		GameRepository gameRepository,
																		GamePlayerRepository gamePlayerRepository,
																		ShipRepository shipRepository) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov");
			Player player2 = new Player("c.obrian@ctu.gov");
			Player player3 = new Player("t.almeida@ctu.gov");
			Player player4 = new Player("d.palmer@whitehouse.gov");
			Player player5 = new Player("laura.palmer@twinpeaks.com");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

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

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);
			gameRepository.save(game9);
			gameRepository.save(game10);

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

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer13);
			gamePlayerRepository.save(gamePlayer14);
			gamePlayerRepository.save(gamePlayer15);
			gamePlayerRepository.save(gamePlayer16);
			gamePlayerRepository.save(gamePlayer17);
			gamePlayerRepository.save(gamePlayer18);
			gamePlayerRepository.save(gamePlayer19);
			gamePlayerRepository.save(gamePlayer20);

			Set<String> shipL1 = new HashSet<>(Arrays.asList("H2","H3","H4"));
			Set<String> shipL2 = new HashSet<>(Arrays.asList("E1","F1","G1"));
			Set<String> shipL3 = new HashSet<>(Arrays.asList("B4","B5"));
			Set<String> shipL4 = new HashSet<>(Arrays.asList("B5","C5","D5"));

			Ship ship1 = new Ship(gamePlayer1,"Destroyer",shipL1);
			Ship ship2 = new Ship(gamePlayer2, "Cruiser",shipL2);
			Ship ship3 = new Ship(gamePlayer3,"Submarine",shipL3);
			Ship ship4 = new Ship(gamePlayer2,"Battleship",shipL4);

			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4));

		};
	}
}