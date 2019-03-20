package main.checkers;

import javax.swing.*;
import java.awt.*;

public class Checker {

	private ImageIcon blue, green, red, yellow, gray, neighbour,orange,violet;

	public Checker() {
		initIcons();
	}

	public void initIcons() {
		gray = new ImageIcon(this.getClass().getResource("/field.png"));
		red = new ImageIcon(this.getClass().getResource("/red.png"));
		blue = new ImageIcon(this.getClass().getResource("/blue.png"));
		green = new ImageIcon(this.getClass().getResource("/green.png"));
		yellow = new ImageIcon(this.getClass().getResource("/yellow.png"));
		orange = new ImageIcon(this.getClass().getResource("/orange.png"));
		violet = new  ImageIcon(this.getClass().getResource("/violet.png"));
		neighbour = new ImageIcon(this.getClass().getResource("/neighbour.png"));
	}

	public Image getIconImage(int field) {
		switch (field) {
			case 1:
				return gray.getImage(); //pole zwykle
			case 2:
				return violet.getImage(); //pionek
			case 3:
				return green.getImage(); //pionek
			case 4:
				return orange.getImage(); //pionek
			case 5:
				return blue.getImage(); //pionek
			case 6:
				return red.getImage(); //pionek
			case 7:
				return yellow.getImage(); //pionek
			case 8:
				return neighbour.getImage(); //pole, na ktore mozna sie ruszyc
			default:
				return null;
		}
	}
}
