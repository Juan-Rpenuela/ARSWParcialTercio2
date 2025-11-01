package edu.eci.arsw.parcial2.tic_tac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import edu.eci.arsw.parcial2.tic_tac.model.GameState;
import edu.eci.arsw.parcial2.tic_tac.model.Move;
import edu.eci.arsw.parcial2.tic_tac.model.PlayerJoin;
import edu.eci.arsw.parcial2.tic_tac.service.GameService;

@Controller
public class GameController {
    @Autowired
    public GameService gameService;

    @MessageMapping("/join")
    @SendTo("/topic/game")
    public GameState joinGame(PlayerJoin playerJoin){
        System.out.println("Jugador conectado: " + playerJoin.getPlayerId());
        return gameService.addPlayer(playerJoin.getPlayerId(), playerJoin.getPlayerName());
    }

    @MessageMapping("/move")
    @SendTo("/topic/game")
    public GameState makeMove(Move move) {
        System.out.println("Movimiento recibido: " + move);
        return gameService.proccessMove(move);
    }

    @MessageMapping("/reset")
    @SendTo("topic/game")
    public GameState resetGame(){
        return gameService.resetGame();
    }

    @MessageMapping("/state")
    @SendTo("/topic/game")
    public GameState getState() {
        return gameService.getGame();
    }
}

