package main.solvers;

import main.Puzzle;
import main.PuzzleHelpers;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

/**
 * Solves a sudoku puzzle using Java's thread pool executor service.
 * Will submit new threads to a thread pool in order to slightly speed up the process of solving the puzzle.
 *
 * @author Matthew Lillie
 */
public class ParallelBacktrackingSolver implements Solver {

    /**
     * Number of threads to use for sorting.
     */
    private final int numberThreads;

    /**
     * Thread pool used for executing sorting Runnables.
     */
    private final ExecutorService threadPool;

    /**
     * Construct a new solver using a given number of threads.
     *
     * @param numberThreads The number of thread pools to have available.
     */
    public ParallelBacktrackingSolver(int numberThreads) {
        this.numberThreads = numberThreads;
        this.threadPool = Executors.newFixedThreadPool(numberThreads);
    }

    /**
     * Construct a new solver using however many processors are available.
     */
    public ParallelBacktrackingSolver() {
        this.numberThreads = Runtime.getRuntime().availableProcessors();
        this.threadPool = Executors.newFixedThreadPool(numberThreads);
    }


    @Override
    public boolean solve(Puzzle puzzle) {
        try {
            MTSolver solver = new MTSolver(puzzle);
            Future<Boolean> completed = threadPool.submit(solver);
            if (completed.get()) {
                System.out.println("Successfully found solution using multi-threaded backtracking!");
                return true;
            } else {
                System.out.println("Failed to find a solution using multi-threaded backtracking!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Callable class that takes in the puzzle we are attempting to solve.
     */
    private class MTSolver implements Callable<Boolean> {

        private final Puzzle puzzle;

        MTSolver(Puzzle puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public Boolean call() throws Exception {
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

                                // Check if the pool is full and continue sequentially, otherwise submit a new thread to the solver.
                                // This will increase speed so we do not have to wait for a thread to be open.
                                if (isPoolFull()) {
                                    if (call()) {
                                        return true;
                                    } else {
                                        puzzle.getCurrentBoard()[x][y].setValue(0);
                                        puzzle.getCurrentBoard()[x][y].setColor(Color.BLACK);
                                    }
                                } else {
                                    MTSolver nextSolver = new MTSolver(puzzle);
                                    Future<Boolean> finished = threadPool.submit(nextSolver);
                                    if (finished.get()) {
                                        return true;
                                    } else {
                                        puzzle.getCurrentBoard()[x][y].setValue(0);
                                        puzzle.getCurrentBoard()[x][y].setColor(Color.BLACK);
                                    }
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

    /**
     * Determines if the pools are all currently being used.
     *
     * @return True if all the pools are full
     */
    private synchronized boolean isPoolFull() {
        ThreadPoolExecutor e = (ThreadPoolExecutor) threadPool;
        return e.getActiveCount() == e.getMaximumPoolSize();
    }
}
