package main.paintstrategy;

import main.Board;
import main.BoardPanel;

import java.awt.*;

/**
 * Strategia (sposob) rysowania planszy
 */
public interface BoardPainter {
    void paint(BoardPanel boardPanel, Graphics g);
}
