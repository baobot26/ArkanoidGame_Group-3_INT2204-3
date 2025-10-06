import java.awt.event.KeyEvent;

/**
 * Paddle class represents the paddle in a game, extending the MoveableObject class.
 * It inherits properties and methods from MoveableObject to allow movement and interaction.
 */
public class Paddle extends MoveableObject{
    PowerUps currentPowerUp;
    @Override
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_SPEED = 10;
   public Paddle(double x, double y, double width, double height, double speed) {
       this.x = x;
       this.y = y;
         this.width = PADDLE_WIDTH;
            this.height = PADDLE_HEIGHT;
            this.speed = PADDLE_SPEED;
   }
   public void moveLeft() {
       this.x -= speed;
   }
   public void moveRight() {
         this.x += speed;
   }
    public void setCurrentPowerUp(PowerUps powerUp) {
        this.currentPowerUp = powerUp;
    }
    public PowerUps getCurrentPowerUp() {
        return currentPowerUp;
    }
    public void handleInput(KeyEvent e) {
       if (e.getKeyCode() == KeyEvent.VK_LEFT) {
           moveLeft();
       } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
           moveRight();
       }
    }
    @Override
    void move() {

    }

    @Override
    void update() {

    }

    @Override
    void render() {

    }
})
