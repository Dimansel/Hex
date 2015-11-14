package org.dimansel.hex;

import java.awt.*;
import java.util.ArrayList;

public class Chain {
    private ArrayList<Point> cells;

    public Chain(Point... pts) {
        cells = new ArrayList<>();
        for (Point p : pts)
            cells.add(p);
    }

    public Chain(Chain chain) {
        cells = new ArrayList<>();
        for (Point p : chain.cells)
            cells.add(new Point(p.x, p.y));
    }

    public void addCell(Point cell) {
        cells.add(cell);
    }

    public boolean isFull(boolean horizontal, int size) {
        if (cells.size() < size) return false;
        for (Point p : cells) {
            if ((p.y == size - 1 && horizontal) || (p.x == size - 1 && !horizontal))
                return true;
        }
        return false;
    }

    public boolean contains(int a, int b) {
        return cells.contains(new Point(a, b));
    }

    public int getLength() {
        return cells.size();
    }

    public void lightUp(Color[][] colors, Color color) {
        for (Point p : cells)
            colors[p.x][p.y] = new Color(3*color.getRGB()/2);
    }

    @Override
    public String toString() {
        String s = "";
        for (Point p : cells)
            s += "["+p.x+"]["+p.y+"] ";
        return s;
    }
}
