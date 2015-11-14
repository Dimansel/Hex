package org.dimansel.hex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Window extends JFrame implements MouseListener, KeyListener {
    public static final int WIDTH = 760;
    public static final int HEIGHT = 510;
    private Field f;

    public Window() {
        /**
         * INITIALIZING WINDOW
         */
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((size.width - WIDTH) / 2, (size.height - HEIGHT) / 2);
        setSize(WIDTH, HEIGHT);
        setTitle("Hex Game");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        /**
         * INITIALIZING GAME
         */
        f = new Field(9, 30, Color.red, Color.blue);
        add(f);
        f.addMouseListener(this);
        addKeyListener(this);
    }

    public static void main(String[] args) {
        new Window();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        f.onClick(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_N) {
            f.clear();
            f.isGameOver = false;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
