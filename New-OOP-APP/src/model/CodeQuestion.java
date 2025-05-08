package model;

public class CodeQuestion {
    private String questionText;
    private String correctAnswer;
    private String availableLetters;

    public CodeQuestion(String questionText, String correctAnswer, String availableLetters) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.availableLetters = availableLetters;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAvailableLetters() {
        return availableLetters;
    }

    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equals(answer);
    }

    public String getCorrectAnswer() {
        throw new UnsupportedOperationException("Unimplemented method 'getCorrectAnswer'");
    }
}