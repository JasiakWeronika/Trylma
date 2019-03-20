package main.paintstrategy;

import main.*;
import main.checkers.Checker;

import java.awt.*;

/**
 * Implementacja strategii rysowania planszy
 */
public class LowGraphicsBoardPainter implements BoardPainter {

    private Checker checker;
    private static int frameWidth,frameHeight, imageSize;
    private static Rectangle nextBounds, waitingBounds, turnBounds;
    Board board;

    @Override
    public void paint(BoardPanel boardPanel, Graphics g) {
        board = boardPanel.getBoard();
        frameWidth = (int) boardPanel.getBounds().getWidth();
        frameHeight = (int) boardPanel.getBounds().getHeight();

        imageSize = boardPanel.getImageSize();
        checker = new Checker();

        nextBounds = new Rectangle(frameWidth/2+imageSize*board.getBoardWidth()/2,frameHeight/2-imageSize * 2, imageSize * 4, imageSize * 4);
        waitingBounds = new Rectangle(frameWidth/2-imageSize*board.getBoardWidth()/2,frameHeight/2-imageSize*(board.getBoardHeight()-1)/12,imageSize*(board.getBoardWidth()-1), imageSize*board.getBoardHeight()/6);
        turnBounds = new Rectangle(frameWidth/2-imageSize*board.getBoardWidth()/2-imageSize*4,frameHeight/2-imageSize,imageSize*4,imageSize*2);

        g.drawImage(boardPanel.getWood().getImage(), 0, 0, (int) boardPanel.getBounds().getWidth(), (int) boardPanel.getBounds().getHeight(), null);


        if (boardPanel.getYourTurn() == 1) {
            int player = boardPanel.getPlayer();
            g.drawImage(boardPanel.getTurn().getImage(), (int) turnBounds.getX(), (int) turnBounds.getY(), (int) turnBounds.getWidth(), (int) turnBounds.getHeight(), null);
            g.drawImage(checker.getIconImage(player), (int) turnBounds.getX() + (int) turnBounds.getWidth(), (int) turnBounds.getY() + imageSize / 2 - 4, imageSize - 2, imageSize - 2, null);
        }


        for (int i = 1; i < board.getBoardHeight(); i++) {
            for (int j = 1; j < board.getBoardWidth(); j++) {
                Rectangle img = board.getRectangle(i, j, imageSize, frameWidth);
                g.drawImage(checker.getIconImage(board.getBoardField(i, j)), img.x, img.y, img.width, img.height, null);
            }
        }

        g.drawImage(boardPanel.getNext().getImage(), (int) nextBounds.getX(), (int) nextBounds.getY(), (int) nextBounds.getWidth(), (int) nextBounds.getHeight(), null);

        if (Client.getState() == GameState.WAITING) {
            g.drawImage(boardPanel.getWaiting().getImage(), (int) waitingBounds.getX(), (int) waitingBounds.getY(), (int) waitingBounds.getWidth(), (int) waitingBounds.getHeight(), null);
        }
    }
}
