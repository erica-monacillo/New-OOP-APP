package view;

import game.GameController;
import model.CodeQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {
    private GameController controller;
    private JLabel questionLabel;
    private JPanel letterCirclePanel;
    private JTextField answerField;
    private List<JButton> letterButtons;
    private StringBuilder selectedLetters;
    private Set<JButton> visitedButtons;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private int score = 0;
 

    public GamePanel(GameController controller) {
        this.controller = controller;
        this.letterButtons = new ArrayList<>();
        this.selectedLetters = new StringBuilder();
        this.visitedButtons = new HashSet<>();
        setupUI();
    }

    private void setupUI() {
    setLayout(new BorderLayout());
    setBackground(Color.DARK_GRAY); // Dark mode background

    // Initialize score label
    scoreLabel = new JLabel("Score: " + score, SwingConstants.LEFT);
    scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
    scoreLabel.setForeground(Color.LIGHT_GRAY);
    scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Initialize question label BEFORE using it
    questionLabel = new JLabel("", SwingConstants.CENTER);
    questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    questionLabel.setForeground(Color.WHITE);
    questionLabel.setOpaque(true);
    questionLabel.setBackground(new Color(50, 50, 50));
    questionLabel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));  // Border around question

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(new Color(50, 50, 50));
    topPanel.add(scoreLabel, BorderLayout.WEST);
    topPanel.add(questionLabel, BorderLayout.CENTER);

    add(topPanel, BorderLayout.NORTH);

    // Feedback label (e.g., "Correct!" or "Try again.")
    feedbackLabel = new JLabel("", SwingConstants.CENTER);
    feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
    feedbackLabel.setForeground(Color.YELLOW);
    feedbackLabel.setOpaque(false);
    add(feedbackLabel, BorderLayout.AFTER_LAST_LINE);

    // Circle of letters in the center
    letterCirclePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawLetterCircle(g);
        }
    };
    letterCirclePanel.setPreferredSize(new Dimension(400, 400));
    letterCirclePanel.setLayout(null);
    letterCirclePanel.setBackground(new Color(30, 30, 30));
    add(letterCirclePanel, BorderLayout.CENTER);

    // Answer field and submit button at the bottom
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBackground(new Color(30, 30, 30));

    answerField = new JTextField();
    answerField.setEditable(false);
    answerField.setBackground(new Color(40, 40, 40));
    answerField.setForeground(Color.WHITE);
    answerField.setCaretColor(Color.WHITE);
    answerField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
    bottomPanel.add(answerField, BorderLayout.CENTER);

    JButton submitButton = new JButton("Submit");
    submitButton.setBackground(new Color(70, 130, 180));
    submitButton.setForeground(Color.WHITE);
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(this::onSubmit);
    bottomPanel.add(submitButton, BorderLayout.EAST);

    JButton clearButton = new JButton("Clear");
    clearButton.setBackground(new Color(100, 100, 100));
    clearButton.setForeground(Color.WHITE);
    clearButton.setFocusPainted(false);
    clearButton.addActionListener(evt -> {
        selectedLetters.setLength(0);
        visitedButtons.clear();
        answerField.setText("");
        feedbackLabel.setText(""); // Clear feedback if any
    });
    bottomPanel.add(clearButton, BorderLayout.WEST);

    add(bottomPanel, BorderLayout.SOUTH);
}

    public void startLevel(int levelNumber) {
        controller.startLevel(levelNumber);
        updateQuestionDisplay();
    }

    private void updateQuestionDisplay() {
        CodeQuestion question = controller.getCurrentQuestion();
        questionLabel.setText("<html><pre>" + question.getQuestionText() + "</pre></html>");
        setupLetterCircle(question.getAvailableLetters());
        answerField.setText("");
        selectedLetters.setLength(0);
        visitedButtons.clear();
    }

    private void setupLetterCircle(String letters) {
        letterCirclePanel.removeAll();
        letterButtons.clear();

        int radius = 150;
        int centerX = letterCirclePanel.getWidth() / 2;
        int centerY = letterCirclePanel.getHeight() / 2;

        double angleStep = 2 * Math.PI / letters.length();
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            double angle = i * angleStep;

            int x = (int) (centerX + radius * Math.cos(angle)) - 25;
            int y = (int) (centerY + radius * Math.sin(angle)) - 25;

            JButton letterButton = new JButton(String.valueOf(letter));
            letterButton.setBounds(x, y, 50, 50);
            letterButton.setFont(new Font("Arial", Font.BOLD, 20));
            letterButton.setBackground(new Color(50, 50, 50));
            letterButton.setForeground(Color.WHITE);
            letterButton.setFocusPainted(false);
            letterButton.addMouseListener(new DragMouseListener(letterButton, letter));
            letterButton.addMouseListener(new HoverEffectListener(letterButton));  // Hover effect listener
            letterButtons.add(letterButton);
            letterCirclePanel.add(letterButton);
        }

        letterCirclePanel.revalidate();
        letterCirclePanel.repaint();
    }

    private void drawLetterCircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(70, 130, 180)); // Steel blue circle border
        g2d.setStroke(new BasicStroke(3));
        int centerX = letterCirclePanel.getWidth() / 2;
        int centerY = letterCirclePanel.getHeight() / 2;
        int radius = 150;
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private void onSubmit(java.awt.event.ActionEvent e) {
        String userAnswer = selectedLetters.toString();
        if (controller.checkAnswer(userAnswer)) {
            feedbackLabel.setText("Correct!");
            feedbackLabel.setForeground(Color.GREEN);
            
            // Increment score
            score += 10;
            updateScoreDisplay();

            // Optional: delay next level by 1 second
            Timer timer = new Timer(1000, evt -> {
                startLevel(controller.getNextLevel());
                feedbackLabel.setText(""); // Clear after next level starts
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            feedbackLabel.setText("Incorrect. Try again.");
            feedbackLabel.setForeground(Color.RED);
            
            // Optional: penalize wrong answers
            score = Math.max(0, score - 2);  // Prevent negative scores
            updateScoreDisplay();
        }
    }
    
    // Method to update the score display
    private void updateScoreDisplay() {
        scoreLabel.setText("Score: " + score);
    }

    // Method to get the current score (can be used by other classes)
    public int getScore() {
        return score;
    }

    private class DragMouseListener extends MouseAdapter {
        private JButton button;
        private char letter;

        public DragMouseListener(JButton button, char letter) {
            this.button = button;
            this.letter = letter;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                visitedButtons.add(button);
                selectedLetters.append(letter);
                answerField.setText(selectedLetters.toString());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && !visitedButtons.contains(button)) {
                visitedButtons.add(button);
                selectedLetters.append(letter);
                answerField.setText(selectedLetters.toString());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            visitedButtons.clear();
            selectedLetters.setLength(0);
            answerField.setText("");
        }
    }

    // Hover Effect for buttons (change color on hover)
    private class HoverEffectListener extends MouseAdapter {
        private JButton button;

        public HoverEffectListener(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(new Color(100, 100, 100)); // Lighter shade on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(new Color(50, 50, 50)); // Original dark color
        }
    }
}