package view;

import game.GameController;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GameController controller;
    private JPanel currentPanel;

    public GameFrame(GameController controller) {
        this.controller = controller;
        setupUI();
    }

    private void setupUI() {
        setTitle("Codespace");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
    }

    public void showMainMenu() {
        // Display a simple main menu
        int option = JOptionPane.showConfirmDialog(
            this,
            "Welcome to Codespace! Start the game?",
            "Main Menu",
            JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            showGameScreen();
        } else {
            System.exit(0);
        }
    }

    public void showGameScreen() {
        // Remove the current panel if it exists
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Create and display the game panel
        GamePanel gamePanel = new GamePanel(controller);
        currentPanel = gamePanel;
        add(gamePanel, BorderLayout.CENTER);

        // Refresh the frame
        revalidate();
        repaint();

        // Start the first level
        gamePanel.startLevel(0);
    }
}