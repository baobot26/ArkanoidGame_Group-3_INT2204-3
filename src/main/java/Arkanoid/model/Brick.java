package Arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A single brick in the playfield with type-specific behavior.
 * Hard bricks require multiple hits; unbreakable bricks cannot be destroyed.
 */
public class Brick extends GameObject {
    private BrickType type;
    private int hitsRemaining;
    private Color color;
    private boolean destroyed;
    
    public Brick(double x, double y, double width, double height, BrickType type, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.color = color;
        this.hitsRemaining = type.getHits();
        this.destroyed = false;
    }
    
    /** Bricks are static; no-op update. */
    @Override
    public void update() {
        // Bricks don't move
    }
    
    /** Bricks are static; no-op update. */
    @Override
    public void update(double deltaTime) {
        // Bricks don't move
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (destroyed) return;
        
        // Draw brick with gradient
        gc.setFill(color);
        gc.fillRoundRect(x + 1, y + 1, width - 2, height - 2, 5, 5);
        
        // Add highlight for 3D effect
        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        gc.fillRoundRect(x + 1, y + 1, width - 2, height / 2, 5, 5);
        
        // Draw border
        gc.setStroke(Color.rgb(0, 0, 0, 0.5));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + 1, y + 1, width - 2, height - 2, 5, 5);
        
        // Show hits remaining for hard bricks
        if (type == BrickType.HARD && hitsRemaining > 0) {
            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font(12));
            String text = String.valueOf(hitsRemaining);
            gc.fillText(text, x + width / 2 - 4, y + height / 2 + 4);
        }
        
        // Unbreakable brick indicator
        if (type == BrickType.UNBREAKABLE) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(2);
            gc.strokeLine(x + 5, y + height / 2, x + width - 5, y + height / 2);
        }
    }
    
    /**
     * Applies a hit to this brick.
     * @return true if the brick was destroyed by this hit
     */
    public boolean hit() {
        if (type == BrickType.UNBREAKABLE) {
            return false;
        }
        
        hitsRemaining--;
        
        if (hitsRemaining <= 0) {
            destroyed = true;
            return true;
        }
        
        // Darken color for hard bricks
        if (type == BrickType.HARD) {
            color = color.darker();
        }
        
        return false;
    }
    
    /** @return true if this brick has been destroyed. */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /** @return the brick type controlling durability and score. */
    public BrickType getType() {
        return type;
    }
    
    /** @return score awarded when this brick is destroyed. */
    public int getScore() {
        switch (type) {
            case HARD:
                return 20;
            case UNBREAKABLE:
                return 0;
            default:
                return 10;
        }
    }
}