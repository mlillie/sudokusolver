import java.awt.*;

class PuzzleNode {
    int value;
    Color color;

    PuzzleNode(int value) {
        this.value = value;
        this.color = Color.BLACK;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}