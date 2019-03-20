package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchTest {

    Match match;
    Board b;

    @Before
    public void setUp() {
        match = new Match(4);
        b = new Board(4);
        b.setCorner(1,5);
    }

    @Test
    public void setMatch() throws Exception {
        assertEquals(match.getGameState(),GameState.NOTEXIST);
        assertNotNull(match.getBoard());
        assertEquals(match.getBoard().getBoardField(1,1),b.getBoardField(1,1));
        assertEquals(b.getBoardField(4,7),5);
        assertEquals(match.getBoard().getBoardField(4,7),b.getBoardField(4,7));
    }

    @Test
    public void setGameState() throws Exception {
        assertNotEquals(match.getGameState(),GameState.CREATING);
        match.setGameState(GameState.CREATING);
        assertEquals(match.getGameState(),GameState.CREATING);
        assertNotEquals(match.getGameState(),GameState.WAITING);
        match.setGameState(GameState.WAITING);
        assertEquals(match.getGameState(),GameState.WAITING);
    }

    @Test
    public void setHmb() throws Exception {
        match.setHmb(4);
        assertEquals(match.getHmb(),4);
        match.setHmb(5);
        assertEquals(match.getHmb(),5);
    }

    @Test
    public void setHmp() throws Exception {
        match.setHmp(4);
        assertEquals(match.getHmp(),4);
        match.setHmp(5);
        assertEquals(match.getHmp(),5);
    }

    @Test
    public void setTurn() throws Exception {
        match.setTurn(2);
        assertEquals(match.getTurn(),5);
        match.nextTurn();
        assertEquals(match.getTurn(),6);
        match.setTurn(5);
        assertEquals(match.getTurn(),2);
        match.nextTurn();
        assertEquals(match.getTurn(),1);
    }

    @Test
    public void playerTest() throws Exception{
        match.setHmp(5);
        match.setNewPlayer(3,3);
        assertNotNull(match.getPlayer(3));
        match.setPlayer(3,null);
        assertNull(match.getPlayer(3));
    }

    @Test
    public void botTest() throws Exception{
        match.setHmp(5);
        match.setHmb(1);
        assertNull(match.getPlayer(1));
        match.setNewPlayer(0,1);
        match.createBot();
        assertNotNull(match.getBot(1));
        match.setBot(1,null);
        assertNull(match.getBot(1));
    }


}