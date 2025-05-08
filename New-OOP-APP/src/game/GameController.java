package game;

import model.CodeQuestion;
import model.GameLevel;
import model.Player;
import utils.QuestionLoader;

import java.util.List;

public class GameController {
    private Player player;
    private List<GameLevel> levels;
    private int currentLevelIndex;

    public GameController() {
        this.player = new Player();
        this.levels = QuestionLoader.loadLevels();
        this.currentLevelIndex = 0;
    }

    public void startLevel(int levelNumber) {
        currentLevelIndex = levelNumber;
    }

    public CodeQuestion getCurrentQuestion() {
        return levels.get(currentLevelIndex).getQuestion();
    }

    public boolean checkAnswer(String answer) {
        return getCurrentQuestion().isCorrectAnswer(answer.trim());
    }

    public Player getPlayer() {
        return player;
    }

    public int getNextLevel() {
        throw new UnsupportedOperationException("Unimplemented method 'getNextLevel'");
    }
}