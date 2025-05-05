package model;

public class CodeQuestion {
    private String questionText;
    private String correctAnswer;
    private String explanation;
    private int difficulty;

    public CodeQuestion(String questionText, String correctAnswer, String explanation, int difficulty) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.difficulty = difficulty;
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getExplanation() { return explanation; }
    public int getDifficulty() { return difficulty; }

    public boolean checkAnswer(String userAnswer) {
        return correctAnswer.equalsIgnoreCase(userAnswer.trim());
    }
}