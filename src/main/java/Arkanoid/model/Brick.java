package Arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GameObject {
    private BrickType type;
    private int hitsRemaining;
    private Color color;
    private boolean destroyed;

    /**
     * Brick constructor with 6 params.
     * @param x the x position
     * @param y the y position
     * @param width the width of brick
     * @param height the height of brick
     * @param type brick type
     * @param color brick color
     */
    public Brick(double x, double y, double width, double height, BrickType type, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.color = color;
<<<<<<< HEAD
=======
        this.hitsRemaining = type.getHits();
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
        this.destroyed = false;
    }

    /**
     * Update the brick status.
     */
    @Override
    public void update() {
<<<<<<< HEAD
        // bricks dont move
=======
        // Bricks do not move
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
    }

    /**
     * Update the brick status.
     * @param deltaTime time elapsed from last update
     */
    @Override
    public void update(double deltaTime) {
<<<<<<< HEAD
        // bricks dont move
=======
        // Bricks do not move
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
    }

    /**
     * Render the brick on the canvas.
     * @param gc the graphics context used to draw the brick on the canvas
     */
    @Override
    public void render(GraphicsContext gc) {
<<<<<<< HEAD

=======
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
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
    }

    /**
     * Method for the brick to take hit.
     * @return true if the brick is destroyed, false otherwise
     */
    public boolean hit() {
<<<<<<< HEAD
        // the get hit implement
        if (type.UNBREAKABLE == type) {
            return false;
        }

        
=======
        // Unbreakable Bricks can not be hit
        if (type == BrickType.UNBREAKABLE) {
            return false;
        }
        hitsRemaining--;
        if(hitsRemaining <= 0) {
            destroyed = true;
            return true;
        }
        // Darken color for hard bricks when taking hit
        if (type == BrickType.HARD) {
            color = color.darker();
        }
        return false;
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
    }

    /**
     * Getter of the destroyed status of the brick.
     * @return destroyed status of the brick
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Getter of brick type.
     * @return brick type
     */
    public BrickType getType() {
        return type;
    }

    /**
     * Get the score after destroy a brick.
     * @return score the score rewarded for the brick
     */
    public int getScore() {
<<<<<<< HEAD
        
=======
        switch (type) {
            case HARD:
                return 20;
            case UNBREAKABLE:
                return 0;
            default:
                return 10;
        }
        return 0;
>>>>>>> d16885b01ee24c6056ed2383060eb6c71ee3474a
    }
}
