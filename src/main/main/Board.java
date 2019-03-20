package main;

import java.awt.*;

public class Board {

	private static int boardHeight;
	private static int boardWidth;
	private static int[][] board;
	private static int center;
	private static int base;

	public Board(int base) {
		this.base = base;
		boardHeight = 4 * base + 1 + 2;
		boardWidth = 3 * base + 1 + 2;
		board = new int[boardHeight][boardWidth];
		center = boardWidth / 2;
		setBoardBase(base);
	}

	public static void setOneWholeLine(int i) {
		for (int j = 1; j < boardWidth - 1; j++) {
			board[i][j] = 1;
		}
	}

	public static void setBoardBase(int base) {
		int tmpCenter = center;
		for (int i = 1; i <= base; i++) { //wypelnia gore i dol
			for (int k = 0; k < i; k++) board[i][tmpCenter + k] = 1;
			for (int k = 0; k < i; k++) board[boardHeight - 1 - i][tmpCenter + k] = 1;
			if (i % 2 == 1) tmpCenter--;
		}
		//wypelnia reszte
		for (int i = base + 1; i < boardWidth - 1; i++) {
			int c = 1;
			int b = 1;
			setOneWholeLine(i);
			if (base % 2 == 0) {
				while (b < base) {
					if (i > base + b && i < boardHeight - 1 - base - b) board[i][boardWidth - 1 - c] = 0;
					if (i > base + b + 1 && i < boardHeight - 1 - base - (b + 1)) board[i][c] = 0;
					c++;
					b = b + 2;
				}
			} else {
				b = 2;
				while (b < base + 2) {
					if (i >= base + b + 1 && i <= boardHeight - 1 - base - (b + 1)) board[i][boardWidth - 1 - c] = 0;
					if (i >= base + b && i <= boardHeight - 1 - base - b) board[i][c] = 0;
					c++;
					b = b + 2;
				}
			}
		}
	}

	public static int getBoardField(int i, int j) {
		return board[i][j];
	}

	public static int getBoardHeight() {
		return boardHeight;
	}

	public static int getBoardWidth() {
		return boardWidth;
	}

	public static void showBoard() {
		for (int i = 0; i < boardHeight; i++) {
			if (i % 2 == 0) System.out.print(" "); //pokazuje przesuniete linie
			for (int j = 0; j < boardWidth; j++) {
				//if(getBoardField(i, j)==1) System.out.print(getBoardField(i, j) + " ");
				//else System.out.print(" " + " ");
				System.out.print(getBoardField(i, j) + " ");
			}
			System.out.println();
		}
	}

	public static int getBoardBase(){
		return base;
	}

	public static void setBoardField(int i, int j, int fieldIndex) {
		board[i][j] = fieldIndex;
	}

	public int getCornerWinX(int playerCorner) {
		switch (playerCorner) {
			case 1:
				return boardHeight - 2;
			case 3:
				return boardHeight - 2 - base;
			case 5:
				return base + 1;
			case 6:
				return 1;
			case 4:
				return base + 1;
			case 2:
				return boardHeight - 2 - base;
		}
		return 0;
	}

	public int getCornerWinY(int playerCorner) {
		switch (playerCorner) {
			case 1:
				return boardWidth / 2;
			case 3:
				return 1;
			case 5:
				return 1;
			case 6:
				return boardWidth / 2;
			case 4:
				return boardWidth - 2;
			case 2:
				return boardWidth - 2;
		}
		return 0;
	}

	public static Rectangle getRectangle(int i, int j, int imageSize, int frameWidth) {
		if ((i % 2 == 0)) return new Rectangle(j * imageSize + (frameWidth/2 - imageSize*boardWidth/2), i * imageSize, imageSize - 2, imageSize - 2);
		else return new Rectangle(j * imageSize - imageSize / 2 + (frameWidth/2 - imageSize*boardWidth/2), i * imageSize, imageSize - 2, imageSize - 2);
	}

	public static int[][] getBoard() {
		return board;
	}

	public static int getCorner(int hmp, int playerIndex) {
		switch (hmp) {
			case 2:
				if (playerIndex == 2) return 1;
				if (playerIndex == 3) return 6;
				break;
			case 3:
				if (playerIndex == 2) return 2;
				if (playerIndex == 3) return 3;
				if (playerIndex == 4) return 6;
				break;
			case 4:
				return playerIndex;
			case 6:
				return playerIndex - 1;
		}
		return 0;
	}

	public static void setCheckers(int howManyPlayers) {
		switch (howManyPlayers) {
			case 2:
				setCorner(1, 2);
				setCorner(6, 3);
				break;
			case 3:
				setCorner(2, 2);
				setCorner(3, 3);
				setCorner(6, 4);
				break;
			case 4:
				setCorner(2, 2);
				setCorner(3, 3);
				setCorner(4, 4);
				setCorner(5, 5);
				break;
			case 6:
				setCorner(1, 2);
				setCorner(2, 3);
				setCorner(3, 4);
				setCorner(4, 5);
				setCorner(5, 6);
				setCorner(6, 7);
				break;
		}
	}

	public static void setCorner(int cornerIndex, int playerIndex) {
		int tmpCenter = center;
		switch (cornerIndex) {
			case 1:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) board[i][tmpCenter + k] = playerIndex;
					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 3:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						board[boardHeight - 1 - (1 + 2 * base) - i][tmpCenter + base + k] = playerIndex;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 5:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) board[i + (1 + 2 * base)][tmpCenter + base + k] = playerIndex;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 6:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) board[boardHeight - 1 - i][tmpCenter + k] = playerIndex;
					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 4:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) board[i + (1 + 2 * base)][tmpCenter - 1 - base + k] = playerIndex;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 2:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						board[boardHeight - 1 - (1 + 2 * base) - i][tmpCenter - 1 - base + k] = playerIndex;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
		}
	}

	public static boolean checkIfPlayerWins(int cornerIndex, int playerIndex) {
		switch (cornerIndex) {
			case 1:
				if (board[boardHeight - 2][center] == playerIndex)
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				break;
			case 3:
				if (board[boardHeight - 2 - base][1] == playerIndex)
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				break;

			case 5:
				if (board[1 + base][1] == playerIndex)
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				break;
			case 6:
				if (board[1][center] == playerIndex) {
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				}
				break;
			case 4:
				if (board[base + 1][boardWidth - 2] == playerIndex)
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				break;
			case 2:
				if (board[boardHeight - 2 - base][boardWidth - 2] == playerIndex)
					if (checkCorner(7 - cornerIndex, playerIndex))
						return true;
				break;
		}
		return false;
	}


	public static boolean checkCorner(int cornerIndex, int playerIndex) {
		int tmpCenter = center;
		switch (cornerIndex) {
			case 1:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) if (board[i][tmpCenter + k] != playerIndex) return false;
					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 3:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						if (board[boardHeight - 1 - (1 + 2 * base) - i][tmpCenter + base + k] != playerIndex)
							return false;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 5:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						if (board[i + 1 + 2 * base][tmpCenter + base + k] != playerIndex) return false;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 6:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						if (board[boardHeight - 1 - i][tmpCenter + k] != playerIndex) return false;
					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 4:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						if (board[i + 1 + 2 * base][tmpCenter - 1 - base + k] != playerIndex) return false;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 2:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++)
						if (board[boardHeight - 1 - (1 + 2 * base) - i][tmpCenter - 1 - base + k] != playerIndex)
							return false;
					if (i % 2 == 0) tmpCenter--;
				}
				break;
		}
		return true;
	}

	public static boolean checkIfInCornerBot(int cornerIndex, int playerIndex, int x, int y) {
		int tmpCenter = center;
		switch (cornerIndex) {
			case 1:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) {
						if(getBoardField(i,tmpCenter + k)==playerIndex) {
							if (x == i && y == tmpCenter + k) {
								return true;
							}
						} else return false;
					}

					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 3:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++){
						if(getBoardField(boardHeight - 1 - (1 + 2 * base) - i,tmpCenter + base + k)==playerIndex) {
							if (x == boardHeight - 1 - (1 + 2 * base) - i && y == tmpCenter + base + k) {
								return true;
							}
						} else return false;
					}
					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 5:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) {
						if(getBoardField(i + 1 + 2 * base,tmpCenter + base + k)==playerIndex) {
							if (x == i + 1 + 2 * base && y == tmpCenter + base + k) {
								return true;
							}
						} else return false;
					}

					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 6:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) {
						if(getBoardField(boardHeight - 1 - i,tmpCenter + k)==playerIndex) {
							if (x == boardHeight - 1 - i && y == tmpCenter + k) {
								return true;
							}
						} else return false;
					}

					if (i % 2 == 1) tmpCenter--;
				}
				break;
			case 4:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++) {
						if(getBoardField(i + 1 + 2 * base,tmpCenter - 1 - base + k)==playerIndex) {
							if (x == i + 1 + 2 * base && y == tmpCenter - 1 - base + k) {
								return true;
							}
						} else return false;
					}

					if (i % 2 == 0) tmpCenter--;
				}
				break;
			case 2:
				for (int i = 1; i <= base; i++) {
					for (int k = 0; k < i; k++){
						if(getBoardField(boardHeight - 1 - (1 + 2 * base) - i,tmpCenter - 1 - base + k)==playerIndex) {
							if (x == boardHeight - 1 - (1 + 2 * base) - i  && y == tmpCenter - 1 - base + k) {
								return true;
							}
						} else return false;
					}

					if (i % 2 == 0) tmpCenter--;
				}
				break;
		}
		return false;
	}
}