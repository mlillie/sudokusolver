package main.solvers;

import main.Puzzle;
import main.PuzzleHelpers;
import main.PuzzleNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Finds all possible solutions to a given suduko puzzle using the Java ForkJoinPool and RecursiveTask libraries.
 *
 * @author Matthew Lillie
 */
public class ForkPoolAllSolver implements Solver {

    @Override
    public boolean solve(Puzzle puzzle) {
        List<PuzzleNode[][]> allResults = new ForkJoinPool().invoke(new ForkSolver(puzzle, puzzle.getCurrentBoard().clone()));

        if (allResults.isEmpty()) {
            System.out.println("No solution?");
            return false;
        } else {
            System.out.println("Found: " + allResults.size() + " solutions!");
            return true;
        }
    }


    private static class ForkSolver extends RecursiveTask<List<PuzzleNode[][]>> {

        private Puzzle puzzle;
        private PuzzleNode[][] board;

        ForkSolver(Puzzle puzzle, PuzzleNode[][] board) {
            this.puzzle = puzzle;
            this.board = board;
        }

        @Override
        protected List<PuzzleNode[][]> compute() {
            if (PuzzleHelpers.isFinished(board)) {
                return Arrays.asList(new PuzzleNode[][][]{board});
            }

            /*
             * Loop through and create all possible solvers for any found 0 value
             */
            List<ForkSolver> solvers = new ArrayList<>();
            start:
            for (int x = 0; x < Puzzle.NUMBER_OF_SQUARES; x++) {
                for (int y = 0; y < Puzzle.NUMBER_OF_SQUARES; y++) {
                    if (board[x][y].getValue() == 0) {
                        for (int n = 1; n <= Puzzle.NUMBER_OF_SQUARES; n++) {
                            if (PuzzleHelpers.checkValid(board, x, y, n)) {
                                board[x][y].setValue(n);
                                board[x][y].setColor(Color.RED);
                                PuzzleNode[][] nextBoard = new PuzzleNode[Puzzle.NUMBER_OF_SQUARES][Puzzle.NUMBER_OF_SQUARES];
                                for (int i = 0; i < Puzzle.NUMBER_OF_SQUARES; i++) {
                                    for (int j = 0; j < Puzzle.NUMBER_OF_SQUARES; j++) {
                                        nextBoard[i][j] = (PuzzleNode) board[i][j].clone();
                                    }
                                }
                                ForkSolver nextSolver = new ForkSolver(puzzle, nextBoard);
                                solvers.add(nextSolver);
                                board[x][y].setValue(0);
                                board[x][y].setColor(Color.BLACK);
                            }
                        }
                        break start;
                    }
                }
            }

            // Reset and check if empty
            board = null;
            if (solvers.isEmpty()) {
                return null;
            }

            // Invoke all the solvers and return all solutions that have been found...
            invokeAll(solvers);

            List<PuzzleNode[][]> allBoards = new ArrayList<>();
            for (ForkSolver solver : solvers) {
                List<PuzzleNode[][]> boards = solver.join();

                if (boards == null || boards.isEmpty()) {
                    continue;
                }

                if (!allBoards.containsAll(boards)) {

                    for (PuzzleNode[][] board : boards) {
                        puzzle.setCurrentBoard(board);
                        SwingUtilities.invokeLater(puzzle::repaint);
                    }
                    allBoards.addAll(boards);
                }
            }

            return allBoards;
        }
    }
}
