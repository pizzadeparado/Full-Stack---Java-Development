package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private String userName;
  private String password;
  private boolean admin;

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayers;

  @OneToMany(mappedBy="player", fetch = FetchType.EAGER)
  public Set<Score> scores = new HashSet();

  public Player () {

  }

  public Player(String userName, String password) {
    this.userName = userName;
    this.password = password;
    this.admin = false;
  }

  public Player(String userName, String password, boolean isAdmin) {
    this.userName = userName;
    this.password = password;
    this.admin = isAdmin;
  }

  public long getPlayerID() {
    return this.ID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String toString() {
    return userName;
  }

  public boolean isAdmin() {
    return this.admin;
  }

  public void setAdmin(boolean isAdmin) {
    this.admin = isAdmin;
  }

  public Set<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  public float scoreWon() {
    return scores.stream().filter(score -> score.getScore() == 1).count();
  }

  public float scoreLost() {
    return scores.stream().filter(score -> score.getScore() == 0).count();
  }

  public float scoreTied() {
    return scores.stream().filter(score -> score.getScore() == 0.5).count();
  }

  public float scoreTotal() {
    return 1f * scoreWon() + 0.5f * scoreLost() + 0 * scoreTied();
  }

  @JsonIgnore
  public List<Game> getGame(){
    return gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toList());
  }

  public Map<String, Object> createPlayerDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("ID", this.getPlayerID());
    dto.put("user", this.getUserName());
    return dto;
  }

  public Map<String, Object> createScoreDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("playerID", this.getPlayerID());
    dto.put("user", this.getUserName());
    dto.put("won", this.scoreWon());
    dto.put("tied", this.scoreTied());
    dto.put("lost", this.scoreLost());
    dto.put("total", this.scoreTotal());
    return dto;
  }
}