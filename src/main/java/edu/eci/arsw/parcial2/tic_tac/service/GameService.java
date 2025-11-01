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

    public synchronized GameState addPlayer(String playerId, String playerName) {
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

    public synchronized GameState resetGame() {
        String player1 = game.getPlayer1();
        String player2 = game.getPlayer2();
        
        game = new GameState();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setCurrentPlayer(player1);
        
        if (player1 != null && player2 != null) {
            game.setStatus(GameStatus.PLAYING);
        }
        
        return game;
    }

    private String calculateWinner(String[] squares) {
        int[][] winnersLines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, 
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, 
            {0, 4, 8}, {2, 4, 6}             
        };

        for (int[] line : winnersLines) {
            String a = squares[line[0]];
            String b = squares[line[1]];
            String c = squares[line[2]];
            
            if (a != null && a.equals(b) && a.equals(c)) {
                return a;
            }
        }
        
        return null;
    }
}


