package main;

public class Player {

    private int playerIndex, cornerIndex, threadIndex;

    public Player(int playerIndex, int cornerIndex, int threadIndex){
        this.playerIndex=playerIndex;
        this.cornerIndex=cornerIndex;
        this.threadIndex=threadIndex;
    }

    public int getPlayerIndex(){
        return playerIndex;
    }

    public int getCornerIndex(){
        return cornerIndex;
    }

    public int getThreadIndex(){
        return threadIndex;
    }
}
