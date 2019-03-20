package main;

public class Bot {
	private int botWinY;
	private int botWinX;
	private int corner, botIndex, base;
	private int[][] botFields;
	private int[] move;
	private Board board;
	private static int ifCorner = 0;

	public Bot(int corner, int index, Board board) {
		this.corner = corner;
		this.botIndex = index;
		this.board = board;
		this.base = board.getBoardBase();
		this.botWinX = board.getCornerWinX(corner);
		this.botWinY = board.getCornerWinY(corner);
		this.botFields = new int[sumCheckers(base)][3];
		setBotCheckers();
		this.move = new int[4];
	}

	public int sumCheckers(int i) {
		if (i == 1) return 1;
		else return i + sumCheckers(i - 1);
	}

	public void setBotCheckers() {
		int k = 0;
		for (int i = 1; i < board.getBoardHeight(); i++) {
			for (int j = 1; j < board.getBoardWidth(); j++) {
				if (board.getBoardField(i, j) == botIndex) {
					botFields[k][0] = i;
					botFields[k][1] = j;
					botFields[k][2] = 0;
					k++;
				}
			}
		}
	}

	public double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
	}

	public int selectRandom(int range) {
		return (int) (Math.random() * range);
	}

	public int getBotCorner() {
		return corner;
	}

	public int getBotIndex() {
		return botIndex;
	}

	public int[] getMove() {
		return move;
	}


	/**
	 * Ulepszony ruch, że moze tez przeskakiwac inne pionki, do 4 skokow
	 */
	public void moveBot() {
		int newX, newY, oldX, oldY, moveX, moveY;
		double minDistance;
		int c, n;

		MoveChecker.resetNeighbours();
		//wybor losowego pionka
		c = selectRandom(botFields.length);
		while (MoveChecker.getNeighboursIndex() == 0) {
			c = selectRandom(botFields.length);
			if (botFields[c][2] == 0) {
				MoveChecker.findNeighbourField(botFields[c][0], botFields[c][1]);
			}

		}
		oldX = botFields[c][0];
		oldY = botFields[c][1];

		//wybiera minDis z neighbour i neighboursacross (jeden skok)
		n = MoveChecker.getNeighboursIndex();
		newX = MoveChecker.getNeighbours()[0][0];
		newY = MoveChecker.getNeighbours()[0][1];
		moveX = newX;
		moveY = newY;
		minDistance = distance(botWinX, botWinY, newX, newY);
		for (int i = 1; i < n; i++) {
			newX = MoveChecker.getNeighbours()[i][0];
			newY = MoveChecker.getNeighbours()[i][1];
			if (distance(botWinX, botWinY, newX, newY) < minDistance) {
				minDistance = distance(botWinX, botWinY, newX, newY);
				moveX = newX;
				moveY = newY;
			}
		}
		n = MoveChecker.getNeighboursAcrossIndex();
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				newX = MoveChecker.getNeighboursAcross()[i][0];
				newY = MoveChecker.getNeighboursAcross()[i][1];
				if (distance(botWinX, botWinY, newX, newY) < minDistance) {
					minDistance = distance(botWinX, botWinY, newX, newY);
					moveX = newX;
					moveY = newY;

					//drugi ruch jesli jest
					MoveChecker.findNeighbourField(newX, newY);
					n = MoveChecker.getNeighboursAcrossIndex();
					if (n > 0) {
						for (int j = 0; j < n; j++) {
							newX = MoveChecker.getNeighboursAcross()[j][0];
							newY = MoveChecker.getNeighboursAcross()[j][1];
							if (distance(botWinX, botWinY, newX, newY) < minDistance) {
								minDistance = distance(botWinX, botWinY, newX, newY);
								moveX = newX;
								moveY = newY;

								//trzeci ruch jesli jest
								MoveChecker.findNeighbourField(newX, newY);
								n = MoveChecker.getNeighboursAcrossIndex();
								if (n > 0) {
									for (int k = 0; k < n; k++) {

										newX = MoveChecker.getNeighboursAcross()[k][0];
										newY = MoveChecker.getNeighboursAcross()[k][1];
										if (distance(botWinX, botWinY, newX, newY) < minDistance) {

											minDistance = distance(botWinX, botWinY, newX, newY);
											moveX = newX;
											moveY = newY;

											//czwarty ruch jesli jest
											MoveChecker.findNeighbourField(newX, newY);
											n = MoveChecker.getNeighboursAcrossIndex();
											if (n > 0) {
												for (int l = 0; l < n; l++) {
													newX = MoveChecker.getNeighboursAcross()[l][0];
													newY = MoveChecker.getNeighboursAcross()[l][1];
													if (distance(botWinX, botWinY, newX, newY) < minDistance) {
														minDistance = distance(botWinX, botWinY, newX, newY);
														moveX = newX;
														moveY = newY;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					//else i konczac zeby wrocic do sprawdzania pozostalych
					MoveChecker.findNeighbourField(oldX, oldY);
					n = MoveChecker.getNeighboursAcrossIndex();
				}
			}
		}

		MoveChecker.moveChecker(moveX, moveY, oldX, oldY, botIndex);
		botFields[c][0] = moveX;
		botFields[c][1] = moveY;
		move[0] = oldX;
		move[1] = oldY;
		move[2] = moveX;
		move[3] = moveY;


		if (board.checkIfInCornerBot(7 - corner, botIndex, moveX, moveY) && ifCorner < sumCheckers(base - 1)) {
			botFields[c][2] = 1;
			ifCorner++;
		}
	}

}
