package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a Sudoku puzzle that is being drawn onto a JPanel, includes a method to be able to generate a random puzzle as well as solving the
 * puzzle using backtracking.
 *
 * @author Matthew Lillie
 */
public class Puzzle extends JPanel {

    // Constants and variables
    public static final int NUMBER_OF_SQUARES = 9;
    public static final int SQUARE_WIDTH = 3;
    public static final int SQUARE_HEIGHT = 3;
    private BufferedImage puzzleImage;
    private PuzzleNode[][] currentBoard;

    /**
     * Construct a new main.Puzzle JPanel
     */
    Puzzle() {
        generateRandomBoard();
        try {
            this.puzzleImage = ImageIO.read(getClass().getResource("/images/sudoku-blankgrid.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selectBox(mouseEvent.getX(), mouseEvent.getY());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

    }

    /**
     * Generates a random Sudoku puzzle.
     */
    public void generateRandomBoard() {
        // Setup nodes
        this.currentBoard = new PuzzleNode[NUMBER_OF_SQUARES][NUMBER_OF_SQUARES];
        for (int x = 0; x < NUMBER_OF_SQUARES; x++) {
            for (int y = 0; y < NUMBER_OF_SQUARES; y++) {
                currentBoard[x][y] = new PuzzleNode(0);
            }
        }

        // Generate numbers..
        for (int x = 0; x < NUMBER_OF_SQUARES; x++) {
            for (int y = 0; y < NUMBER_OF_SQUARES; y++) {
                // 75% chance that a number will be placed at a given empty position
                if (Math.random() < .75) {
                    continue;
                }
                // Make sure the number validated is valid.
                int number = (int) (Math.random() * NUMBER_OF_SQUARES + 1);
                while (!PuzzleHelpers.checkValid(currentBoard, x, y, number)) {
                    number = (int) (Math.random() * NUMBER_OF_SQUARES + 1);
                }
                currentBoard[x][y].setValue(number);
            }
        }
    }


    /**
     * This shows an input dialog to the selected box in order to change the value for that node.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     */
    private void selectBox(int mouseX, int mouseY) {
        int scaleX = getWidth() / NUMBER_OF_SQUARES;
        int scaleY = getHeight() / NUMBER_OF_SQUARES;

        int x = mouseX / scaleX;
        int y = mouseY / scaleY;


        // Ask for a number and properly parse...
        String s = JOptionPane.showInputDialog(null, "Enter number (0-9):",
                "Changing number for index: (" + x + "," + y + ")", JOptionPane.QUESTION_MESSAGE);
        if (s == null) {
            return;
        }
        int number = attemptParse(s);
        while (number < 0 || number > 9 || !PuzzleHelpers.checkValid(currentBoard, x, y, number)) {
            s = JOptionPane.showInputDialog(null,
                    "Try again... number (0-9):", "Changing number for index: (" + x + "," + y + ")", JOptionPane.QUESTION_MESSAGE);
            if (s == null) {
                return;
            } else {
                number = attemptParse(JOptionPane.showInputDialog(null,
                        "Try again... number (0-9):", "Changing number for index: (" + x + "," + y + ")", JOptionPane.QUESTION_MESSAGE));
            }
        }

        // Set the value and repaint
        currentBoard[x][y].setValue(number);
        repaint();
    }

    /**
     * Attemps to parse the String to an Integer
     *
     * @param s The string to parse
     * @return The integer value of the String otherwise 1.
     */
    private int attemptParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;
        // Enable anti aliasing and prefer quality rendering
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw the puzzle image.
        Image scaledImage = puzzleImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        graphics.drawImage(scaledImage, 0, 0, this);


        // Draw the individual portions
        int scaleX = getWidth() / NUMBER_OF_SQUARES;
        int scaleY = getHeight() / NUMBER_OF_SQUARES;
        for (int x = 0; x < NUMBER_OF_SQUARES; x++) {
            for (int y = 0; y < NUMBER_OF_SQUARES; y++) {
                if (currentBoard[x][y].getValue() == 0) {
                    continue;
                }
                int realX = (x * scaleX + getWidth() / NUMBER_OF_SQUARES / SQUARE_WIDTH + 8) % getWidth();
                int realY = (y * scaleY + getHeight() / NUMBER_OF_SQUARES / SQUARE_HEIGHT + 15) % getHeight();
                int value = currentBoard[x][y].getValue();
                graphics.setColor(currentBoard[x][y].getColor());
                graphics.setFont(new Font("TimesRoman", Font.BOLD, 20));
                graphics.drawString(String.valueOf(value), realX, realY);
            }
        }
    }


    public PuzzleNode[][] getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(PuzzleNode[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puzzle puzzle = (Puzzle) o;
        return Arrays.equals(currentBoard, puzzle.currentBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(currentBoard);
    }

}
