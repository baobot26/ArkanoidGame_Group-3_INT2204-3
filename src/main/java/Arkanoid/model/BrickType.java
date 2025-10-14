package Arkanoid.model;

public enum BrickType {
    NORMAL(1),
    HARD(2),
    UNBREAKABLE(3);

    private final int hits;

    BrickType(int hits) {
        this.hits = hits;
    }

    public int getHits() {
        return hits;
    }
}