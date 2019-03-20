package main;

import main.paintstrategy.BoardPainter;
import main.paintstrategy.HighGraphicsBoardPainter;
import main.paintstrategy.LowGraphicsBoardPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class BoardPanel extends JPanel {

    private static ImageIcon wood, next, waiting, turn;
    private static int imageSize, player, frameWidth, frameHeight;
    private static MouseHandling mouseHandling;
    private static int yourTurn = 0;
    private static Board board;
    // strategia rysowania planszy
    private BoardPainter boardPainter;

    BoardPanel(int imageSize, int boardBase) {//, int player, int howManyPlayers
		this.imageSize = imageSize;
		//this.player = player; //do usuniecia? prawdopodobnie tak
		board = new Board(boardBase);
		//board.setCheckers(howManyPlayers); //do usuniecia? prawdopodobnie tak

		wood = new ImageIcon(this.getClass().getResource("/darkwood.jpg"));
		next = new ImageIcon(this.getClass().getResource("/next.png"));
		waiting = new ImageIcon(this.getClass().getResource("/waiting.png"));
		turn = new ImageIcon(this.getClass().getResource("/yourturn.png"));

		mouseHandling = new MouseHandling();
		addMouseListener(mouseHandling);

		this.boardPainter = new LowGraphicsBoardPainter();
	}

    public void setBoardPainter(String strategy) {
		switch (strategy) {
			case ("CIRCLE"):
				boardPainter = new LowGraphicsBoardPainter();
				break;
			case ("HEXAGON"):
				boardPainter = new HighGraphicsBoardPainter();
				break;
		}
	}

    public static ImageIcon getWood() {
		return wood;
	}

    public static ImageIcon getNext() {
		return next;
	}

    public static ImageIcon getWaiting() {
		return waiting;
	}

    public static ImageIcon getTurn() {
		return turn;
	}

    public static int getImageSize() {
		return imageSize;
	}

    public static int getPlayer() {
		return player;
	}

    public static int getYourTurn() {
		return yourTurn;
	}

    public static Board getBoard() { return board; }

    public static void setHowManyPlayers(int howManyPlayers) {
		board.setCheckers(howManyPlayers);
	}

    public static void setPlayer(int playerID) {
		player = playerID;
	}

    public static void setYourTurn() {
		yourTurn = 1;
	}

    public static void setEndTurn() {
		yourTurn = 0;
	}


    class MouseHandling extends MouseAdapter {

		private int x, y, mouseX, mouseY, oldX, oldY;
		private int mouseAction = 0;

		public void mousePressed(MouseEvent e) {
			frameWidth = (int) getBounds().getWidth();
			frameHeight = (int) getBounds().getHeight();
			Rectangle nextBounds = new Rectangle(frameWidth / 2 + imageSize * board.getBoardWidth() / 2, frameHeight / 2 - imageSize * 2, imageSize * 4, imageSize * 4);

			x = e.getX();
			y = e.getY();

			if (yourTurn == 1) { //jeżeli jest nasza tura

				if (nextBounds.contains(x, y)) { //jeżeli klikamy na przycisk końca tury
					if (mouseAction != 0) {
						mouseAction = 0; //koniec ruchu
						MoveChecker.resetNeighbours();
						Client.sendMove(oldX, oldY, mouseX, mouseY, player);
					} else {
						yourTurn = 0;
						Client.sendEndTurn();
					}
				}

				for (int i = 1; i < board.getBoardHeight(); i++) {
					for (int j = 1; j < board.getBoardWidth(); j++) {

						if (mouseAction <= 1) {
							if (board.getBoardField(i, j) == player) {
								if (board.getRectangle(i, j, imageSize, frameWidth).contains(x, y)) {
									oldX = i;
									oldY = j;
									mouseX = i;
									mouseY = j;
									MoveChecker.resetNeighbours();
									MoveChecker.findNeighbourField(i, j);
									MoveChecker.setNeighbours();
									MoveChecker.setNeighboursAcross();
									mouseAction = 1;
								}
							}
						}

						if (mouseAction >= 1) {
							//jezeli wybierzemy zwykle pole neighbours
							if (mouseAction == 1 && board.getRectangle(i, j, imageSize, frameWidth).contains(x, y) && MoveChecker.checkNeighbours(i, j) == 1) {
								MoveChecker.resetNeighbours();
								Client.sendMove(oldX, oldY, i, j, player);
								mouseAction = 0;
							}

							//jezeli wybierzemy pole neighbours across
							if (mouseAction >= 1 && board.getRectangle(i, j, imageSize, frameWidth).contains(x, y) && MoveChecker.checkNeighboursAcross(i, j) == 1) {
								MoveChecker.moveChecker(i, j, mouseX, mouseY, player);
								mouseX = i;
								mouseY = j;
								mouseAction = 2;
								MoveChecker.findNeighbourField(i, j); //szukamy sasiadow across nowego pola
								MoveChecker.setNeighboursAcross();
							}
						}
					}
				}
				repaint();
			}
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
		}

	}

    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		boardPainter.paint(this, g);
	}
}