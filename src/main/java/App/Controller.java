package App;

import Implementations.BoardImpl;
import Implementations.SafeField;
import Interfaces.Board;
import Interfaces.Cell;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static GlobalConstants.Constants.*;


public class Controller {
    private static Board board;
    private Cell[][] field;
    private boolean isLevelValid = false;
    private boolean isFirstInput = true;
    private boolean bombIsNotHit = true;
    private boolean gameInProgress = true;
    private int cellsToWin = 0;
    private int openCells = 0;
    private boolean isDifficultyValid = false;

    public Controller() {
        board = new BoardImpl();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(ENTER_DIFFICULTY_MESSAGE);

        // Cycles until a valid difficulty is given
        while (!isDifficultyValid) {
            String difficultyInput = scanner.nextLine();

            if (difficultyInput.trim().isEmpty() || difficultyInput.length() > 1) {
                System.out.println(INVALID_DIFFICULTY_MESSAGE);
            } else {
                int difficulty = Integer.parseInt(difficultyInput);
                switch (difficulty) {
                    case 0:
                        init(9);
                        break;
                    case 1:
                        init(16);
                        break;
                    case 2:
                        init(24);
                        break;
                    default:
                        System.out.println(INVALID_LEVEL_OF_DIFFICULTY_MESSAGE);
                }
            }
            //Game starts only if setup is valid
            if (isLevelValid) {
                //Cycles until a bomb is hit or game is won
                while (bombIsNotHit && gameInProgress) {
                    System.out.println(USER_INPUT_MESSAGE);
                    String userInput = scanner.nextLine();

                    //Checks if user input is number
                    String regex = "([0-9 ]+)";
                    Pattern pattern = Pattern.compile(regex);

                    Matcher matcher = pattern.matcher(userInput);

                    if (userInput.trim().isEmpty() || !matcher.matches()) {
                        System.out.println(INVALID_USER_INPUT_MESSAGE);
                        continue;
                    }

                    int[] input = Stream.of(userInput.split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    clickCell(input[0], input[1]);

                    //Checks if game is won
                    if (openCells >= cellsToWin) {
                        gameInProgress = false;
                    }

                }
                //Displays message depending on ending of game
                if (!bombIsNotHit) {
                    System.out.println(GAME_OVER_MESSAGE);
                    displayAllBombs();
                } else {
                    System.out.println(GAME_WON_MESSAGE);
                }
            }
        }
    }

    //Initializes playing field and sets up game parameters
    private void init(int size) {
        field = board.create(size);
        if (size == 9) {
            cellsToWin = (9 * 9) - 10;
        } else if (size == 16) {
            cellsToWin = (16 * 16) - 40;
        } else if (size == 24) {
            cellsToWin = (24 * 24) - 99;
        }

        isLevelValid = true;
        isDifficultyValid = true;
    }

    //Action on chosen cell
    private void clickCell(int row, int col) {
        if (checkValidUserInput(row, col)) {
            //Makes every first move safe
            if (isFirstInput) {
                field[row][col] = new SafeField();
                isFirstInput = false;
            }
            //Checks whether chosen cell is already clicked
            if (field[row][col].isClicked()) {
                System.out.println(CELL_ALREADY_CHOSEN_MESSAGE);
            } else {
                if (field[row][col].isBomb()) {
                    field[row][col].click();
                    bombIsNotHit = false;
                } else {
                    //Recursively shows all safe fields in area
                    findAllSafePositions(row, col);
                    displayBoard();
                }
            }
        } else {
            System.out.println(INVALID_USER_INPUT_MESSAGE);
        }
    }

    private boolean checkValidUserInput(int row, int col) {
        boolean isValid = true;
        if ((row < 0) || (row >= field.length)) {
            isValid = false;
        }
        if ((col < 0) || (col >= field.length)) {
            isValid = false;
        }
        return isValid;
    }

    private void displayBoard() {
        //Displays header info for board
        getHeaderInfo();
        //Prints out board data
        for (int i = 0; i < field.length; i++) {
            if (i <= 9) {
                System.out.print(i + "|   ");
            } else {
                System.out.print(i + "|  ");
            }
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j].isClicked()) {
                    System.out.print(field[i][j].getSymbol() + "    ");
                } else {
                    System.out.print("-    ");
                }
            }
            System.out.println();
        }
    }

    private void displayAllBombs() {
        getHeaderInfo();
        //Awkward looking empty spaces make output pleasing to the eye
        for (int i = 0; i < field.length; i++) {
            if (i <= 9) {
                System.out.print(i + "|   ");
            } else {
                System.out.print(i + "|  ");
            }
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j].isClicked() || field[i][j].isBomb()) {
                    System.out.print(field[i][j].getSymbol() + "    ");
                } else {
                    System.out.print("-    ");
                }
            }
            System.out.println();
        }
    }

    private void getHeaderInfo() {
        System.out.println(GAME_STATE);
        System.out.print("     ");
        for (int i = 0; i < field.length; i++) {
            if (i <= 9) {
                System.out.print(i + "    ");
            } else {
                System.out.print(i + "   ");
            }
        }
        System.out.println();
    }

    private void findAllSafePositions(int row, int col) {
        if ((row < 0) || (row >= field.length) || (col < 0) || (col >= field.length)) {
            return;
        }

        Cell chosenCell = field[row][col];

        if (chosenCell.isClicked()) {
            return;
        }


        if (chosenCell.isBomb()) {
            return;
        }

        chosenCell.click();
        openCells++;
        int bombs = countNearByBombs(row, col);

        if (bombs > 0) {
            if (chosenCell.isSafe()) {
                chosenCell.setSymbol(Character.forDigit(bombs, 10));
            }
            return;
        }


        findAllSafePositions(row + 1, col);
        findAllSafePositions(row + 1, col + 1);
        findAllSafePositions(row - 1, col);
        findAllSafePositions(row - 1, col - 1);
        findAllSafePositions(row + 1, col - 1);
        findAllSafePositions(row - 1, col + 1);
        findAllSafePositions(row, col + 1);
        findAllSafePositions(row, col - 1);

    }

    //Gets information for all nearby bombs and return their count to be used as a symbol
    private int countNearByBombs(int row, int col) {
        int countOfBombs = 0;

        if ((row + 1) < field.length) {
            if (field[row + 1][col].isBomb()) {
                countOfBombs++;
            }
        }

        if ((row + 1 < field.length) && (col + 1 < field.length)) {
            if (field[row + 1][col + 1].isBomb()) {
                countOfBombs++;
            }
        }

        if ((row - 1) >= 0) {
            if (field[row - 1][col].isBomb()) {
                countOfBombs++;
            }
        }

        if ((row - 1 >= 0) && (col - 1 >= 0)) {
            if (field[row - 1][col - 1].isBomb()) {
                countOfBombs++;
            }
        }

        if ((row + 1 < field.length) && (col - 1 >= 0)) {
            if (field[row + 1][col - 1].isBomb()) {
                countOfBombs++;
            }
        }

        if ((row - 1 >= 0) && (col + 1 < field.length)) {
            if (field[row - 1][col + 1].isBomb()) {
                countOfBombs++;
            }
        }

        if ((col + 1) < field.length) {
            if (field[row][col + 1].isBomb()) {
                countOfBombs++;
            }
        }

        if ((col - 1) >= 0) {
            if (field[row][col - 1].isBomb()) {
                countOfBombs++;
            }
        }
        return countOfBombs;
    }

}
