package com.example.chinczyk;

public class Field {
    private int x, y;
    public final int RED_START_POINT = 0;
    public final int GREEN_START_POINT = 10;
    public final int BLUE_START_POINT = 20;
    public final int YELLOW_START_POINT = 30;
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "Field{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
