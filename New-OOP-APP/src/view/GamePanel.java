package view;

import game.GameController;
import model.CodeQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {
    private GameController controller;
    private JLabel questionLabel;
    private JPanel letterCirclePanel;
    private JTextField answerField;
    private List<RoundButton> letterButtons;
    private StringBuilder selectedLetters;
    private Set<RoundButton> visitedButtons;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private int score = 0;
    private Point dragEnd;
    private boolean isDragging = false;
    private List<Point> dragPath = new ArrayList<>();
    private boolean isMousePressed = false;
    private boolean selectionActive = false;  

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

        scoreLabel = new JLabel("Score: " + score, SwingConstants.LEFT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
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
                drawSelectionLines(g);  // Always draw the selection lines
            }
        };
        letterCirclePanel.setPreferredSize(new Dimension(400, 400));
        letterCirclePanel.setLayout(null);
        letterCirclePanel.setBackground(new Color(30, 30, 30));
        add(letterCirclePanel, BorderLayout.CENTER);

        letterCirclePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isMousePressed) {
                    finishSelection();
                }
            }
        });

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
            resetSelection();
        });
        bottomPanel.add(clearButton, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void resetSelection() {
        selectedLetters.setLength(0);
        visitedButtons.clear();
        answerField.setText("");
        feedbackLabel.setText("");
        dragPath.clear();
        selectionActive = false;
        isMousePressed = false;
        isDragging = false;
        dragEnd = null;
        resetAllButtonColors();
        letterCirclePanel.repaint();
    }

    private void resetAllButtonColors() {
        for (RoundButton button : letterButtons) {
            button.setBackground(new Color(50, 50, 50));
        }
    }

    private void finishSelection() {
        isMousePressed = false;
        isDragging = false;
        dragEnd = null;
        selectionActive = true;
        letterCirclePanel.repaint();
    }

    private void drawSelectionLines(Graphics g) {
        if (dragPath.isEmpty()) return;
        
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
        
        // Draw the current drag line to cursor (only when actively dragging)
        if (isDragging && dragPath.size() > 0 && dragEnd != null) {
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
        dragEnd = null;
        isMousePressed = false;
        isDragging = false;
        selectionActive = false;

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

            RoundButton letterButton = new RoundButton(String.valueOf(letter));
            letterButton.setBounds(pos.x, pos.y, 50, 50);
            letterButton.setFont(new Font("Arial", Font.BOLD, 20));
            letterButton.setBackground(new Color(50, 50, 50));
            letterButton.setForeground(Color.WHITE);
            letterButton.setFocusPainted(false);
            
            // Add modified mouse listeners
            LetterButtonListener buttonListener = new LetterButtonListener(letterButton, letter);
            letterButton.addMouseListener(buttonListener);
            letterButton.addMouseMotionListener(buttonListener);
            
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

    // Custom round button class
    private class RoundButton extends JButton {
        public RoundButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillOval(0, 0, getWidth(), getHeight());
            
            // Draw text
            FontMetrics metrics = g2.getFontMetrics(getFont());
            Rectangle2D stringBounds = metrics.getStringBounds(getText(), g2);
            int x = (getWidth() - (int) stringBounds.getWidth()) / 2;
            int y = (getHeight() - (int) stringBounds.getHeight()) / 2 + metrics.getAscent();
            
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (getModel().isRollover()) {
                return super.contains(x, y);
            } else {
                int radius = Math.min(getWidth(), getHeight()) / 2;
                return Point.distance(x, y, getWidth() / 2, getHeight() / 2) <= radius;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int diameter = Math.max(size.width, size.height);
            return new Dimension(diameter, diameter);
        }
    }

    private class LetterButtonListener extends MouseAdapter {
        private RoundButton button;
        private char letter;

        public LetterButtonListener(RoundButton button, char letter) {
            this.button = button;
            this.letter = letter;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectionActive) {
                resetSelection();
            }
            
            // Start the selection process
            isMousePressed = true;
            isDragging = true;
            
            // Add this button as first selection
            selectButton();
            
            // Update drag points
            Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
            dragEnd = center;
            dragPath.add(center);
            
            updateQuestionDisplay();
            letterCirclePanel.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isMousePressed && !visitedButtons.contains(button)) {
                // Add this letter to selection when hovering while pressed
                selectButton();
                
                // Update drag path
                Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
                dragPath.add(center);
                dragEnd = center;
                
                updateQuestionDisplay();
                letterCirclePanel.repaint();
            } else if (!isMousePressed && !visitedButtons.contains(button)) {
                button.setBackground(new Color(100, 100, 100));
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                button.setBackground(new Color(50, 50, 50));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            finishSelection();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging) {
                // Update the drag end point for drawing the line
                dragEnd = SwingUtilities.convertPoint(button, e.getPoint(), letterCirclePanel);
                letterCirclePanel.repaint();
            }
        }
        
        private void selectButton() {
            button.setBackground(Color.RED);
            visitedButtons.add(button);
            selectedLetters.append(letter);
        }
    }
}