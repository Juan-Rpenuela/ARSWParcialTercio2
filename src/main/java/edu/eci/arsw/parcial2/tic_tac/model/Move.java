package edu.eci.arsw.parcial2.tic_tac.model;


public class Move {
    public String symbol;
    public String playerId;
    public int position;

    public Move(String symbol, String playerId, int position) {
        this.playerId = playerId;
        this.symbol = symbol;
        this.position = position;

    }

    public Move() {
    }

    // getters

    public int getPosition() {
        return this.position;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getSymbol() {
        return this.symbol;
    }

    // setters

    public void setPosition(int newPos) {
        this.position = newPos;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;

    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Move{" +
                "position=" + position +
                ", playerId='" + playerId + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

}