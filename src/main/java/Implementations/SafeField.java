package Implementations;

import Interfaces.Cell;

public class SafeField implements Cell {
    private boolean isClicked = false;
    //Sets default symbol, used for spaces with no bombs near them
    private char symbol = '#';

    public boolean isSafe() {
        return true;
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
    public boolean isBomb() {
        return false;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public void setSymbol(char sym) {
        this.symbol = sym;
    }

}
