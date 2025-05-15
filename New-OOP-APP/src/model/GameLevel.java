package model;

/**
 * Represents a level in the game with a coding question
 */
public class GameLevel {
    private CodeQuestion question;
    private boolean completed;
    
    /**
     * Creates a new game level with the given question
     * @param question the coding question for this level
     */
    public GameLevel(CodeQuestion question) {
        this.question = question;
        this.completed = false;
    }
    
    /**
     * Get the coding question for this level
     * @return the question
     */
    public CodeQuestion getQuestion() {
        return question;
    }
    
    /**
     * Set the coding question for this level
     * @param question the question to set
     */
    public void setQuestion(CodeQuestion question) {
        this.question = question;
    }
    
    /**
     * Check if this level has been completed
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Set the completion status for this level
     * @param completed the completion status to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}