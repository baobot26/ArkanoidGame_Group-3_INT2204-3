package Arkanoid.model;

public enum BrickType {
    NORMAL(1),      // 1 hit
    HARD(2),        // 2 hits
    UNBREAKABLE(3); // no hit

    private final int hits;

    BrickType(int hits) {
        this.hits = hits;
    }

    public int getHits() {
        return hits;
    }
}