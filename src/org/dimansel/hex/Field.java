package org.dimansel.hex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Field extends JPanel {
    public int s; //field size (number of cells in each row and column)
    private int r; //cell radius
    private Point[][] fl; //array of cells' coordinates
    private Color[][] fs; //array of cells' state, 0 - none, 1 - red, 2 - blue
    private Point loc = new Point(10, 10); //field location
    private Color p1; //first player's color
    private Color p2; //second player's color
    private boolean first = true; //first player's turn
    private final int cx = 5; //horizontal distance between cells
    private final int cy = -2; //vertical distance between cells
    public boolean isGameOver = false;

    public Field(int size, int cellRadius, Color c1, Color c2) {
        s = size;
        r = cellRadius;
        fs = new Color[s][s];
        fl = new Point[s][s];

        clear();
        p1 = c1;
        p2 = c2;
        locateCells();
    }

    private void locateCells() {
        for (int b = 0; b < s; b++)
            fl[0][b] = new Point((int)(b*(r*Math.sqrt(3)+cx))+cx+loc.x, loc.y);

        for (int a = 1; a < s; a++)
            for (int b = 0; b < s; b++) {
                int x = (int)(fl[a-1][b].x + (r * Math.sqrt(3) + cx)/2);
                int y = (int)(a * (r * Math.sqrt(3) + cy) + loc.y);
                fl[a][b] = new Point(x, y);
            }
    }

    public void clear() {
        first = true;
        for (Color[] cell : fs)
            Arrays.fill(cell, Color.gray);
    }

    private void checkWin(Color clr) {
        ArrayList<Chain> chains = new ArrayList<>();
        ArrayList<Point> start = new ArrayList<>();
        ArrayList<Point> end = new ArrayList<>();
        for (int a = 0; a < s; a++) {
            if (fs[a][0].equals(p1) && clr.equals(p1)) start.add(new Point(a, 0));
            if (fs[a][s-1].equals(p1) && clr.equals(p1)) end.add(new Point(a, s-1));
            if (fs[0][a].equals(p2) && clr.equals(p2)) start.add(new Point(0, a));
            if (fs[s-1][a].equals(p2) && clr.equals(p2)) end.add(new Point(s-1, a));
        }

        if (start.isEmpty() || end.isEmpty()) return;

        for (Point p : start) {
            chains.add(new Chain(p));
            getAllChains(chains, p.x, p.y, clr, chains.size()-1);
        }

        Chain minChain = null;
        int minLength = 0;
        boolean f = true;

        for (Chain c : chains) {
            if (f && c.isFull(clr.equals(p1), s)) {
                minChain = c;
                minLength = c.getLength();
                f = false;
            }

            if (c.isFull(clr.equals(p1), s)) {
                int l = c.getLength();
                if (l < minLength) {
                    minChain = c;
                    minLength = l;
                }
            }
        }

        if (minChain != null) {
            minChain.lightUp(fs, clr);
            isGameOver = true;
        }
    }

    private void getAllChains(ArrayList<Chain> chains, int a, int b, Color clr, int i) {
        int branches = 0;
        Chain branch = new Chain(chains.get(i));
        for (int c = -1; c <= 1; c++) {
            for (int d = -1; d <= 1; d++) {
                if (c*d == 1 || c*c+d*d == 0) continue;
                if (!branch.contains(a + c, b + d)
                        && a+c >= 0 && a+c < s && b+d >= 0 && b+d < s) {
                    if (!fs[a+c][b+d].equals(clr)) continue;
                    if (clr.equals(p1) && b+d == 0) continue;
                    else if (clr.equals(p2) && a+c == 0) continue;
                    if (branches > 0) {
                        Chain sub = new Chain(branch);
                        sub.addCell(new Point(a + c, b + d));
                        chains.add(sub);
                        if ((clr.equals(p1) && b+d != s-1) || (clr.equals(p2) && a+c != s-1))
                            getAllChains(chains, a+c, b+d, clr, chains.size()-1);
                    } else {
                        chains.get(i).addCell(new Point(a+c, b+d));
                        branches++;
                        if ((clr.equals(p1) && b+d != s-1) || (clr.equals(p2) && a+c != s-1))
                            getAllChains(chains, a+c, b+d, clr, i);
                    }
                }
            }
        }
    }

    public void onClick(int x, int y) {
        if (isGameOver) return;
        for (int a = 0; a < s; a++) {
            for (int b = 0; b < s; b++) {
                Point p = fl[a][b];
                //is point inside of the hexagon?
                if (x >= p.x && x <= p.x+r*Math.sqrt(3) && y >= p.y && y <= p.y+2*r) {
                    double expr = 1/Math.sqrt(3)*Math.abs(2*x-(2*p.x+r*Math.sqrt(3)));
                    if ((y >= p.y+r/2 && y <= p.y+3*r/2) ||
                        expr+Math.abs(2*y-(2*p.y+r)) <= r ||
                        expr+Math.abs(2*y-(2*p.y+3*r)) <= r) {

                        if (fs[a][b] != Color.gray) break;
                        fs[a][b] = first ? p1 : p2;
                        checkWin(p1);
                        checkWin(p2);
                        first = !first;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        for (int a = 0; a < s; a++)
            for (int b = 0; b < s; b++) {
                g.setColor(fs[a][b]);
                int x = fl[a][b].x;
                int y = fl[a][b].y;
                //drawing hexagon
                g.fillPolygon(new int[]{
                        (int) (x + r * Math.sqrt(3) / 2), x, x, (int) (x + r * Math.sqrt(3) / 2), (int) (x + r * Math.sqrt(3)), (int) (x + r * Math.sqrt(3))}, new int[]{
                        y, y + r / 2, y + 3 * r / 2, y + 2 * r, y + 3 * r / 2, y + r / 2}, 6);
            }
    }
}
