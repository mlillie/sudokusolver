package main.solvers;

import main.Puzzle;
import main.PuzzleHelpers;

import javax.swing.*;
import java.awt.*;

/**
 * Solves a sudoku puzzle using a standard sequential backtracking algorithm.
 *
 * @author Matthew Lillie
 */
public class SequentialBacktrackingSolver implements Solver {

    @Override
    public boolean solve(Puzzle puzzle) {
        if (backtracking(puzzle)) {
            System.out.println("Successfully found solution using sequential backtracking!");
            return true;
        } else {
            System.out.println("Failed to find a solution using sequential backtracking!");
            return false;
        }
    }

    private boolean backtracking(Puzzle puzzle) {
        // If every square is filled, then we finished!
        if (PuzzleHelpers.isFinished(puzzle.getCurrentBoard())) {
            return true;
        }

        for (int x = 0; x < Puzzle.NUMBER_OF_SQUARES; x++) {
            for (int y = 0; y < Puzzle.NUMBER_OF_SQUARES; y++) {
                if (puzzle.getCurrentBoard()[x][y].getValue() == 0) {
                    for (int n = 1; n <= Puzzle.NUMBER_OF_SQUARES; n++) {
                        if (PuzzleHelpers.checkValid(puzzle.getCurrentBoard(), x, y, n)) {
                            puzzle.getCurrentBoard()[x][y].setValue(n);
                            puzzle.getCurrentBoard()[x][y].setColor(Color.RED);
                            SwingUtilities.invokeLater(puzzle::repaint);
                            if (backtracking(puzzle)) {
                                return true;
                            } else {
                                puzzle.getCurrentBoard()[x][y].setValue(0);
                                puzzle.getCurrentBoard()[x][y].setColor(Color.BLACK);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
