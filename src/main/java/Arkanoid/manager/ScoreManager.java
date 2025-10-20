package Arkanoid.manager;

public class ScoreManager {
    private int score;
    private int highScore;
    private int level;
    private int lives;

    public ScoreManager() {
        reset();
    }

    public void addScore(int points) {
        score += points;
      
    public void loseLife() {
        lives--;
    }

    public void addLife() {
        lives++;
    }

    public void nextLevel() {
        level++;
    }

    public void reset() {
        highScore = Math.max(highScore,score);
        score = 0;
        lives = 3;
        level = 1;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    // Getters
    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return lives;
    }
}