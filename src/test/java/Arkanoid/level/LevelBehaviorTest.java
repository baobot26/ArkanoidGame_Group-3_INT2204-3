package Arkanoid.level;

import Arkanoid.model.Brick;
import Arkanoid.model.BrickType;
import Arkanoid.util.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelBehaviorTest {

    private LevelData makeSampleLevelData() {
        LevelData ld = new LevelData();
        ld.setLevelNumber(99);
        ld.setName("JUnit Level");
        ld.setBallSpeed(5.5);
        ld.setLives(4);

        List<LevelData.BrickData> bricks = new ArrayList<>();
        bricks.add(new LevelData.BrickData(0, 0, "NORMAL", "#FF0000"));
        bricks.add(new LevelData.BrickData(0, 1, "HARD", "#8B0000"));
        bricks.add(new LevelData.BrickData(0, 2, "UNBREAKABLE", "#808080"));
        ld.setBricks(bricks);

        return ld;
    }

    @Test
    void testInitializeFromData_countsScoringAndCompletion() {
        Level level = new Level(makeSampleLevelData());
        level.initialize();

        // 3 bricks loaded from our test data
        assertEquals(3, level.getBricks().size());

        // UNBREAKABLE doesn't count -> 2 remaining
        assertEquals(2, level.getRemainingBricks());

        // Score math: NORMAL: 10, HARD: 20, UNBREAKABLE: 0 -> 30
        assertEquals(30, level.getMaxScore());

        // Smash all breakable bricks and check finish condition
        for (Brick b : level.getBricks()) {
            if (b.getType() == BrickType.UNBREAKABLE) continue;
            for (int i = 0; i < 6 && !b.isDestroyed(); i++) {
                b.hit();
            }
            assertTrue(b.isDestroyed(), "Breakable brick should be destroyed after hits");
        }
        assertEquals(0, level.getRemainingBricks());
        assertTrue(level.isCompleted());
    }

    @Test
    void testResetRestoresInitialLayoutAndFreshInstances() {
        Level level = new Level(makeSampleLevelData());
        level.initialize();

        List<Brick> before = new ArrayList<>(level.getBricks());

        // Mess with state (break some bricks)
        for (Brick b : before) {
            if (b.getType() != BrickType.UNBREAKABLE) {
                for (int i = 0; i < 6 && !b.isDestroyed(); i++) b.hit();
            }
        }
        assertTrue(before.stream().anyMatch(Brick::isDestroyed), "At least one brick should be destroyed");

        // Reset back to the starting layout
        level.reset();

        List<Brick> after = level.getBricks();
        // Same layout size as before
        assertEquals(before.size(), after.size());

        // Fresh objects (no reusing old bricks)
        for (Brick nb : after) {
            for (Brick ob : before) {
                assertNotSame(ob, nb, "Bricks should be recreated on reset");
            }
        }

        // Remaining count restored (ignores UNBREAKABLE) -> 2
        assertEquals(2, level.getRemainingBricks());
        assertFalse(level.isCompleted());
    }

    @Test
    void testDefaultLevelFallback_whenNoBrickData() {
        LevelData ld = new LevelData();
        ld.setLevelNumber(1);
        ld.setName("Default Grid");
        ld.setBricks(null); // no data -> fall back to default grid
        ld.setBallSpeed(4.5);
        ld.setLives(3);

        Level level = new Level(ld);
        level.initialize();

        int expected = Constants.BRICK_ROWS * Constants.BRICK_COLS;
        assertEquals(expected, level.getBricks().size());
        assertEquals(expected, level.getRemainingBricks());
        // Default grid = all NORMAL bricks -> 10 points each
        assertEquals(expected * 10, level.getMaxScore());
        assertFalse(level.isCompleted());
    }

    @Test
    void testLevelConfigAccessors() {
        Level level = new Level(makeSampleLevelData());
        level.initialize();

        assertEquals(5.5, level.getBallSpeed(), 1e-9);
        assertEquals(4, level.getInitialLives());
        assertEquals(99, level.getLevelNumber());
        assertEquals("JUnit Level", level.getLevelName());
    }
}
