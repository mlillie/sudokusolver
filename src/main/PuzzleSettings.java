package main;

import main.solvers.ForkPoolAllSolver;
import main.solvers.ParallelBacktrackingSolver;
import main.solvers.SequentialBacktrackingSolver;
import main.solvers.Solver;

import javax.swing.*;

/**
 * Panel that represents the settings for the Sudoku main.Puzzle. Includes solving the puzzle and re-randomizing it.
 *
 * @author Matthew Lillie
 */
public class PuzzleSettings extends JPanel {

    // The thread that is solving the puzzle; separate from the main Swing Thread.
    private Thread solvingThread;

    /**
     * Construct a new Settings JPanel with the puzzle it is using.
     *
     * @param puzzle The puzzle these settings are for.
     */
    PuzzleSettings(Puzzle puzzle) {
        // Create buttons and add listeners for handling
        JButton solveButton = new JButton("Solve Puzzle");
        JButton newButton = new JButton("New Puzzle");
        JButton stopButton = new JButton("Stop Solving");
        solveButton.addActionListener((actionEvent) -> {
            if (solvingThread == null) {
                solvingThread = new Thread(() -> {
                    final PuzzleNode[][] currentBoard = puzzle.getCurrentBoard();
                    Solver solver = new ParallelBacktrackingSolver();

                    long start = System.currentTimeMillis();
                    solver.solve(puzzle);
                    System.out.println("MT Found in " + (System.currentTimeMillis() - start) + "ms");

                    puzzle.setCurrentBoard(currentBoard);
                    puzzle.repaint();

                    solver = new SequentialBacktrackingSolver();
                    start = System.currentTimeMillis();
                    solver.solve(puzzle);
                    System.out.println("S Found in " + (System.currentTimeMillis() - start) + "ms");

                    puzzle.setCurrentBoard(currentBoard);
                    puzzle.repaint();

                    solver = new ForkPoolAllSolver();
                    start = System.currentTimeMillis();
                    solver.solve(puzzle);
                    System.out.println("F Found in " + (System.currentTimeMillis() - start) + "ms");
                    solvingThread = null;
                });
                solvingThread.start();
            }

        });

        newButton.addActionListener((actionEvent -> {
            if (PuzzleHelpers.isFinished(puzzle.getCurrentBoard()) || solvingThread == null) {
                puzzle.generateRandomBoard();
                puzzle.repaint();
            }
        }));

        this.add(solveButton);
        this.add(newButton);
    }
}
