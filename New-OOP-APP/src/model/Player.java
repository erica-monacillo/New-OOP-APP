package model;

public class Player {
    private String name;
    private int score;
    private int currentLevel;

    public Player() {
        this("Player1");
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.currentLevel = 1;
    }

    public void incrementScore(int points) {
        this.score += points;
    }

    public void advanceLevel() {
        this.currentLevel++;
    }

    // Getters and setters
    public String getName() { return name; }
    public int getScore() { return score; }
    public int getCurrentLevel() { return currentLevel; }
    public void setName(String name) { this.name = name; }
}