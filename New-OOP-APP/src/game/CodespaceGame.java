package game;

import view.GameFrame;

public class CodespaceGame {
    private GameFrame gameFrame;

    public CodespaceGame() {
        GameController controller = new GameController();
        this.gameFrame = new GameFrame(controller);
    }

    public void start() {
        gameFrame.showMainMenu();
    }
}