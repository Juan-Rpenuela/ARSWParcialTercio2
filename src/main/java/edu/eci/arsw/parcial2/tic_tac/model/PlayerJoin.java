package edu.eci.arsw.parcial2.tic_tac.model;

public class PlayerJoin {
    public String playerId;
    public String playerName;


    public PlayerJoin(){}

    public PlayerJoin(String playerName, String playerId){
        this.playerId = playerId;
        this.playerName = playerName;
    }

    //getters

    public String getPlayerId (){
        return this.playerId;
    }
    
    public String getPlayerName (){
        return this.playerName;
    }

    //setters 

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
}
