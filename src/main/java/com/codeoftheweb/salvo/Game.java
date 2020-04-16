package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    private Integer currentTurn;

    private Boolean gameOver = false;

    public Game(){}

    public Game(Date date) {this.creationDate = date;}

    public Game (Date date, Integer currentTurn) {
        this.creationDate = date;
        this.currentTurn = currentTurn;
    }

    public Integer getCurrentTurn() {return currentTurn;}

    public Boolean isGameOver() {return gameOver;}

    public Date getCreationDate() {return creationDate;}

    public void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public void setCurrentTurn(Integer turn) {this.currentTurn = turn;}

    public void setGameOver(Boolean gameOver) {this.gameOver = gameOver;}

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", gamePlayers=" + gamePlayers +
                ", scores=" + scores +
                '}';
    }
}