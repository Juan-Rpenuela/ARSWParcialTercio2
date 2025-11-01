package edu.eci.arsw.parcial2.tic_tac.model;

import java.util.Arrays;

public class GameState {

    private String[] squares;
    private boolean xIsNext;
    private String winner;
    private int stepNumber;
    private String player1;
    private String player2;
    private String currentPlayer;
    private GameStatus status;


    public GameState() {
        this.squares = new String[9];
        Arrays.fill(this.squares, null);
        this.xIsNext = true;
        this.winner = null;
        this.stepNumber = 0;
        this.status = GameStatus.WAITING;
    }

    public String[] getSquares() {
        return squares;
    }

    public void setSquares(String[] squares) {
        this.squares = squares;
    }

    public boolean isxIsNext() {
        return xIsNext;
    }

    public void setxIsNext(boolean xIsNext) {
        this.xIsNext = xIsNext;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GameState{"
                + "squares=" + Arrays.toString(squares)
                + ", xIsNext=" + xIsNext
                + ", winner='" + winner + '\''
                + ", stepNumber=" + stepNumber
                + ", status=" + status
                + '}';
    }
}
