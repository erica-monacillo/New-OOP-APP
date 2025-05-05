package game;

import model.CodeQuestion;
import model.GameLevel;
import model.Player;
import view.GamePanel;

public class GameController {
    private Player player;
    private GameLevel currentLevel;
    private GameTimer timer;

    public GameController(Player player) {
        this.player = player;
        this.timer = new GameTimer(60); // 60 seconds per level
    }

    public void startLevel(int levelNumber) {
        this.currentLevel = new GameLevel(levelNumber);
        timer.reset();
        timer.start();
    }

    public boolean checkAnswer(String userAnswer) {
        boolean isCorrect = currentLevel.checkAnswer(userAnswer);
        if (isCorrect) {
            player.incrementScore(currentLevel.getPoints());
            timer.stop();
        }
        return isCorrect;
    }

    public CodeQuestion getCurrentQuestion() {
        return currentLevel.getCurrentQuestion();
    }

    public int getTimeLeft() {
        return timer.getTimeLeft();
    }

    public Player getPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }
}