package main;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sudoku Solver");
            Puzzle puzzle = new Puzzle();
            PuzzleSettings puzzleSettings = new PuzzleSettings(puzzle);
            frame.setSize((int) screenSize.getWidth() / 2, (int) ((int) screenSize.getHeight() / 1.5));
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(puzzle, BorderLayout.CENTER);
            frame.add(puzzleSettings, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }


}
