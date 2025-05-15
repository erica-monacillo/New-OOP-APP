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
    private Point dragStart;
    private Point dragEnd;
    private boolean isDragging = false;
    private List<Point> dragPath = new ArrayList<>();

    public GamePanel(GameController controller) {
        this.controller = controller;
        this.letterButtons = new ArrayList<>();
        this.selectedLetters = new StringBuilder();
        this.visitedButtons = new HashSet<>();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Initialize score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.LEFT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Initialize question label
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(new Color(50, 50, 50));
        questionLabel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.add(scoreLabel, BorderLayout.WEST);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Feedback label
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
                if (isDragging && dragStart != null && dragEnd != null) {
                    drawDragLine(g);
                }
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
            feedbackLabel.setText("");
            dragPath.clear();
            letterCirclePanel.repaint();
        });
        bottomPanel.add(clearButton, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void drawDragLine(Graphics g) {
        if (dragStart == null || dragEnd == null) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the connecting lines between selected letters
        if (dragPath.size() > 1) {
            // Draw the solid path between letters
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));
            
            // Draw lines between consecutive points
            for (int i = 0; i < dragPath.size() - 1; i++) {
                Point p1 = dragPath.get(i);
                Point p2 = dragPath.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        
        // Draw the current drag line to cursor
        if (dragPath.size() > 0) {
            Point lastPoint = dragPath.get(dragPath.size() - 1);
            g2d.setColor(new Color(255, 0, 0, 150)); // Semi-transparent red
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(lastPoint.x, lastPoint.y, dragEnd.x, dragEnd.y);
        }
        
        // Draw the path points with a glowing effect
        for (Point p : dragPath) {
            // Draw outer glow
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.fillOval(p.x - 12, p.y - 12, 24, 24);
            // Draw middle glow
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.fillOval(p.x - 8, p.y - 8, 16, 16);
            // Draw inner point
            g2d.setColor(new Color(255, 0, 0, 200));
            g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
        }
    }

    public void startLevel(int levelNumber) {
        controller.startLevel(levelNumber);
        CodeQuestion question = controller.getCurrentQuestion();
        setupLetterCircle(question.getAvailableLetters());
        updateQuestionDisplay();
    }

    private void updateQuestionDisplay() {
        CodeQuestion question = controller.getCurrentQuestion();
        String questionText = question.getQuestionText();
        // Replace underscores with selected letters in order
        String displayText = questionText.replace("____", selectedLetters.toString());
        questionLabel.setText("<html><pre>" + displayText + "</pre></html>");
        answerField.setText(selectedLetters.toString());
        letterCirclePanel.repaint();
    }

    private void setupLetterCircle(String letters) {
        letterCirclePanel.removeAll();
        letterButtons.clear();
        selectedLetters.setLength(0);
        visitedButtons.clear();
        dragPath.clear();
        dragStart = null;
        dragEnd = null;

        int radius = 150;
        int centerX = letterCirclePanel.getWidth() / 2;
        int centerY = letterCirclePanel.getHeight() / 2;

        // Calculate positions first to ensure consistent placement
        List<Point> positions = new ArrayList<>();
        double angleStep = 2 * Math.PI / letters.length();
        for (int i = 0; i < letters.length(); i++) {
            double angle = i * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle)) - 25;
            int y = (int) (centerY + radius * Math.sin(angle)) - 25;
            positions.add(new Point(x, y));
        }

        // Create buttons with fixed positions
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            Point pos = positions.get(i);

            JButton letterButton = new JButton(String.valueOf(letter));
            letterButton.setBounds(pos.x, pos.y, 50, 50);
            letterButton.setFont(new Font("Arial", Font.BOLD, 20));
            letterButton.setBackground(new Color(50, 50, 50));
            letterButton.setForeground(Color.WHITE);
            letterButton.setFocusPainted(false);
            letterButton.addMouseListener(new DragMouseListener(letterButton, letter, i));
            letterButton.addMouseListener(new HoverEffectListener(letterButton));
            letterButtons.add(letterButton);
            letterCirclePanel.add(letterButton);
        }

        letterCirclePanel.revalidate();
        letterCirclePanel.repaint();
    }

    private void drawLetterCircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(70, 130, 180));
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

            Timer timer = new Timer(1000, evt -> {
                startLevel(controller.getNextLevel());
                feedbackLabel.setText("");
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            feedbackLabel.setText("Incorrect. Try again.");
            feedbackLabel.setForeground(Color.RED);
        }
    }

    private class DragMouseListener extends MouseAdapter {
        private JButton button;
        private char letter;
        private int position;
        private Point originalPosition;

        public DragMouseListener(JButton button, char letter, int position) {
            this.button = button;
            this.letter = letter;
            this.position = position;
            this.originalPosition = button.getLocation();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
                dragStart = center;
                dragEnd = center;
                dragPath.clear();
                dragPath.add(center);
                visitedButtons.add(button);
                selectedLetters.append(letter);
                button.setBackground(Color.RED);
                updateQuestionDisplay();
                letterCirclePanel.repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (dragStart != null) {
                dragEnd = e.getPoint();
                letterCirclePanel.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragPath.clear();
            dragStart = null;
            dragEnd = null;
            letterCirclePanel.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                visitedButtons.add(button);
                selectedLetters.append(letter);
                button.setBackground(new Color(70, 130, 180));
                Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
                dragPath.add(center);
                updateQuestionDisplay();
                letterCirclePanel.repaint();
            }
        }
    }

    private class HoverEffectListener extends MouseAdapter {
        private JButton button;

        public HoverEffectListener(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                button.setBackground(new Color(100, 100, 100));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                button.setBackground(new Color(50, 50, 50));
            }
        }
    }
}
