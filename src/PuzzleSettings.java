import javax.swing.*;

/**
 * Panel that represents the settings for the Sudoku Puzzle. Includes solving the puzzle and re-randomizing it.
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
                    if (puzzle.solve()) {
                        System.out.println("Finished, null thread");
                    } else {
                        System.out.println("No solution found.");
                        JOptionPane.showMessageDialog(puzzle, "No solution found!", "Solution", JOptionPane.ERROR_MESSAGE);
                    }
                    solvingThread = null;
                });
                solvingThread.start();
            }
        });

        newButton.addActionListener((actionEvent -> {
            if (puzzle.finished() || solvingThread == null) {
                puzzle.generateRandomBoard();
                puzzle.repaint();
            }
        }));

        this.add(solveButton);
        this.add(newButton);
    }
}
