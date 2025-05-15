package view;

import game.GameController;

import javax.swing.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private MainMenu mainMenu;
    private GameController controller;

    public GameFrame() {
        setTitle("Codespace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(true); 
        
        // Initialize the controller
        controller = new GameController();
        
        // Initialize the game panel (but don't add it yet)
        gamePanel = new GamePanel(controller);
        
        // Initialize and show the main menu first
        mainMenu = new MainMenu(this);
        add(mainMenu);
    }

    public void startApp() {
        setVisible(true);
    }
    
    public void startGame() {
        // Remove the main menu
        getContentPane().removeAll();
        
        // Add the game panel
        add(gamePanel);
        
        // Start the first level
        gamePanel.startLevel(0);
        
        // Refresh the frame
        revalidate();
        repaint();
    }
    
    public void returnToMainMenu() {
        // Remove the game panel
        getContentPane().removeAll();
        
        // Add the main menu back
        add(mainMenu);
        
        // Refresh the frame
        revalidate();
        repaint();
    }
}