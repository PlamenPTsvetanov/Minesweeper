package Interfaces;

public interface Cell {

    void click();

    boolean isClicked();

    boolean isBomb();

    boolean isSafe();

    char getSymbol();

    void setSymbol(char symbol);

}
