package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game extends JFrame implements ActionListener {
    private static Game game;
    private JPanel gameNumber, gamePlayers, gameMenu, gameBoard, gameBoardShape;
    private MyButton[] players = new MyButton[7];
    private MyButton[] bot = new MyButton[7];
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int width = (int) screenSize.getWidth();
    private static int height = (int) screenSize.getHeight();
    private int boardBase, imageSize;
    private static main.BoardPanel boardPanel;

    class MyButton extends JButton {
		MyButton(String text, String action, Color color) {
			setBorderPainted(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setText(text);
			setActionCommand(action);
			setForeground(color);
		}
	}

    Game() {

		boardBase = Client.getBoardBase();
		imageSize = height / (4 * boardBase + 1 + 2);

		//USTAWIENIA OKNA GRY
		this.setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Trylma");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		//MENU GRY
		gameMenu = new JPanel();
		gameMenu.setBounds(0, 0, width, height);
		gameMenu.setLayout(null);
		gameMenu.setBackground(new Color(253, 239, 220));

		//WYBÓR KSZTAŁTU PIONKÓW
		gameBoardShape = new JPanel();
		gameBoardShape.setBounds(0, 0, width, height);
		gameBoardShape.setLayout(null);
		gameBoardShape.setBackground(new Color(253, 239, 220));

		//OBRAZEK PLANSZY
		gameBoard = new JPanel();
		gameBoard.setBounds(width / 3, height / 50, 131 * width / 200, 117 * height / 125);
		gameBoard.setOpaque(false);
		ImageIcon wood = new ImageIcon(new ImageIcon(this.getClass().getResource("/board.PNG")).getImage().getScaledInstance(131 * width / 200, 117 * height / 125, Image.SCALE_SMOOTH));
		JLabel back = new JLabel();
		back.setBounds(width, height / 50, 131 * width / 200, 117 * height / 125);
		back.setIcon(wood);
		gameBoard.add(back);
		gameMenu.add(gameBoard);

		//LICZBA GRACZY
		gameNumber = new JPanel();
		gameNumber.setBounds(0, 0, width, height);
		gameNumber.setLayout(new GridLayout(3, 2));
		gameNumber.setBackground(new Color(253, 239, 220));

		//LICZBA BOTÓW
		gamePlayers = new JPanel();
		gamePlayers.setBounds(0, 0, width, height);
		gamePlayers.setLayout(new GridLayout(7, 1));
		gamePlayers.setBackground(new Color(253, 239, 220));

		JLabel question4 = new JLabel("TRYLMA", SwingConstants.CENTER);
		question4.setBounds(0, 20 * height / 100, width / 3, 2 * height / 10);
		question4.setForeground(new Color(56, 55, 55));
		question4.setFont(new Font("Jokerman", Font.BOLD, height / 10));
		gameMenu.add(question4);

		JLabel question5 = new JLabel("CHOOSE THE SHAPE OF CHECKERS", SwingConstants.CENTER);
		question5.setBounds(0, 0, width, 2 * height / 10);
		question5.setForeground(new Color(56, 55, 55));
		question5.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		gameBoardShape.add(question5);

		JLabel question1 = new JLabel("CHOOSE THE NUM", SwingConstants.RIGHT);
		question1.setForeground(new Color(56, 55, 55));
		question1.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		gameNumber.add(question1);

		JLabel question2 = new JLabel("BER OF PLAYERS", SwingConstants.LEFT);
		question2.setForeground(new Color(56, 55, 55));
		question2.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		gameNumber.add(question2);

		JLabel question3 = new JLabel("HOW MANY BOTS DO YOU WANT?", SwingConstants.CENTER);
		question3.setForeground(new Color(56, 55, 55));
		question3.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		gamePlayers.add(question3);

		JButton circle = new JButton();
		circle.setIcon(new ImageIcon(this.getClass().getResource("/blue.png")));
		circle.setBounds(0, 3 * height / 10, width / 2, 5 * height / 10);
		circle.setBorderPainted(false);
		circle.setContentAreaFilled(false);
		circle.setFocusPainted(false);
		circle.setActionCommand("CIRCLE");
		circle.addActionListener(this);
		gameBoardShape.add(circle);

		JButton hexagon = new JButton();
		hexagon.setIcon(new ImageIcon(this.getClass().getResource("/green2.png")));
		hexagon.setBounds(width / 2, 3 * height / 10, width / 2, 5 * height / 10);
		hexagon.setBorderPainted(false);
		hexagon.setContentAreaFilled(false);
		hexagon.setFocusPainted(false);
		hexagon.setActionCommand("HEXAGON");
		hexagon.addActionListener(this);
		gameBoardShape.add(hexagon);

		MyButton how = new MyButton("How to play?", "HOW TO PLAY", new Color(104, 88, 95));
		how.setBounds(0, 45 * height / 100, width / 3, height / 10);
		how.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		how.addActionListener(this);
		gameMenu.add(how);

		MyButton start = new MyButton("Start", "START", new Color(104, 88, 95));
		start.setBounds(0, 55 * height / 100, width / 3, height / 10);
		start.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		start.addActionListener(this);
		gameMenu.add(start);

		MyButton author = new MyButton("?", "AUTHOR", new Color(188, 65, 48));
		author.setBounds(0, 85 * height / 100, width / 9, height / 10);
		author.setFont(new Font("Eras Bold ITC", Font.BOLD, height / 20));
		author.addActionListener(this);
		gameMenu.add(author);

		//PRZYCISKI WYBORU GRACZY I BOTOW
		for (int i = 0; i < 7; ++i) {

			bot[i] = new MyButton(" " + i + "", "BOT " + i, new Color(104, 88, 95));
			bot[i].setFont(new Font("Eras Bold ITC", Font.BOLD, height / 10));
			bot[i].addActionListener(this);

			if (i == 2 || i == 3 || i == 4 || i == 6) {
				players[i] = new MyButton(" " + i + " ", "PLAYERS " + i, new Color(104, 88, 95));
				players[i].setFont(new Font("Eras Bold ITC", Font.BOLD, height / 10));
				players[i].addActionListener(this);
			}
		}

		//DODANIE PANELÓW DO OKNA
		this.add(gameBoardShape);
		this.add(gameMenu);
		this.add(gameNumber);
		this.add(gamePlayers);

		//PLANSZA DO GRY
		boardPanel = new BoardPanel(imageSize, boardBase);
		boardPanel.setBounds(0, 0, width, height);
		boardPanel.setLayout(null);
		this.add(boardPanel);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				Client.sendQuit();
				System.exit(0);
			}
		});
	}

    @Override
    public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
			case "CIRCLE":
				gameBoardShape.setVisible(false);
				boardPanel.setBoardPainter("CIRCLE");
				gameMenu.setVisible(true);
				break;
			case "HEXAGON":
				gameBoardShape.setVisible(false);
				boardPanel.setBoardPainter("HEXAGON");
				gameMenu.setVisible(true);
				break;
		}

		switch (e.getActionCommand()) {
			case "START":
				Client.sendState();
				Client.sendState();
				while (Client.getState() == null) Client.sendState();

				Client.sendStart();
				if (Client.getState() == GameState.NOTEXIST) {
					gameMenu.setVisible(false);
					for (int i = 0; i < 7; i++) {
						if (i == 2 || i == 3 || i == 4 || i == 6) {
							gameNumber.add(players[i]);
						}
					}
					gameNumber.setVisible(true);
				} else if (Client.getState() == GameState.WAITING) {
					//System.out.println("MOGE DOLACZYC");
					gameMenu.setVisible(false);
					gameNumber.setVisible(false);
					gamePlayers.setVisible(false);
					boardPanel.setVisible(true);
				} else if (Client.getState() == GameState.CREATING) {
					System.out.println("GRA W TRAKCIE TWORZENIA, SPRÓBUJ PÓŹNIEJ");
				} else if (Client.getState() == GameState.EXIST) {
					System.out.println("GRA W TRAKCIE, SPRÓBUJ PÓŹNIEJ");
				}

				break;
			case "HOW TO PLAY":
				JOptionPane.showMessageDialog(
						game,
						"1. W jednej grze uczestniczy 2, 3, 4 lub 6 graczy. Każdy gracz ma 10 pionów.\n" +
								"2. Gra toczy się na planszy w kształcie sześciopromiennej gwiazdy. Każdy promień zawiera 10 pól, w których\n" +
								"początkowo umieszczane są piony. Wewnętrzny sześciokąt ma 61 pól, na każdy jego bok składa się 5 pól.\n" +
								"3. Celem każdego gracza jest umieszczenie wszystkich swoich pionów w przeciwległym promieniu.\n" +
								"Wygrywa gracz, który dokona tego jako pierwszy. Pozostali gracze mogą kontynuować grę walcząc o kolejne miejsca.\n" +
								"4. Gracz rozpoczynający grę wybierany jest losowo. Następnie gracze wykonują ruchy po kolei zgodnie z\n" +
								"kierunkiem ruchu wskazówek zegara.\n" +
								"5. Ruch polega na przesunięciu piona na sąsiednie puste pole lub na przeskakiwaniu pionem innych pionów\n" +
								"(swoich lub przeciwnika) tak jak w warcabach, ale bez możliwości zbijania pionów. Ruch może odbywać\n" +
								"się w dowolnym kierunku.\n" +
								"6. Dopuszczalne jest pozostawianie swoich pionów w promieniu docelowym innego gracza (tzw. blokowanie).\n" +
								"7. Gracz może zrezygnować z ruchu w danej turze.",
						"How to play",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			case "AUTHOR":
				JOptionPane.showMessageDialog(
						game,
						"Autorzy:\n" +
								"Marta Staroń\n" +
								"Weronika Jasiak\n" +
								"Gra wykonana na potrzeby zajęć z Technologii Programowania\n" +
								"styczeń 2018\n",
						"About game",
						JOptionPane.PLAIN_MESSAGE);
				break;
		}

		switch (e.getActionCommand()) {
			case "PLAYERS 2":
				gameNumber.setVisible(false);
				for (int i = 0; i < 2; i++) {
					gamePlayers.add(bot[i]);
				}
				gamePlayers.setVisible(true);
				boardPanel.setHowManyPlayers(2);
				Client.sendHowManyPlayers(2);
				break;

			case "PLAYERS 3":
				gameNumber.setVisible(false);
				for (int i = 0; i < 3; i++) {
					gamePlayers.add(bot[i]);
				}
				gamePlayers.setVisible(true);
				boardPanel.setHowManyPlayers(3);
				Client.sendHowManyPlayers(3);
				break;

			case "PLAYERS 4":
				gameNumber.setVisible(false);
				for (int i = 0; i < 4; i++) {
					gamePlayers.add(bot[i]);
				}
				gamePlayers.setVisible(true);
				boardPanel.setHowManyPlayers(4);
				Client.sendHowManyPlayers(4);
				break;

			case "PLAYERS 6":
				gameNumber.setVisible(false);
				for (int i = 0; i < 6; i++) {
					gamePlayers.add(bot[i]);
				}
				gamePlayers.setVisible(true);
				boardPanel.setHowManyPlayers(6);
				Client.sendHowManyPlayers(6);
				break;
		}

		switch (e.getActionCommand()) {
			case "BOT 0":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(0);
				break;

			case "BOT 1":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(1);
				break;

			case "BOT 2":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(2);
				break;

			case "BOT 3":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(3);
				break;

			case "BOT 4":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(4);
				break;

			case "BOT 5":
				gamePlayers.setVisible(false);
				boardPanel.setVisible(true);
				Client.sendHowManyBots(5);
				break;
		}
	}
}