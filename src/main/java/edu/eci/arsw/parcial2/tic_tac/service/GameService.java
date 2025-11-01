package edu.eci.arsw.parcial2.tic_tac.service;

import org.springframework.stereotype.Service;

import edu.eci.arsw.parcial2.tic_tac.model.GameState;
import edu.eci.arsw.parcial2.tic_tac.model.GameStatus;
import edu.eci.arsw.parcial2.tic_tac.model.Move;

@Service
public class GameService {
    private GameState game;

    public GameService() {
        this.game = new GameState();
    }

    // getters
    public GameState getGame() {
        return this.game;
    }

    public synchronized GameState addPlayer(String playerId) {
        if (this.game.getPlayer1() == null) {
            this.game.setPlayer1(playerId);
            this.game.setCurrentPlayer(playerId);
        } else if (this.game.getPlayer2() == null) {
            this.game.setPlayer2(playerId);
            this.game.setStatus(GameStatus.PLAYING);
        }
        return game;
    }

    public synchronized GameState proccessMove(Move move) {
        if (!move.getPlayerId().equals(game.getCurrentPlayer())) {
            System.out.println("No es tu turno");
        }

        if (game.getSquares()[move.getPosition()] != null) {
            System.out.println("Casilla Ocupada");
        }

        if (game.getStatus() != GameStatus.PLAYING) {
            System.out.println("No hay juego en curso");
        }

        String[] squares = game.getSquares();
        game.setSquares(squares);
        game.setStepNumber(game.getStepNumber() + 1);

        String winner = calculateWinner(squares);
        if (game.getWinner() == null) {
            game.setWinner(winner);
            game.setStatus(GameStatus.FINISHED);
        } else if (game.getStepNumber() == 9) {
            game.setWinner("Draw");
            game.setStatus(GameStatus.FINISHED);
        } else {
            game.setxIsNext(!game.isxIsNext());
            game.setCurrentPlayer(
                    move.getPlayerId().equals(game.getPlayer1())
                            ? game.getPlayer2()
                            : game.getPlayer1());
        }
        return game;
    }

}
