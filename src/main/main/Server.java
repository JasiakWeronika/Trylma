package main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
	private static Server instance;

	// liczba graczy mogaca polaczyc sie z serwerem
	private static final int maxPlayersCount = 100;
	private static final clientThread[] threads = new clientThread[maxPlayersCount];

	// nazwy graczy
	private static final HashSet<String> names = new HashSet<String>();

	private static int boardBase = 4;
	//tworzymy rozgrywke nieistniejaca jeszcze
	private static Match match = new Match(boardBase);

	static JFrame frame;
	static ServerSocket serverSocket;

	public static Server getInstance() {
		if(instance == null) {
			synchronized(Server.class) {
				if (instance == null) {
					instance = new Server();
				}
			}
		}
		return instance;
	}

	public static void resetInstance() {
		instance = null;
	}

	public static Match getMatch() {
		return match;
	}

	/**
	 * initialization with good performance.
	 * In software engineering, the initialization-on-demand holder (design pattern)
	 * idiom is a lazy-loaded singleton. In all versions of Java, the idiom enables a safe,
	 * highly concurrent lazy initialization with good performance.
	 */

	protected Server() {
		try {
			serverSocket = new ServerSocket(9898);
			System.out.println("The Trylma server is running.");

		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					frame,
					"Server is already in use",
					"Something went wrong",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		while (true) {

			try {
				final Socket clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxPlayersCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxPlayersCount) {
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
					out.println("TRY LATER");
					out.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}


	public static void main(String args[]) throws Exception {
		new Server();
	}

	/**
	 * watek klienta
	 * otwiera strumienie wejscia i wyjscia
	 * pyta o imie klienta
	 * informuje o tym, ze nowy klient dolaczyl do gry lub ja opuscil
	 */
	private static class clientThread extends Thread {
		private BufferedReader in = null;
		private PrintWriter out = null;
		private Socket clientSocket = null;
		private final clientThread[] threads;
		private int maxPlayersCount;
		String name;


		clientThread(Socket clientSocket, clientThread[] threads) {
			this.clientSocket = clientSocket;
			this.threads = threads;
			maxPlayersCount = threads.length;
		}

		public void run() {
			int maxPlayersCount = this.maxPlayersCount;
			clientThread[] threads = this.threads;
			try {
				// tworzy strumien wejscia i wyjscia dla tego klienta
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				// pobieramy imie i sprawdzamy, czy nie ma uzytkownika o takim imieniu
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				this.out.println("PLAYER");

				/** gdy dziala serwer i klient*/
				while (true) {
					String line = in.readLine();

					if (line.startsWith("QUIT")) {
						break;
					}

					synchronized (line) {
						switch (line) {
							case ("GAMESTATE"):
								sendGameState();
								break;
							case ("START"):
								sendGameState();
								if (match.getGameState() == GameState.NOTEXIST) {
									match.setGameState(GameState.CREATING);
									this.out.println("CREATE YOUR GAME");
								} else if (match.getGameState() == GameState.CREATING) {
									this.out.println("CREATING IN PROCESS");
								} else if (match.getGameState() == GameState.WAITING) {
									this.out.println("WATING FOR PLAYERS");

									//dodaje gracza jesli tamten czeka
									for (int p = 0; p < match.getHmp(); p++) {
										if (match.getPlayer(p) == null) {
											createPlayer(p);
											sendGame();
											break;
										}
									}

									int test = 0;
									for (int p = 0; p < match.getHmp(); p++) {
										if (match.getPlayer(p) != null)
											test++;
									}

									if (test == match.getHmp() - match.getHmb()) { //jesli wszyscy gracze ludzcy dolaczyli
										//System.out.println("GRACZE DOLACZYLI");
										match.createBot(); //zapelniamy botami wolne miejsca
										sendGameState();
										match.setGameState(GameState.STARTED);
										sendGameState();
										randomPlayerStart();
										nextTurn();
									}

								} else if (match.getGameState() == GameState.EXIST) {
									this.out.println("GAME IS RUNNING, PLEASE WAIT");
								}

								break;
							case ("HOWMANYPLAYERS"):
								int hmp = Integer.parseInt(in.readLine());
								match.setHmp(hmp);
								match.getBoard().setCheckers(hmp);
								break;
							case ("HOWMANYBOTS"):
								int hmb = Integer.parseInt(in.readLine());
								match.setHmb(hmb);
								createPlayer(0);

								if ((match.getHmp() - match.getHmb()) == 1) { //gra tylko z botami
									match.setGameState(GameState.EXIST);
									match.createBot();
									randomPlayerStart();
									nextTurn();
								} else {  //gram z innymi ludźmi
									match.setGameState(GameState.WAITING);
									sendGameState();
								}
								break;
							case ("ENDTURN"):
								nextTurn();
								break;
							case ("MOVE"):
								int oldX = Integer.parseInt(in.readLine());
								int oldY = Integer.parseInt(in.readLine());
								int newX = Integer.parseInt(in.readLine());
								int newY = Integer.parseInt(in.readLine());
								int playerIndex = Integer.parseInt(in.readLine());
								checkMove(oldX, oldY, newX, newY, playerIndex);
								checkWinner();
								break;
						}
					}

				}

				// usuwa klienta, ktory opuscil gre, aby kolejny mogl dolaczyc
				for (int i = 0; i < maxPlayersCount; i++) {
					if (threads[i] == this) {

						if (match.getGameState() != GameState.NOTEXIST) {
							for (int j = 0; j < match.getHmp(); j++) {
								if (match.getPlayer(j) != null) {
									if (match.getPlayer(j).getThreadIndex() == i) {
										if (match.getTurn() == match.getPlayer(j).getCornerIndex()){ nextTurn();}
										if (!match.getBoard().checkIfPlayerWins(match.getPlayer(j).getCornerIndex(), match.getPlayer(j).getPlayerIndex())) {
											match.setBot(j, new Bot(match.getPlayer(j).getCornerIndex(), match.getPlayer(j).getPlayerIndex(), match.getBoard()));
										}
										match.setPlayer(j, null);
										break;
									}
								}

							}


							if (checkPlayers()) { //wszyscy gracze wyszli
								match = new Match(boardBase);
								sendGameState();
								for (int j = 0; j < match.getHmp(); j++) {
									match.setBot(j, null);
								}
							}

							//jesli ktos wyjdzie w trakcie tworzenia gry
							if (match.getGameState() == GameState.CREATING) {
								match = new Match(boardBase);
								sendGameState();
							}
						}
						threads[i] = null;
					}
				}
				in.close();
				out.close();
				clientSocket.close();
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null) {
					names.remove(name);
				}
			}
		}

		void sendGameState() {

			if (match.getGameState() == GameState.STARTED) {
				for (int i = 0; i < match.getHmp(); i++) {
					if (match.getPlayer(i) != null) {
						threads[match.getPlayer(i).getThreadIndex()].out.println("GAMESTATE");
						threads[match.getPlayer(i).getThreadIndex()].out.println(match.getGameState());
					}
				}
			} else {
				for (int i = 0; i < maxPlayersCount; i++) {
					if (threads[i] != null) {
						this.out.println("GAMESTATE");
						this.out.println(match.getGameState());
					}
				}
			}
		}

		void sendGame() {
			this.out.println("GAME");
			this.out.println(match.getHmp());
		}

		void randomPlayerStart() {
			match.setTurn((int) (Math.random() * match.getHmp() - 1));
		}

		void createPlayer(int playerIndex) {
			for (int i = 0; i < maxPlayersCount; i++) {
				if (threads[i] == this) {
					this.out.println("PLAYERID");
					this.out.println(playerIndex + 2);
					match.setNewPlayer(playerIndex, i);
					break;
				}
			}
		}

	    void nextTurn() {
			int ifPlayer = 0;
			match.nextTurn();
			int turn = match.getTurn();
			for (int i = 0; i < match.getHmp(); i++) {
				if (match.getPlayer(i) != null) {
					if (match.getPlayer(i).getCornerIndex() == turn) {
						threads[match.getPlayer(i).getThreadIndex()].out.println("YOURTURN");
						ifPlayer = 1;
						break;
					}
				} else if (match.getBot(i) != null) {
					if (match.getBot(i).getBotCorner() == turn) {
						try {
							sleep(150); //opoznienie ruchu, zeby za szybko nie przeskakiwal pionek
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						match.getBot(i).moveBot();
						sendMove(match.getBot(i).getMove()[0], match.getBot(i).getMove()[1], match.getBot(i).getMove()[2], match.getBot(i).getMove()[3], match.getBot(i).getBotIndex());
						checkWinner();
						break;
					}
				}
			}
			if (ifPlayer == 0)
				nextTurn();
		}

		void sendMove(int oldX, int oldY, int newX, int newY, int playerIndex) {
			for (int i = 0; i < match.getHmp(); i++) {
				if (match.getPlayer(i) != null) {
					threads[match.getPlayer(i).getThreadIndex()].out.println("MOVE");
					threads[match.getPlayer(i).getThreadIndex()].out.println(oldX);
					threads[match.getPlayer(i).getThreadIndex()].out.println(oldY);
					threads[match.getPlayer(i).getThreadIndex()].out.println(newX);
					threads[match.getPlayer(i).getThreadIndex()].out.println(newY);
					threads[match.getPlayer(i).getThreadIndex()].out.println(playerIndex);
				}
			}
		}

		void checkMove(int oldX, int oldY, int newX, int newY, int playerIndex) {
			if (match.getBoard().getBoardField(newX, newY) == 1 || (newX == oldX && newY == oldY)) {
				match.getBoard().setBoardField(oldX, oldY, 1);
				match.getBoard().setBoardField(newX, newY, playerIndex);
				sendMove(oldX, oldY, newX, newY, playerIndex);
				out.println("ENDTURN");
			} else {
				out.println("Ruch niepoprawny");
				match.getBoard().showBoard();
				out.println("YOURTURN");
			}
		}

		void checkWinner() {

			for (int i = 0; i < match.getHmp(); i++) {
				if (match.getPlayer(i) != null) {
					if (match.getBoard().checkIfPlayerWins(match.getPlayer(i).getCornerIndex(), match.getPlayer(i).getPlayerIndex())) {
						for (int j = 0; j < match.getHmp(); j++) {
							if (match.getPlayer(j) != null) {
								threads[match.getPlayer(j).getThreadIndex()].out.println("PLAYER WON");
								threads[match.getPlayer(j).getThreadIndex()].out.println(match.getPlayer(i).getPlayerIndex());
							}
						}
						match.setPlayer(i,null);
						break;
					}

				} else if (match.getBot(i) != null) {
					if (match.getBoard().checkIfPlayerWins(match.getBot(i).getBotCorner(), match.getBot(i).getBotIndex())) {
						for (int j = 0; j < match.getHmp(); j++) {
							if (match.getPlayer(j) != null) {
								threads[match.getPlayer(j).getThreadIndex()].out.println("BOT WON");
							}
						}
						match.setBot(i, null);
						break;
					}
				}
			}

			if (checkPlayers()) {
				match = new Match(boardBase);
				sendGameState();
				for (int j = 0; j < match.getHmp(); j++) {
					match.setBot(j, null);
				}

			}
		}
	}

	static boolean checkPlayers() {
		int pl = 0;
		for (int j = 0; j < match.getHmp(); j++) {
			if (match.getPlayer(j) == null) {
				pl++;
			}
		}

		if (pl == match.getHmp()) { //wszyscy gracze wyszli lub wygrali
			return true;
		}
		return false;
	}
}