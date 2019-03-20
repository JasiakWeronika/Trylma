package main;

public class Match {

    private GameState state;
    private int hmb, hmp, turn, turnCounter;
    private Board board;
    private static Player[] players;
    private static Bot[] bots;
    private static int [ ] turns = new int[6];

    public Match(int boardBase) {
        this.state = GameState.NOTEXIST;
        board = new Board(boardBase);
        setTurns();
    }

    public GameState getGameState() {
        return state;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public void setHmb(int hmb) {
        this.hmb = hmb;
        bots = new Bot[hmp];
    }

    public int getHmb() {
        return hmb;
    }

    public void setHmp(int hmp) {
        this.hmp = hmp;
        players = new Player[hmp];
    }

    public void setTurns(){
        turns[0]=1;
        turns[1]=3;
        turns[2]=5;
        turns[3]=6;
        turns[4]=4;
        turns[5]=2;
    }

    public int getHmp() {
        return hmp;
    }

    public void setTurn(int turn) {
        this.turnCounter = turn;
        this.turn = turns[turnCounter];
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turnCounter++;
        if(turnCounter>=6) turnCounter = 0;
        turn = turns[turnCounter];
    }

    public Board getBoard(){
        return board;
    }

    public void setNewPlayer(int playerIndex, int threadIndex){
        players[playerIndex] = new Player(playerIndex + 2, board.getCorner(hmp, playerIndex + 2), threadIndex);
    }

    public void createBot() {
        for (int i = 1; i <= hmb; i++) {
            for (int p = 1; p < hmp; p++) {
                if (players[p] == null && bots[p] == null) {
                    bots[p] = new Bot(board.getCorner(hmp, p + 2), p + 2, board);
                    break;
                }
            }
        }
    }

    public Player getPlayer(int i){
        return players[i];
    }

    public Bot getBot(int i){
        return bots[i];
    }

    public void setPlayer(int i, Player player){
        players[i] = player;
    }

    public void setBot(int i, Bot bot){
        bots[i] = bot;
    }
}
