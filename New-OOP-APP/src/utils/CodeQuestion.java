package utils;

/**
 * Represents a coding question with prompt, answer, and available letters
 */
public class CodeQuestion {
    private String questionText;
    private String answer;
    private String availableLetters; // Added this field for the available letters

    /**
     * Creates a new coding question with the given text and answer
     * @param questionText the question text with blanks to fill
     * @param answer the correct answer
     */
    public CodeQuestion(String questionText, String answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    /**
     * Creates a new coding question with the given text, answer, and available letters
     * @param questionText the question text with blanks to fill
     * @param answer the correct answer
     * @param availableLetters the letters available to the player
     */
    public CodeQuestion(String questionText, String answer, String availableLetters) {
        this.questionText = questionText;
        this.answer = answer;
        this.availableLetters = availableLetters;
    }

    /**
     * Get the question text
     * @return the question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Set the question text
     * @param questionText the question text to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Get the correct answer
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set the correct answer
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Get the available letters for this question
     * @return the available letters
     */
    public String getAvailableLetters() {
        return availableLetters;
    }

    /**
     * Set the available letters for this question
     * @param availableLetters the available letters to set
     */
    public void setAvailableLetters(String availableLetters) {
        this.availableLetters = availableLetters;
    }
}