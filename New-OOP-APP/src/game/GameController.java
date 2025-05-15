package game;

import model.CodeQuestion;
import model.Player;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<CodeQuestion> questions;
    private int currentLevel = 0;
    private Player player;  // Added Player field

    public GameController() {
        questions = new ArrayList<>();
        questions.add(new CodeQuestion("System.out.println(\"Hello, ____!\");", "world"));
        questions.add(new CodeQuestion("for(int i = 0; i < __; i++)", "10"));
        questions.add(new CodeQuestion("String name = \"__\";", "java"));
        
        // Initialize with a default player
        player = new Player("Player 1");
    }

    /**
     * Get the current player
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the current player
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Start a specific level
     * @param levelNumber the level to start
     */
    public void startLevel(int levelNumber) {
        currentLevel = levelNumber - 1; // Adjust zero-based index
        
        // Update player's highest level if needed
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
    }

    /**
     * Get the current question for the selected level
     * @return the current code question
     */
    public CodeQuestion getCurrentQuestion() {
        // Make sure currentLevel is within bounds
        if (currentLevel < 0 || currentLevel >= questions.size()) {
            currentLevel = 0;
        }
        return questions.get(currentLevel);
    }

    /**
     * Check if the provided answer is correct
     * @param answer the player's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(String answer) {
        return getCurrentQuestion().getAnswer().equalsIgnoreCase(answer);
    }

    /**
     * Advance to the next level and update player progress
     * @return the next level number
     */
    public int getNextLevel() {
        currentLevel++;
        if (currentLevel >= questions.size()) currentLevel = 0;
        
        // Update player's level if they've advanced
        int levelNumber = currentLevel + 1; // Convert to 1-based index
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
        
        return currentLevel + 1; // Return 1-based level number
    }
    
    /**
     * Award points to the player for correct answers
     * @param points number of points to award
     */
    public void awardPoints(int points) {
        player.setScore(player.getScore() + points);
    }
}