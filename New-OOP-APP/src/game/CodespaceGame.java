package game;

// Removed unused import
import model.Player;
import view.GameFrame;

public class CodespaceGame {
    private GameFrame gameFrame;
    private Player player;
    private GameController controller;

    public CodespaceGame() {
        this.player = new Player();
        this.controller = new GameController(player);
        this.gameFrame = new GameFrame(controller);
    }

    public void start() {
        gameFrame.showMainMenu();
    }
}