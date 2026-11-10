import java.awt.Color;

/**
 * Foxes are slower and shorter-sighted than hawks, and can only run down one
 * axis at a time.
 */
public class Fox extends Predator {
    public Fox(int x, int y) {
        super(x, y, 150, 2, 3, MovementStrategy.ROOK);
    }

    @Override
    protected Animal newOffspring(int x, int y) {
        return new Fox(x, y);
    }

    @Override
    public String getLabel() {
        return "F";
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}
