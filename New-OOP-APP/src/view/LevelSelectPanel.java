package view;

import game.GameController;
import model.Player;

import javax.swing.*;
import java.awt.*;

public class LevelSelectPanel extends JPanel {
    private GameController controller;
    private JLabel titleLabel;
    private JButton[] levelButtons;
    private JLabel playerInfoLabel;

    public LevelSelectPanel(GameController controller) {
        this.controller = controller;
        setupUI();
        updatePlayerInfo();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel = new JLabel("Codespace - Select Level", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Player info
        playerInfoLabel = new JLabel("", SwingConstants.CENTER);
        playerInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(playerInfoLabel, BorderLayout.SOUTH);

        // Level buttons grid
        JPanel levelsPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        levelButtons = new JButton[15]; // 15 levels

        for (int i = 0; i < levelButtons.length; i++) {
            final int level = i + 1;
            levelButtons[i] = new JButton("Level " + level);
            levelButtons[i].setFont(new Font("Arial", Font.BOLD, 16));

            // Style buttons differently based on completion status
            styleLevelButton(levelButtons[i], level);

            levelButtons[i].addActionListener(e -> {
                controller.startLevel(level);
                ((GameFrame)SwingUtilities.getWindowAncestor(this)).showGameScreen();
            });

            levelsPanel.add(levelButtons[i]);
        }

        JScrollPane scrollPane = new JScrollPane(levelsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleLevelButton(JButton button, int level) {
        // Using the correct method getPlayer() instead of getCurrentPlayer()
        Player player = controller.getPlayer();
        
        if (level <= player.getCurrentLevel()) {
            // Available level
            button.setBackground(new Color(100, 200, 100));
            button.setEnabled(true);
        } else {
            // Locked level
            button.setBackground(Color.LIGHT_GRAY);
            button.setEnabled(false);
        }

        // Make buttons more visually appealing
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 60));
    }

    public void updatePlayerInfo() {
        // Using the correct method getPlayer() instead of getCurrentPlayer()
        Player player = controller.getPlayer();
        
        playerInfoLabel.setText("Player: " + player.getName() + " | Score: " + player.getScore() + 
                              " | Highest Level: " + player.getCurrentLevel());

        // Update button states
        for (int i = 0; i < levelButtons.length; i++) {
            styleLevelButton(levelButtons[i], i + 1);
        }
    }
}