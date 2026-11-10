import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

public class SimPanel extends JPanel {
    /** Pixels per grid cell. */
    public static final int CELL_SIZE = 18;

    private final World world;

    public SimPanel(World world) {
        this.world = world;
        setBackground(new Color(30, 30, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Faint lattice so the discrete cell-by-cell movement is visible.
        g.setColor(new Color(48, 48, 48));
        for (int x = 0; x <= world.getWidth(); x++)
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, world.getHeight() * CELL_SIZE);
        for (int y = 0; y <= world.getHeight(); y++)
            g.drawLine(0, y * CELL_SIZE, world.getWidth() * CELL_SIZE, y * CELL_SIZE);

        // Safe zone: predators cannot enter this rectangle.
        if (world.hasSafeZone()) {
        g.setColor(new Color(80, 160, 255, 40));
        g.fillRect(world.getZoneX() * CELL_SIZE, world.getZoneY() * CELL_SIZE,
                   world.getZoneWidth() * CELL_SIZE, world.getZoneHeight() * CELL_SIZE);
        g.setColor(new Color(80, 160, 255));
        g.drawRect(world.getZoneX() * CELL_SIZE, world.getZoneY() * CELL_SIZE,
                   world.getZoneWidth() * CELL_SIZE, world.getZoneHeight() * CELL_SIZE);
        g.drawString("Safe zone", world.getZoneX() * CELL_SIZE + 6, world.getZoneY() * CELL_SIZE + 14);
        }

        for (Entity e : world.allEntities()) {
            if (e.isAlive()) {
                e.draw(g);
            }
        }
        drawCounts(g);
    }

    /** Counts sit on their own panel so they never hide an entity underneath. */
    private void drawCounts(Graphics g) {
        Map<String, DoubleSummaryStatistics> fed = world.fedLevelBySpecies();
        String[] rows = {
            String.format("H  Hawks    %-3d", world.getHawks().size()) + fedText(fed.get("Hawk")),
            String.format("F  Foxes    %-3d", world.getFoxes().size()) + fedText(fed.get("Fox")),
            String.format("R  Rabbits  %-3d", world.getRabbits().size()) + fedText(fed.get("Rabbit")),
            String.format("m  Mice     %-3d", world.getMice().size()) + fedText(fed.get("Mouse")),
            "*  Food     " + world.getFood().size(),
        };
        Color[] colours = { new Hawk(0,0).getColor(), new Fox(0,0).getColor(),
                            new Rabbit(0,0).getColor(), new Mouse(0,0).getColor(),
                            new Food(0,0).getColor() };
        int boxWidth = 236;
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(4, 4, boxWidth, 6 * 16 + 8);
        g.setColor(new Color(90, 90, 90));
        g.drawRect(4, 4, boxWidth, 6 * 16 + 8);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        g.setColor(Color.GRAY);
        g.drawString("            n    fed  (min)", 12, 20);
        for (int i = 0; i < rows.length; i++) {
            g.setColor(colours[i]);
            g.drawString(rows[i], 12, 36 + i * 16);
        }
    }

    private static String fedText(DoubleSummaryStatistics stats) {
        if (stats == null || stats.getCount() == 0) return "";
        return String.format("  %3.0f%%  (%2.0f%%)", stats.getAverage(), stats.getMin());
    }
}