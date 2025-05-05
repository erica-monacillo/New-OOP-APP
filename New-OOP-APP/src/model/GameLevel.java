package model;

import utils.QuestionLoader;
import java.util.List;

public class GameLevel {
    private int levelNumber;
    private List<CodeQuestion> questions;
    private int currentQuestionIndex;
    private int points;

    public GameLevel(int levelNumber) {
        this.levelNumber = levelNumber;
        this.questions = QuestionLoader.loadQuestionsForLevel(levelNumber);
        this.currentQuestionIndex = 0;
        this.points = levelNumber * 100; // More points for higher levels
    }

    public CodeQuestion getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public boolean checkAnswer(String userAnswer) {
        boolean isCorrect = getCurrentQuestion().checkAnswer(userAnswer);
        if (isCorrect && hasNextQuestion()) {
            currentQuestionIndex++;
        }
        return isCorrect;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public int getPoints() {
        return points;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}