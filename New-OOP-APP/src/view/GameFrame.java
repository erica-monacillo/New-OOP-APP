package view;

import game.GameController;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GameController controller;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private LevelSelectPanel levelSelectPanel;

    public GameFrame(GameController controller) {
        this.controller = controller;
        setupUI();
    }

    private void setupUI() {
        setTitle("Codespace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel(controller);
        levelSelectPanel = new LevelSelectPanel(controller);

        mainPanel.add(levelSelectPanel, "LEVEL_SELECT");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel);
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "LEVEL_SELECT");
        setVisible(true);
    }

    public void showGameScreen() {
        cardLayout.show(mainPanel, "GAME");
    }
}