import java.awt.Color;

/** Hawks are fast, long-sighted hunters, but they soar every fourth tick instead of acting. */
public class Hawk extends Predator {
    private int restCounter = 0;

    public Hawk(int x, int y) {
        super(x, y, 150, 3, 5, MovementStrategy.BISHOP);
    }

    @Override
    protected Animal newOffspring(int x, int y) { return new Hawk(x, y); }

    @Override
    protected void act(World world) {
        if (++restCounter % 4 == 0) return;
        super.act(world);
    }


    @Override
    public String getLabel() { return "H"; }

    @Override
    public Color getColor() { return Color.RED; }
}
