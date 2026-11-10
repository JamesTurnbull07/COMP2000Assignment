/**
 * Strategy pattern
 * codes how an animal covers a (dx, dy) gap in one tick.
 * Each Animal holds one movementstrategy.
 */
@FunctionalInterface
public interface MovementStrategy {
    int[] step(int dx, int dy, int speed);

    /** King can move in any of the 8 directions. this is the default movement for prey. */
    MovementStrategy KING = (dx, dy, speed) ->
        new int[]{ clamp(dx, speed), clamp(dy, speed) };

    /** Rook moves on one axis at a time. used by Fox. */
    MovementStrategy ROOK = (dx, dy, speed) ->
        Math.abs(dx) >= Math.abs(dy)
            ? new int[]{ clamp(dx, speed), 0 }
            : new int[]{ 0, clamp(dy, speed) };

    /** Bishop moves on diagonals. used by Hawk. */
    MovementStrategy BISHOP = (dx, dy, speed) -> {
        int sx = (dx == 0) ? 1 : Integer.signum(dx);
        int sy = (dy == 0) ? 1 : Integer.signum(dy);
        int m = Math.min(speed, Math.max(1, Math.max(Math.abs(dx), Math.abs(dy))));
        return new int[]{ sx * m, sy * m };
    };

    static int clamp(int delta, int speed) {
        return Math.max(-speed, Math.min(speed, delta));
    }
}