package Implementations;

import Interfaces.Cell;

public class Bomb implements Cell {
    private boolean isClicked = false;
    //Sets default symbol for bombs
    private char symbol = '*';

    @Override
    public boolean isBomb() {
        return true;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public void click() {
        isClicked = true;
    }

    @Override
    public boolean isClicked() {
        return isClicked;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    //Ensures access to symbol if change is to occur
    @Override
    public void setSymbol(char sym) {
        symbol = sym;
    }


}
