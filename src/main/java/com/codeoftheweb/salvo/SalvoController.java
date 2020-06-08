package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    public Map<String, Object> authenticatedUser(Authentication authentication)  {
        Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", playerRepository.findByUserName(authentication.getName()).getId());
            dto.put("userName", playerRepository.findByUserName(authentication.getName()).getUserName());
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if(!isGuest(authentication)) {
        dto.put("currentUser", authenticatedUser(authentication));
        } else {
            dto.put("currentUser",null);
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> makeGameDTO(game)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", findGamePlayers(game));
        return dto;
    }

    private List<Object> findGamePlayers(Game game) {
        return game.getGamePlayers().stream().map(gamePlayer -> makeGPDTO(gamePlayer)).collect(Collectors.toList());
    }

    private Map<String, Object> makeGPDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        if (gamePlayer.getScore() == null) {
            dto.put("score", null);
        } else {
            dto.put("score", gamePlayer.getScore().getScore());
        }
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("username", player.getUserName());
        dto.put("email", player.getUserEmail());
        return dto;
    }

    private List<Object> findShips(GamePlayer gamePlayer) {
        return gamePlayer.getShips().stream().map(ship -> makeShipDTO(ship)).collect(Collectors.toList());
    }

    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("shipType", ship.getShipType());
        dto.put("shipLocations", ship.getShipLocations());
        return dto;
    }

    private Map<String, Map<String, List<String>>> makeSalvoesDTO(Set<GamePlayer> gamePlayers) {
        Map<String, Map<String, List<String>>> dto = new LinkedHashMap<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            dto.put(gamePlayer.getId().toString(), makeSalvoLocationsDTO(gamePlayer.getSalvoes()) );
        };
        return dto;
    }

    private Map<String, List<String>> makeSalvoLocationsDTO(Set<Salvo> salvoes) {
        Map<String, List<String>> innerDTO = new LinkedHashMap<>();
        for (Salvo salvo : salvoes) {
            innerDTO.put(salvo.getTurn().toString(), salvo.getSalvoLocation());
        }
        return innerDTO;
    }

    @RequestMapping("/game_view/{gpID}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gpID, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpID);
        Game game = gamePlayer.getGame();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gameplayers", findGamePlayers(game));
        dto.put("ships", findShips(gamePlayer));
        dto.put("salvoes", makeSalvoesDTO(gamePlayer.getGame().getGamePlayers()));
        dto.put("status", getStatus(gamePlayer));
        if (getOpponent(gamePlayer) != null) {
            dto.put("battleStatus", makeGpBattleStatusDTO(gamePlayer, getOpponent(gamePlayer)));
        }

        if (gamePlayer != null && authenticatedUser(authentication).get("id") == gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error", "Don't cheat!!!"), HttpStatus.UNAUTHORIZED);
        }
    }

    private String getStatus (GamePlayer gamePlayer) {
        String status = "";
        Double viewerPoints = 0.0;
        Double opponentPoints = 0.0;
        if (gamePlayer.getGame().getGamePlayers().size() < 2) {
            status = "Waiting for an opponent!";
        } else {
            status = "Place your ships!";
            if (gamePlayer.getShips().size() != getOpponent(gamePlayer).getShips().size()) {
                status = "Waiting for ships to be placed!";
            }
            if ((gamePlayer.getSalvoes().size() == getOpponent(gamePlayer).getSalvoes().size()) && (gamePlayer.getShips().size() != 0 && getOpponent(gamePlayer).getShips().size() != 0)) {
                status = "Fire your salvoes!";
            }
            if (gamePlayer.getSalvoes().size() != getOpponent(gamePlayer).getSalvoes().size()) {
                status = "Waiting for salvoes to be fired!";
            }
            if(successfulHitsCalc(getOpponent(gamePlayer), true).size() == 17 && gamePlayer.getSalvoes().size() == getOpponent(gamePlayer).getSalvoes().size()) {
                status = "Victory!";
                viewerPoints = 1.0;
                scoreRepository.save(new Score(gamePlayer.getGame(), gamePlayer.getPlayer(), viewerPoints));
            }
            if (successfulHitsCalc(gamePlayer, true).size() == 17 && gamePlayer.getSalvoes().size() == getOpponent(gamePlayer).getSalvoes().size()) {
                status = "Defeat!";
                opponentPoints = 1.0;
                scoreRepository.save(new Score(gamePlayer.getGame(), getOpponent(gamePlayer).getPlayer(), opponentPoints));
            }
            if (successfulHitsCalc(getOpponent(gamePlayer), true).size() == 17 && successfulHitsCalc(gamePlayer, true). size() == 17 && gamePlayer.getSalvoes().size() == getOpponent(gamePlayer).getSalvoes().size()) {
                status = "Tie!";
                viewerPoints = 0.5;
                opponentPoints = 0.5;
                scoreRepository.save(new Score(gamePlayer.getGame(), gamePlayer.getPlayer(), viewerPoints));
                scoreRepository.save(new Score(gamePlayer.getGame(), getOpponent(gamePlayer).getPlayer(), opponentPoints));
            }
        }
        return status;
    }

   private GamePlayer getOpponent (GamePlayer oneGP) {
        return oneGP.getGame().getGamePlayers().stream().filter(gamePlayer -> !gamePlayer.getId().equals(oneGP.getId())).findFirst().orElse(null);
    }

    private Set<String> getShots (GamePlayer oneGP) {
        if (oneGP != null) {
            return oneGP.getSalvoes().stream().map(oneSalvo -> oneSalvo.getSalvoLocation()).flatMap(Collection::stream).collect(Collectors.toSet());
        } else return null;
    }

    private Set<String> getShipLocations (GamePlayer oneGP) {
        return oneGP.getShips().stream().map(oneShip -> oneShip.getShipLocations()).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Map<String, Object> makeGpBattleStatusDTO(GamePlayer viewer, GamePlayer oneGP) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayer", oneGP.getId());
        dto.put("hitsReceived", successfulHitsCalc(oneGP, true));
        dto.put("missReceived", successfulHitsCalc(oneGP, false));
        dto.put("fleetStatus", fleetStatus(viewer, oneGP));
        return dto;
    }

    private Set<String> successfulHitsCalc (GamePlayer oneGP, Boolean successfulHitsGet) {
        Set<String> ownShipLocations = getShipLocations(oneGP);
        Set<String> opponentShots = getShots(getOpponent(oneGP));
        Set<String> dto = new LinkedHashSet<>();
        if (opponentShots != null) {

            if(successfulHitsGet) {
                dto = opponentShots.stream().filter(ownShipLocations::contains).collect(Collectors.toSet());
            } else {
                dto = opponentShots.stream().filter(oneShot -> !ownShipLocations.contains(oneShot)).collect(Collectors.toSet());
            }
            return dto;
        }

        return dto;
    }

    private Set<Map<String, Object>> fleetStatus(GamePlayer viewer, GamePlayer oneGP) {
        Set<Map<String, Object>> dto = oneGP.getShips().stream().sorted(Comparator.comparing(oneShip -> oneShip.getShipLocations().size())).map(oneShip -> makeShipStatusDTO(viewer, oneShip)).collect(Collectors.toSet());
        return dto;
    }

    private Map<String, Object> makeShipStatusDTO (GamePlayer viewer, Ship oneShip) {
        Map dto = new LinkedHashMap();
        dto.put("shipType", oneShip.getShipType());
        dto.put("isSunk", shipSunk(oneShip));
        dto.put("shipHP", oneShip.getShipLocations().size());
        return dto;
    }

    private Boolean shipSunk (Ship oneShip) {
        GamePlayer shipOwner = oneShip.getGamePlayer();
        Set<String> opponentShots = getShots(getOpponent(shipOwner));
        if (opponentShots != null) {
            return oneShip.getShipLocations().stream().allMatch(opponentShots::contains);
        } else return false;
    }

    @RequestMapping("/leader_board")
    public Map<String, Object> getLB() {
        List<GamePlayer>gamePlayers = gamePlayerRepository.findAll();
        Map<String, Object>leaderBoard = new LinkedHashMap<>();
        for (GamePlayer gp : gamePlayers) {
            Set<Score>scoreList = gp.getPlayer().getScores();
            if (!leaderBoard.containsKey(gp.getPlayer().getUserName())) {
                Map<String, Object>scoreInfo = new LinkedHashMap<>();
                scoreInfo.put("wins", scoreList.stream().filter(score -> score.getScore() == 1.0).count());
                scoreInfo.put("losses", scoreList.stream().filter(score -> score.getScore() == 0.0).count());
                scoreInfo.put("ties", scoreList.stream().filter(score -> score.getScore() == 0.5).count());
                scoreInfo.put("totalScore", scoreList.stream().mapToDouble(score -> score.getScore()).sum());
                leaderBoard.put(gp.getPlayer().getUserName(), scoreInfo);
            }
        }
        return leaderBoard;
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> newUser(@RequestBody Player player) {
        String userName = player.getUserName();
        String userEmail = player.getUserEmail();
        String password = player.getPassword();
        Player playerExists = playerRepository.findByUserName(userName);
        if (playerExists != null) {
            return new ResponseEntity<>(makeMap("error", "This user already exists!"), HttpStatus.FORBIDDEN);
        }
        if (userName.isEmpty() || userEmail.isEmpty() || password.isEmpty() || !userEmail.contains("@") || userEmail.contains(" ")) {
            return new ResponseEntity<>(makeMap("error", "Woops! You're missing something in the sign up form!"), HttpStatus.UNAUTHORIZED);
        }
        Player newUser = new Player(userName, userEmail, passwordEncoder.encode(password));
        playerRepository.save(newUser);
        return new ResponseEntity<>(makeMap("userName", newUser.getUserName()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (!isGuest(authentication)) {
            Date creationDate = new Date();
            Game newGame = new Game(creationDate);
            Player creationPlayer = playerRepository.findByUserName(authentication.getName());
            GamePlayer newGamePlayer = new GamePlayer(newGame, creationPlayer);
            gameRepository.save(newGame);
            gamePlayerRepository.save(newGamePlayer);
            return new ResponseEntity<>(makeMap("gpID", newGamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "Log in needed to create a game"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/game/{gameID}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameID, Authentication authentication) {
        Game requestedGame = gameRepository.findById(gameID).orElse(null);
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "User is not logged in"), HttpStatus.UNAUTHORIZED);
        } if (requestedGame == null) {
            return new ResponseEntity<>(makeMap("error", "This game does not exist"), HttpStatus.FORBIDDEN);
        } if (requestedGame.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(makeMap("error", "This game is full"), HttpStatus.FORBIDDEN);
        } if (requestedGame.getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer().equals(playerRepository.findByUserName(authentication.getName()))).findFirst().orElse(null)!=null) {
            return  new ResponseEntity<>(makeMap("error", "You have already joined this game"), HttpStatus.CONFLICT);
        }
            Player creationPlayer = playerRepository.findByUserName(authentication.getName());
            GamePlayer newGamePlayer = new GamePlayer(requestedGame, creationPlayer);
            gamePlayerRepository.save(newGamePlayer);
            return new ResponseEntity<>(makeMap("gpID", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gpID}/ships", method = RequestMethod.POST)
    public ResponseEntity<String> placeShips(@PathVariable Long gpID, @RequestBody Set<Ship> ships, Authentication authentication) {
        GamePlayer currentGamePlayer = gamePlayerRepository.findById(gpID).orElse(null);
        if((authenticatedUser(authentication).get("id") == null) || (currentGamePlayer == null || authenticatedUser(authentication).get("id") != currentGamePlayer.getPlayer().getId())) {
            return new ResponseEntity<>("No valid user logged in", HttpStatus.UNAUTHORIZED);
        } else {
            Set<Ship> shipFleet = currentGamePlayer.getShips();
            if (shipFleet.size() != 0) {
                return new ResponseEntity<>("Ships have been already placed!", HttpStatus.FORBIDDEN);
            } else {
                ships.stream().forEach(ship -> {
                    ship.setGamePlayer(currentGamePlayer);
                    shipRepository.save(ship);
                });
                return new ResponseEntity<>("Ships successfully placed!", HttpStatus.CREATED);
            }
        }
    }

    @RequestMapping(value = "/games/players/{gpID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<String> placeSalvoes(@PathVariable Long gpID, @RequestBody Salvo newSalvo, Authentication authentication) throws Exception {
        GamePlayer currentGamePlayer = gamePlayerRepository.findById(gpID).orElse(null);
        if ((authenticatedUser(authentication).get("id") == null) ||
                (currentGamePlayer == null) || (authenticatedUser(authentication).get("id") != currentGamePlayer.getPlayer().getId())) {
            return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
        } else {
            if (currentGamePlayer.getGame().getGamePlayers().size() < 2) {
                return new ResponseEntity<>("Waiting for an opponent!", HttpStatus.FORBIDDEN);
            }
            if (newSalvo.getSalvoLocation().size() != 5) {
                return new ResponseEntity<>("Salvoes must be fired in sets of 5!", HttpStatus.FORBIDDEN);
            } else if(currentGamePlayer.getSalvoes().size() > getOpponent(currentGamePlayer).getSalvoes().size()) {
                return new ResponseEntity<>("Waiting for opponent's salvoes!", HttpStatus.FORBIDDEN);
            }
            else {
                newSalvo.setTurn(currentGamePlayer.getSalvoes().size() + 1);
                newSalvo.setGamePlayer(currentGamePlayer);
                salvoRepository.save(newSalvo);
                return new ResponseEntity<>("Salvoes successfully fired!", HttpStatus.CREATED);
            }
        }
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }
}