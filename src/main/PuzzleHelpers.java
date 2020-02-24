package main;

/**
 * Helper class used for the puzzles..
 *
 * @author Matthew Lillie
 */
public class PuzzleHelpers {

    /**
     * Determines if a number is valid for a given position on a given board.
     *
     * @param board    The board to check
     * @param currentX The current row
     * @param currentY The current col
     * @param number   The number to check
     * @return True if it is a valid number... (does not interfere in rows, columns or same square) otherwise False
     */
    public static boolean checkValid(PuzzleNode[][] board, int currentX, int currentY, int number) {
        if (number == 0) {
            return true;
        }
        // Check row
        for (int x = 0; x < Puzzle.NUMBER_OF_SQUARES; x++) {
            if (board[x][currentY].getValue() == number) {
                return false;
            }
        }

        // Check column
        for (int y = 0; y < Puzzle.NUMBER_OF_SQUARES; y++) {
            if (board[currentX][y].getValue() == number) {
                return false;
            }
        }

        // Check current square
        int squareX = (int) (Math.floor(currentX / Puzzle.SQUARE_WIDTH) * Puzzle.SQUARE_WIDTH);
        int squareY = (int) (Math.floor(currentY / Puzzle.SQUARE_HEIGHT) * Puzzle.SQUARE_HEIGHT);

        for (int i = 0; i < Puzzle.SQUARE_WIDTH; i++) {
            for (int j = 0; j < Puzzle.SQUARE_HEIGHT; j++) {
                if (currentX == squareX + i && currentY == squareY) continue;

                if (board[squareX + i][squareY + j].getValue() == number) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if a board is finished
     *
     * @param board The given board to check.
     * @return True if the board is filled otherwise False
     */
    public static boolean isFinished(PuzzleNode[][] board) {
        for (int x = 0; x < Puzzle.NUMBER_OF_SQUARES; x++) {
            for (int y = 0; y < Puzzle.NUMBER_OF_SQUARES; y++) {
                if (board[x][y].getValue() == 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
