package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BotTest {

    Board board;
    Bot bot,bot2;

    @Before
    public void setUp(){
        board = new Board(4);
        bot = new Bot(1, 3, board); //ta druga wartosc  (index) to jakby kolor bota
        board.setCorner(1,3);
        bot.setBotCheckers();
    }

    @Test
    public void checkBotMove() {
        Assert.assertTrue(board.checkCorner(bot.getBotCorner(),bot.getBotIndex()));
        Assert.assertFalse(board.checkCorner(7-bot.getBotCorner(),bot.getBotIndex()));
        while(board.checkIfPlayerWins(bot.getBotCorner(),bot.getBotIndex())==false) {
            bot.moveBot();
        }
        Assert.assertTrue(board.checkIfPlayerWins(bot.getBotCorner(),bot.getBotIndex()));
    }

    @Test
    public void checkTwoBotsMove() {
        bot2 = new Bot(6, 4, board); //ta druga wartosc  (index) to jakby kolor bota
        board.setCorner(6,4);
        bot2.setBotCheckers();

        Assert.assertTrue(board.checkCorner(bot.getBotCorner(),bot.getBotIndex()));
        Assert.assertFalse(board.checkCorner(7-bot.getBotCorner(),bot.getBotIndex()));
        Assert.assertTrue(board.checkCorner(bot2.getBotCorner(),bot2.getBotIndex()));
        Assert.assertFalse(board.checkCorner(7-bot2.getBotCorner(),bot2.getBotIndex()));

        while(board.checkIfPlayerWins(bot.getBotCorner(),bot.getBotIndex())==false  && board.checkIfPlayerWins(bot2.getBotCorner(),bot2.getBotIndex())==false) {
            bot.moveBot();
            bot2.moveBot();
        }
        Assert.assertTrue(board.checkIfPlayerWins(bot.getBotCorner(),bot.getBotIndex()) || board.checkIfPlayerWins(bot2.getBotCorner(),bot2.getBotIndex()));
    }

    @Test
    public void checkSumCheckers(){
        Assert.assertEquals(bot.sumCheckers(1),1);
        Assert.assertEquals(bot.sumCheckers(2),3);
        Assert.assertEquals(bot.sumCheckers(4),10);
    }

    @Test
    public void checkDistance(){
        Assert.assertNotNull(bot.distance(4,5,6,7));
    }

    @Test
    public void checkRandom(){
        Assert.assertNotNull(bot.selectRandom(3));
        for(int i=0; i<100; i++){
            Assert.assertTrue(bot.selectRandom(3)<3);
            Assert.assertTrue(bot.selectRandom(3)>=0);
        }
    }

    @Test
    public void checkBotIndexes(){
       Assert.assertEquals(bot.getBotCorner(),1);
        Assert.assertEquals(bot.getBotIndex(),3);
    }

    @Test
    public void checkGetMove(){
        Assert.assertNotNull(bot.getMove());
    }
}
