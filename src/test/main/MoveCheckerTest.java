package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveCheckerTest {

    Board b;

    @Before
    public void setUp(){
        b=new Board(4);
    }

    @Test
    public void findNeighbourField() throws Exception {
        MoveChecker.findNeighbourField(1,1);
        MoveChecker.setNeighbours();
        assertEquals(b.getBoardField(1,1),0);
        assertEquals(b.getBoardField(1,2),0);
        assertEquals(b.getBoardField(2,1),0);
        assertEquals(MoveChecker.checkNeighbours(1,1),0);
        assertEquals(MoveChecker.getNeighboursIndex(),0);
        assertEquals(MoveChecker.getNeighboursAcrossIndex(),0);
        assertNotNull(MoveChecker.getNeighbours());
        assertNotNull(MoveChecker.getNeighboursAcross());


        b.setBoardField(6,7,5);
        MoveChecker.findNeighbourField(7,7);
        MoveChecker.setNeighbours();
        assertEquals(b.getBoardField(7,7),1);
        assertEquals(b.getBoardField(7,8),8);
        assertEquals(MoveChecker.checkNeighbours(7,6),1);
        assertEquals(MoveChecker.getNeighboursIndex(),5);
        assertEquals(MoveChecker.getNeighboursAcrossIndex(),1);
    }

    @Test
    public void setNeighboursAcross() throws Exception {
        b.setBoardField(1,2,2);
        b.setBoardField(1,3,1);
        MoveChecker.findNeighbourField(1,1);
        MoveChecker.setNeighboursAcross();
        assertEquals(MoveChecker.checkNeighboursAcross(1,2),0);
        assertEquals(MoveChecker.checkNeighboursAcross(1,3),1);
    }

    @Test
    public void moveChecker() throws Exception {
        MoveChecker.moveChecker(1,1,4,4,4);
        assertEquals(b.getBoardField(1,1),4);
        assertEquals(b.getBoardField(4,4),1);
    }

    @Test
    public void getNeighbours() throws Exception {
        b.setBoardBase(4);

    }



}