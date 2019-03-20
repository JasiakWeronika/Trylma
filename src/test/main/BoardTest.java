package main;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class BoardTest {

    Board b;

    @Before
    public void setUp(){
        b = new Board(4);
    }

    @Test
    public void setBoard() throws Exception {
        for(int i=0; i<b.getBoardWidth(); i++)
            assertEquals(b.getBoardField(0,i),0);
        assertEquals(b.getBoardField(1,6),0);
        assertEquals(b.getBoardField(1,8),0);
    }

    @Test
    public void setOneWholeLine() throws Exception {
        b.setOneWholeLine(5);
        assertEquals(b.getBoardField(5,0),0);
        assertEquals(b.getBoardField(5,b.getBoardWidth()-1),0);
        for(int i=1; i<b.getBoardWidth()-1; i++)
            assertEquals(b.getBoardField(5,i),1);
    }

    @Test
    public void getBoardField() throws Exception {
        assertNotNull(b.getBoardField(1,2));
    }

    @Test
    public void getBoardHeight() throws Exception {
        Board b4 = new Board(4);
        assertEquals(b4.getBoardHeight(),19);
        Board b5 = new Board(5);
        assertEquals(b5.getBoardHeight(),23);
    }

    @Test
    public void getBoardWidth() throws Exception {
        Board b4 = new Board(4);
        assertEquals(b4.getBoardWidth(),15);
        Board b5 = new Board(5);
        assertEquals(b5.getBoardWidth(),18);
    }

    @Test
    public void setBoardField() throws Exception {
        assertNotEquals(b.getBoardField(8,8),8);
        b.setBoardField(8,8,8);
        assertEquals(b.getBoardField(8,8),8);

        assertNotEquals(b.getBoardField(1,2),3);
        b.setBoardField(1,2,3);
        assertEquals(b.getBoardField(1,2),3);

        assertNotEquals(b.getBoardField(1,1),1);
        b.setBoardField(1,1,2);
        assertEquals(b.getBoardField(1,1),2);
    }

    @Test
    public void getRectangle() throws Exception {
        assertNotNull(b.getRectangle(3,3,30,300));
        assertEquals(b.getRectangle(3,3,30,1000),new Rectangle(350,90,28,28));
        assertEquals(b.getRectangle(2,2,20,1000),new Rectangle(390,40,18,18));
    }

    @Test
    public void getBoard() throws Exception {
        assertNotNull(b.getBoard());
    }

    @Test
    public void getCorner() throws Exception {
        assertEquals(b.getCorner(2,2),1);
        assertEquals(b.getCorner(2,3),6);
        assertNotEquals(b.getCorner(2,2),0);
        assertEquals(b.getCorner(1,2),0);
        assertNotEquals(b.getCorner(3,3),0);
        assertEquals(b.getCorner(3,2),2);
        assertEquals(b.getCorner(3,3),3);
        assertEquals(b.getCorner(3,4),6);
        assertEquals(b.getCorner(4,3),3);
        assertEquals(b.getCorner(4,2),2);
        assertEquals(b.getCorner(6,5),4);
    }


    @Test
    public void setCheckers() throws Exception {
        b.setCheckers(2);
        assertTrue(b.checkCorner(1,2));
        assertTrue(b.checkCorner(6,3));
        b.setCheckers(3);
        assertTrue( b.checkCorner(2,2));
        assertTrue( b.checkCorner(3,3));
        assertTrue(b.checkCorner(6,4));
        b.setCheckers(4);
        assertTrue( b.checkCorner(2,2));
        assertTrue(b.checkCorner(3,3));
        assertTrue(b.checkCorner(4,4));
        assertTrue(b.checkCorner(5,5));
        b.setCheckers(6);
        assertTrue( b.checkCorner(1,2));
        assertTrue( b.checkCorner(2,3));
        assertTrue( b.checkCorner(3,4));
        assertTrue( b.checkCorner(4,5));
        assertTrue( b.checkCorner(5,6));
        assertTrue( b.checkCorner(6,7));
    }

    @Test
    public void setCorner() throws Exception {
        b.setCorner(3,3);
        b.setCorner(5,5);
        assertTrue(b.checkCorner(3,3));
        assertTrue(b.checkCorner(5,5));
    }

    @Test
    public void getBoardBase() throws Exception {
        assertEquals(b.getBoardBase(),4);
    }


    @Test
    public void getCornerWin() throws Exception{
        b.setCheckers(6);
        assertEquals(b.getCornerWinX(1),17);
        assertEquals(b.getCornerWinY(1),7);
        assertEquals(b.getCornerWinX(2),13);
        assertEquals(b.getCornerWinY(2),13);
        assertEquals(b.getCornerWinX(3),13);
        assertEquals(b.getCornerWinY(3),1);
        assertEquals(b.getCornerWinX(4),5);
        assertEquals(b.getCornerWinY(4),13);
        assertEquals(b.getCornerWinX(5),5);
        assertEquals(b.getCornerWinY(5),1);
        assertEquals(b.getCornerWinX(6),1);
        assertEquals(b.getCornerWinY(6),7);
        assertEquals(b.getCornerWinX(7),0);
        assertEquals(b.getCornerWinY(7),0);
    }

    @Test
    public void checkCorner() throws Exception {
        b.setCorner(3,3);
        assertTrue(b.checkCorner(3,3));
        assertFalse(b.checkCorner(3,5));
    }

    @Test
    public void checkWinner() throws Exception{
        b.setCorner(1,3);
        assertTrue(b.checkIfPlayerWins(6,3));
        assertFalse(b.checkIfPlayerWins(1,3));
        b.setCorner(6,3);

        b.setCorner(2,3);
        assertTrue(b.checkIfPlayerWins(5,3));
        b.setCorner(2,3);

        b.setCorner(3,3);
        assertTrue(b.checkIfPlayerWins(4,3));
        b.setCorner(3,3);

        b.setCorner(4,3);
        assertTrue(b.checkIfPlayerWins(3,3));
        b.setCorner(4,3);

        b.setCorner(5,3);
        assertTrue(b.checkIfPlayerWins(2,3));
        b.setCorner(5,3);

        b.setCorner(6,3);
        assertTrue(b.checkIfPlayerWins(1,3));
        b.setCorner(6,3);

    }


    @Test
    public void setBoardBaseTest(){
        Board br = new Board(3);
        br.setBoardBase(3);
        br.showBoard();

        Board br2 = new Board(4);
        br2.setBoardBase(4);
        br2.showBoard();
    }

}