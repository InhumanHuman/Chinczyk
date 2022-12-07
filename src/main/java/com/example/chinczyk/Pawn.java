package com.example.chinczyk;

public class Pawn {
    private String color;
    private int fieldNumber;
    private String name;
    private boolean inGame;
    private boolean isFinished;
    private Field field;

    public Pawn(String color, String name, Field field) {
        this.color = color;
        this.fieldNumber = 99;
        this.name = name;
        this.field = field;
        this.isFinished = false;
        this.inGame = false;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
