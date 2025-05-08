package view;

import game.GameController;
import model.CodeQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private Set<JButton> visitedButtons; // To track buttons visited during drag

    public GamePanel(GameController controller) {
        this.controller = controller;
        this.letterButtons = new ArrayList<>();
        this.selectedLetters = new StringBuilder();
        this.visitedButtons = new HashSet<>();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Question label at the top
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        // Circle of letters in the center
        letterCirclePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLetterCircle(g);
            }
        };
        letterCirclePanel.setPreferredSize(new Dimension(400, 400));
        letterCirclePanel.setLayout(null); // Absolute positioning for buttons
        add(letterCirclePanel, BorderLayout.CENTER);

        // Answer field and submit button at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        answerField = new JTextField();
        answerField.setEditable(false);
        bottomPanel.add(answerField, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> onSubmit());
        bottomPanel.add(submitButton, BorderLayout.EAST);

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
    
        int radius = 150; // Radius of the circle
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
            letterButton.addMouseListener(new DragMouseListener(letterButton, letter));
            letterButtons.add(letterButton);
            letterCirclePanel.add(letterButton);
        }
    
        letterCirclePanel.revalidate();
        letterCirclePanel.repaint();
    }

    private void drawLetterCircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(3));
        int centerX = letterCirclePanel.getWidth() / 2;
        int centerY = letterCirclePanel.getHeight() / 2;
        int radius = 150;
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private void onSubmit() {
        String userAnswer = selectedLetters.toString();
        System.out.println("User Answer: " + userAnswer);
        System.out.println("Correct Answer: " + controller.getCurrentQuestion().getCorrectAnswer());
    
        if (controller.checkAnswer(userAnswer)) {
            JOptionPane.showMessageDialog(this, "Correct!");
            startLevel(controller.getNextLevel());
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect. Try again.");
        }
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
            // Start dragging
            if (!visitedButtons.contains(button)) {
                visitedButtons.add(button);
                selectedLetters.append(letter);
                answerField.setText(selectedLetters.toString());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Continue dragging
            if (SwingUtilities.isLeftMouseButton(e) && !visitedButtons.contains(button)) {
                visitedButtons.add(button);
                selectedLetters.append(letter);
                answerField.setText(selectedLetters.toString());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Stop dragging
            visitedButtons.clear();
        }
    }
}