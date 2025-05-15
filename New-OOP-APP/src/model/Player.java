package model;

/**
 * Represents a player in the coding game with their progress information
 */
public class Player {
    private String name;
    private int score;
    private int currentLevel;

    /**
     * Create a new player with the given name
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.currentLevel = 1; // Players start at level 1
    }

    /**
     * Get the player's name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player's name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the player's current score
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the player's score
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Get the highest level the player has reached
     * @return the current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Set the player's current level
     * @param currentLevel the level to set
     */
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}