package main.checkers;

import javax.swing.*;
import java.awt.*;

public class Checker2 {

    private ImageIcon blue, green, red, yellow, gray, neighbour,orange,violet;

    public Checker2() {
	initIcons();
    }

    public void initIcons() {
		gray = new ImageIcon(this.getClass().getResource("/field2.png"));
		red = new ImageIcon(this.getClass().getResource("/red2.png"));
		blue = new ImageIcon(this.getClass().getResource("/blue2.png"));
		green = new ImageIcon(this.getClass().getResource("/green2.png"));
		yellow = new ImageIcon(this.getClass().getResource("/yellow2.png"));
		orange = new ImageIcon(this.getClass().getResource("/orange2.png"));
		violet = new ImageIcon(this.getClass().getResource("/violet2.png"));
		neighbour = new ImageIcon(this.getClass().getResource("/neighbour2.png"));
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
