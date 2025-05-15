package view;

import game.GameController;

import javax.swing.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameController controller;

    public GameFrame() {
        setTitle("Code Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        controller = new GameController();
        gamePanel = new GamePanel(controller);
        add(gamePanel);
    }

    public void showMainMenu() {
        setVisible(true);
        showGameScreen();
    }

    public void showGameScreen() {
        gamePanel.startLevel(0);
    }
}
