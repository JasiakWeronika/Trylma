package main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	// strumien wyjscia
	private static PrintWriter out = null;

	private Game frame = new Game();
	private static int playerID, hmp;
	private static int boardBase = 4;

	private static GameState state;

	//pobieramy IP
	private String getServerAddress() {
		return JOptionPane.showInputDialog(
				frame,
				"Enter IP Address of the Server:",
				"Welcome to the Game",
				JOptionPane.QUESTION_MESSAGE);
	}

	// pobieramy imie/nick
	private String getName() {
		return JOptionPane.showInputDialog(
				frame,
				"Enter your nickname:",
				"Game nickname",
				JOptionPane.PLAIN_MESSAGE);
	}

	// gdy serwer jest przeciazony
	private void tryLater() {
		JOptionPane.showMessageDialog(
				frame,
				"Server is too busy. Try later!",
				"Sorry",
				JOptionPane.WARNING_MESSAGE);
	}


	private void showErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(
				frame,
				errorMessage,
				"Sorry",
				JOptionPane.WARNING_MESSAGE);
	}

	private void showErrorMessage() {
		showErrorMessage("Something went wrong :(");
	}

	private void botWon() {
		JOptionPane.showMessageDialog(
				frame,
				"Bot won the game!",
				"We have a winner!",
				JOptionPane.WARNING_MESSAGE);
	}

	private void playerWon(int playerIndex) {
		JOptionPane.showMessageDialog(
				frame,
				"Player " + playerIndex + " won the game!",
				"We have a winner!",
				JOptionPane.WARNING_MESSAGE);
	}


	private void load() {
		JOptionPane.showMessageDialog(
				frame,
				"Creating game in process",
				"Loading",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void running() {
		JOptionPane.showMessageDialog(
				frame,
				"Game is running, please wait",
				"Loading",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected Client() {
	}

	public static int getBoardBase() {
		return boardBase;
	}

	public static GameState getState() {
		return state;
	}

	public static void sendQuit() {
		out.println("QUIT");
	}

	public static void sendStart() {
		out.println("START");
	}

	public static void sendState() {
		out.println("GAMESTATE");
	}

	public static void sendEndTurn() {
		out.println("ENDTURN");
	}

	public static void sendHowManyPlayers(int players) {
		out.println("HOWMANYPLAYERS");
		out.println(players);
	}

	public static void sendHowManyBots(int bots) {
		out.println("HOWMANYBOTS");
		out.println(bots);
	}

	static void sendMove(int oldX, int oldY, int newX, int newY, int player) {
		out.println("MOVE");
		out.println(oldX);
		out.println(oldY);
		out.println(newX);
		out.println(newY);
		out.println(player);
	}

	private void listen(BufferedReader in) throws IOException {
		String responseLine;
		while ((responseLine = in.readLine()) != null) {
			//System.out.println("SERWER:  " + responseLine); //patrzymy co dostalismy od serwera

		    synchronized(responseLine) {
			switch (responseLine) {
			    case ("SUBMITNAME"):
				String name = getName();
				if (name != null) {
				    out.println(name);
				} else {
				    showErrorMessage("No player name was given! Try again!");
				    sendQuit();
				    System.exit(0);
				}
				break;
			    case ("TRY LATER"):
				tryLater();
				frame.setVisible(false);
				break;
			    case ("GAMESTATE"):
				state = GameState.valueOf(in.readLine());
				break;
			    case ("GAME"):
				hmp = Integer.parseInt(in.readLine());
				BoardPanel.setHowManyPlayers(hmp);
				break;
			    case ("PLAYER"):
				frame.setVisible(true);
				break;
			    case ("PLAYERID"):
				playerID = Integer.parseInt(in.readLine());
				BoardPanel.setPlayer(playerID);
				break;
			    case ("YOURTURN"):
				BoardPanel.setYourTurn();
				break;
			    case ("ENDTURN"):
				BoardPanel.setEndTurn();
				Client.sendEndTurn();
				break;
			    case ("MOVE"):
				int oldX = Integer.parseInt(in.readLine());
				int oldY = Integer.parseInt(in.readLine());
				int newX = Integer.parseInt(in.readLine());
				int newY = Integer.parseInt(in.readLine());
				int playerIndex = Integer.parseInt(in.readLine());
				Board.setBoardField(oldX, oldY, 1);
				Board.setBoardField(newX, newY, playerIndex);
				break;
			    case ("BOT WON"):
				botWon();
				break;
			    case ("PLAYER WON"):
				int pi = Integer.parseInt(in.readLine());
				playerWon(pi);
				if (pi == playerID) {
				    sendQuit();
				    System.exit(0);
				}
				break;
			    case ("CREATING IN PROCESS"):
				load();
				break;
			    case ("GAME IS RUNNING, PLEASE WAIT"):
				running();
				break;
			}
			frame.getContentPane().repaint();
		    }
		}
	}

	// tworzenie watku, ktory czyta serwer
	public void run() {
		String serverAddress = getServerAddress();
		try {
			if (serverAddress == null) {
				showErrorMessage("Wrong server address! Try again!");
				System.exit(0);
			}
			Socket clientSocket = new Socket(serverAddress, 9898);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			listen(in);
		} catch (IOException e) {
			showErrorMessage();
			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.run();
	}
}