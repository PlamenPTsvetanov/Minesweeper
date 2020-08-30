package Implementations;

import Interfaces.Cell;
import Interfaces.Board;

import java.util.Random;

public class BoardImpl implements Board {
    private int bombsCreated = 0;

    private int bombCountLimit;

    public Cell[][] create(int size) {
        getBombCountLimit(size);

        Cell[][] field = new Cell[size][size];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                Cell cell = setFieldAtRandom();
                field[i][j] = cell;

                if (cell.isBomb()) {
                    bombsCreated++;
                }
            }
        }
        return field;
    }

    //Sets bomb limit
    private void getBombCountLimit(int size) {
        if (size == 9) {
            bombCountLimit = 10;
        } else if (size == 16) {
            bombCountLimit = 40;
        } else if (size == 24) {
            bombCountLimit = 99;
        }
    }

    //Sets cells status - bomb or safe field
    private Cell setFieldAtRandom() {
        Cell cell;
        Random random = new Random();

        int decider = random.nextInt(2);

        if (decider == 0 && bombsCreated < bombCountLimit) {
            cell = new Bomb();
        } else {
            cell = new SafeField();
        }
        return cell;
    }
}
