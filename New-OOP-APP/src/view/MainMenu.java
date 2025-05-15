package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Menu for Codespace game
 * A word puzzle game with a programming theme
 */
public class MainMenu extends JPanel {
    private GameFrame parentFrame;
    private final Color BACKGROUND_COLOR = new Color(40, 44, 52);  // Dark code editor background
    private final Color TEXT_COLOR = new Color(171, 178, 191);     // Light text color
    private final Color ACCENT_COLOR = new Color(97, 175, 239);    // Blue accent
    private final Font TITLE_FONT = new Font("Monospace", Font.BOLD, 48);
    private final Font BUTTON_FONT = new Font("Monospace", Font.BOLD, 18);
    
    private JButton playButton;
    private JButton exitButton;
    private JButton aboutButton;
    
    public MainMenu(GameFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        initComponents();
    }
    
    private void initComponents() {
        // Title Panel (North)
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Buttons Panel (Center)
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.CENTER);
        
        // Footer Panel (South)
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        
        JLabel titleLabel = new JLabel("CODESPACE", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ACCENT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Solve the code, master the words", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Monospace", Font.ITALIC, 16));
        subtitleLabel.setForeground(TEXT_COLOR);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return titlePanel;
    }
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        playButton = createMenuButton("PLAY");
        exitButton = createMenuButton("EXIT");
        aboutButton = createMenuButton("ABOUT");
        
        // Add action listeners
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.startGame();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });
        
        // Add buttons to panel with spacing
        buttonsPanel.add(Box.createVerticalGlue());
        addButtonWithSpacing(buttonsPanel, playButton);
        addButtonWithSpacing(buttonsPanel, exitButton);
        addButtonWithSpacing(buttonsPanel, aboutButton);
        buttonsPanel.add(Box.createVerticalGlue());
        
        return buttonsPanel;
    }
    
    private void addButtonWithSpacing(JPanel panel, JButton button) {
        panel.add(Box.createVerticalStrut(15));
        panel.add(button);
        panel.add(Box.createVerticalStrut(15));
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(new Color(59, 64, 72));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(59, 64, 72));
                button.setForeground(TEXT_COLOR);
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setForeground(new Color(100, 110, 120));
        footerPanel.add(versionLabel);
        
        return footerPanel;
    }
    
    private void showAboutDialog() {
        String aboutMessage = 
            "Codespace - A word puzzle  programming game\n\n" +
            "Create words from available letters to solve coding challenges.\n" +
            "© 2025 Codespace Team wow";
            
        JOptionPane.showMessageDialog(
            parentFrame,
            aboutMessage,
            "About Codespace",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}