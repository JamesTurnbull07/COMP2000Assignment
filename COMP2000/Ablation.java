import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ablation {
    record Trial(String label, String... args) {}

    static String run(Trial t) {
        String label = t.label();
        String[] args = t.args();
        SimulationConfig c;
        try {
            c = SimulationConfig.fromArgs(args);
        } catch (SimulationConfigException e) {
            throw new IllegalArgumentException("Bad config for " + label, e);
        }
        World w = new World(c);
        java.util.Random r = w.getRandom();
        for (int i=0;i<c.getHawks();i++)   w.addHawk(new Hawk(r.nextInt(c.getCols()), r.nextInt(c.getRows())));
        for (int i=0;i<c.getFoxes();i++)   w.addFox(new Fox(r.nextInt(c.getCols()), r.nextInt(c.getRows())));
        for (int i=0;i<c.getRabbits();i++) w.addRabbit(new Rabbit(r.nextInt(c.getCols()), r.nextInt(c.getRows())));
        for (int i=0;i<c.getMice();i++)    w.addMouse(new Mouse(r.nextInt(c.getCols()), r.nextInt(c.getRows())));
        for (int i=0;i<c.getFood();i++)    w.addFood(new Food(r.nextInt(c.getCols()), r.nextInt(c.getRows())));
        int peak=0, troughAfterPeak=Integer.MAX_VALUE, rebounds=0, prev=c.getRabbits(); boolean falling=false;
        while (w.getState()==SimulationState.RUNNING && w.getTickCount()<3000) {
            w.update();
            int R = w.getRabbits().size() + w.getMice().size();
            peak = Math.max(peak, R);
            if (R < prev) falling = true;
            if (falling && R > prev + 4) { rebounds++; falling = false; }
            prev = R;
        }
        return String.format("%-26s survived %4d ticks  peakPrey=%-3d rebounds=%d  births=%d",
            label, w.getTickCount(), peak, rebounds, w.getBirths());
    }

    public static void main(String[] a) {
        List<String> results = Stream.of("918273645", "555000111", "777222333")
            .flatMap(seed -> Stream.of(
                new Trial("refuge ON  seed " + seed, "seed=" + seed, "safeZone=1"),
                new Trial("refuge OFF seed " + seed, "seed=" + seed, "safeZone=0")))
            .parallel()
            .map(Ablation::run)
            .collect(Collectors.toList());
        results.forEach(System.out::println);
    }
}