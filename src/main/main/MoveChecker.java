package main;

public class MoveChecker {

	private static int[][] neighbours = new int[6][2];
	private static int[][] neighboursAcross = new int[6][2];
	private static int neighboursIndex, neighboursAcrossIndex, iAcross, jAcross;
	private static int limitX, limitY;

	public static void findNeighbourField(int x, int y) {
		neighboursIndex = 0;
		neighboursAcrossIndex = 0;

		limitX = x;
		limitY = y;
		if (x % 2 == 0) limitY++;

		//przejezdzam po 6 miejsach dookola
		for (int i = limitX - 1; i <= limitX + 1; i++) {
			for (int j = limitY - 1; j < limitY + 1; j++) {

				//tylko zamiast srodka daje pole po prawej lub po lewej stronie, ktore nie jest uwzglednione w forze
				if (i == x && j == y) {
					if (x % 2 == 0) j = y - 1;
					else j = y + 1;
				}

				//jezeli to pole jest puste (bo == 1) to je zapisujemy do tablicy sasiadow i zwiekszamy index tej tablicy
				if (Board.getBoard()[i][j] == 1) {
					neighbours[neighboursIndex][0] = i;
					neighbours[neighboursIndex][1] = j;
					neighboursIndex++;
				}
				//jezeli to pole nie jest puste, ale znajduje sie na planszy (czyli jest tam inny pionek) to szukamy pola na przeciwko?
				else if (Board.getBoard()[i][j] != 0) {
					iAcross = x + 2 * (i - x); //2*i - x

					//to mozna jakos zebrac chyba ladnie
					jAcross = y + 2 * (j - y); //2*j - y
					if (x % 2 == 1 && i != x) jAcross++;
					if (x % 2 == 0 && i != x) jAcross--;

					//sprawdzamy czy pole po przeskoku jest puste, jak tak no to zapisujemy do sasiadow
					if (Board.getBoard()[iAcross][jAcross] == 1) {
						neighboursAcross[neighboursAcrossIndex][0] = iAcross;
						neighboursAcross[neighboursAcrossIndex][1] = jAcross;
						neighboursAcrossIndex++;
					}
				}

				//przywracam j do normalnej postaci po tym jak zmienilam go na prawo albo lewo od srodka
				if (i == x && j == y + 1 && x % 2 == 1) j = y;
				if (i == x && j == y - 1 && x % 2 == 0) j = y;

			}
		}
	}

	public static void setNeighbours() {
		for (int k = 0; k < neighboursIndex; k++)
			Board.setBoardField(neighbours[k][0], neighbours[k][1], 8);
	}

	public static void setNeighboursAcross() {
		for (int k = 0; k < neighboursAcrossIndex; k++)
			Board.setBoardField(neighboursAcross[k][0], neighboursAcross[k][1], 8);
	}


	public static int checkNeighbours(int i, int j) {
		for (int k = 0; k < neighboursIndex; k++)
			if (neighbours[k][0] == i && neighbours[k][1] == j) return 1;
		return 0;
	}

	public static int checkNeighboursAcross(int i, int j) {
		for (int k = 0; k < neighboursAcrossIndex; k++)
			if (neighboursAcross[k][0] == i && neighboursAcross[k][1] == j) return 1;
		return 0;
	}

	public static void moveChecker(int newX, int newY, int oldX, int oldY, int player) {
		resetNeighbours();
		Board.setBoardField(newX, newY, player);
		Board.setBoardField(oldX, oldY, 1);
	}

	public static void resetNeighbours() {
		for (int k = 0; k < neighboursIndex; k++) Board.setBoardField(neighbours[k][0], neighbours[k][1], 1);
		for (int k = 0; k < neighboursAcrossIndex; k++)
			Board.setBoardField(neighboursAcross[k][0], neighboursAcross[k][1], 1);
		neighboursIndex = 0;
		neighboursAcrossIndex = 0;
	}

	public static int getNeighboursIndex() {
		return neighboursIndex;
	}

	public static int[][] getNeighbours() {
		return neighbours;
	}

	public static int getNeighboursAcrossIndex() {
		return neighboursAcrossIndex;
	}

	public static int[][] getNeighboursAcross() {
		return neighboursAcross;
	}

}